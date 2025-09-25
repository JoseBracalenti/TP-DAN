package isi.dan.ms.pedidos.servicio;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import isi.dan.ms.pedidos.dao.PedidoRepository;
import isi.dan.ms.pedidos.dto.ClienteDTO;
import isi.dan.ms.pedidos.dto.ProductoDTO;
import isi.dan.ms.pedidos.dto.StockUpdateDTO;
import isi.dan.ms.pedidos.modelo.DetallePedido;
import isi.dan.ms.pedidos.modelo.EstadoPedido;
import isi.dan.ms.pedidos.modelo.Pedido;
@Service
public class PedidoService {
    
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Autowired
    private RestTemplate restTemplate;
    
    Logger log = LoggerFactory.getLogger(PedidoService.class);
    
    // URLs de los microservicios usando service discovery
    private static final String CLIENTES_URL = "http://MS-CLIENTES/clientes";
    private static final String PRODUCTOS_URL = "http://MS-PRODUCTOS/productos";
    



   public Pedido savePedido(Pedido pedido) {
        pedido.setFecha(Instant.now());
        
        // Generar número de pedido único
        if (pedido.getNumeroPedido() == null) {
            pedido.setNumeroPedido(generateNumeroPedido());
        }
        
        // Verificar que el cliente existe y obtener su información
        ClienteDTO cliente = verificarYObtenerCliente(pedido.getCliente().getId());
        if (cliente == null) {
            pedido.setEstado(EstadoPedido.RECHAZADO);
            log.error("Cliente no encontrado: " + pedido.getCliente().getId());
            return pedidoRepository.save(pedido);
        }
        
        // Actualizar información del cliente en el pedido
        pedido.getCliente().setNombre(cliente.getNombre());
        pedido.getCliente().setCorreoElectronico(cliente.getCorreoElectronico());
        pedido.getCliente().setCuit(cliente.getCuit());
        pedido.getCliente().setMaximoDescubierto(cliente.getMaximoDescubierto());
        
        // Obtener información de productos y actualizar precios
        for (DetallePedido detalle : pedido.getDetalle()) {
            ProductoDTO producto = obtenerProducto(detalle.getProducto().getId());
            if (producto == null) {
                pedido.setEstado(EstadoPedido.RECHAZADO);
                log.error("Producto no encontrado: " + detalle.getProducto().getId());
                return pedidoRepository.save(pedido);
            }
            
            // Actualizar información del producto
            detalle.getProducto().setNombre(producto.getNombre());
            detalle.getProducto().setDescripcion(producto.getDescripcion());
            detalle.getProducto().setCategoria(producto.getNombreCategoria());
            detalle.getProducto().setPrecio(producto.getPrecio());
            
            // Configurar precios del detalle
            detalle.setPrecioUnitario(producto.getPrecio());
            
            if (detalle.getDescuento() == null) {
                detalle.setDescuento(producto.getDescuentoPromocional() != null ? 
                    producto.getDescuentoPromocional() : BigDecimal.ZERO);
            }
            
            // Calcular precio final con descuento
            BigDecimal precioConDescuento = detalle.getPrecioUnitario().subtract(detalle.getDescuento());
            detalle.setPrecioFinal(precioConDescuento.max(BigDecimal.ZERO));
        }
        
        BigDecimal total = calcularMontoTotal(pedido);
        pedido.setTotal(total);

        // Verificar saldo del cliente
        boolean tieneSaldo = verificarSaldoCliente(cliente, total);
        
        if (!tieneSaldo) {
            pedido.setEstado(EstadoPedido.RECHAZADO);
            log.warn("Cliente sin saldo suficiente. Cliente: " + cliente.getId() + 
                    ", Total: " + total + ", Máximo descubierto: " + cliente.getMaximoDescubierto());
            return pedidoRepository.save(pedido);
        }

        // Verificar y actualizar stock de productos
        boolean stockDisponible = actualizarStockProductos(pedido.getDetalle());
        
        if (stockDisponible) {
            pedido.setEstado(EstadoPedido.ACEPTADO);
            log.info("Pedido aceptado. ID: " + pedido.getId() + ", Total: " + total);
        } else {
            pedido.setEstado(EstadoPedido.EN_PREPARACION);
            log.warn("Stock insuficiente, pedido en preparación. ID: " + pedido.getId());
        }

        return pedidoRepository.save(pedido);
    }
    
    private Integer generateNumeroPedido() {
        // Generar un número de pedido único basado en timestamp
        return (int) (System.currentTimeMillis() % 100000);
    }

    private BigDecimal calcularMontoTotal(Pedido pedido) {
        return pedido.getDetalle().stream().map(dp -> dp.getPrecioFinal().multiply(BigDecimal.valueOf(dp.getCantidad()))).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private ClienteDTO verificarYObtenerCliente(Integer clienteId) {
        try {
            ClienteDTO cliente = restTemplate.getForObject(
                CLIENTES_URL + "/" + clienteId, ClienteDTO.class);
            log.info("Cliente obtenido: " + cliente.getId() + " - " + cliente.getNombre());
            return cliente;
        } catch (Exception e) {
            log.error("Error al obtener cliente: " + clienteId, e);
            return null;
        }
    }
    
    private ProductoDTO obtenerProducto(Long productoId) {
        try {
            ProductoDTO producto = restTemplate.getForObject(
                PRODUCTOS_URL + "/" + productoId, ProductoDTO.class);
            log.info("Producto obtenido: " + producto.getId() + " - " + producto.getNombre());
            return producto;
        } catch (Exception e) {
            log.error("Error al obtener producto: " + productoId, e);
            return null;
        }
    }

    private boolean verificarSaldoCliente(ClienteDTO cliente, BigDecimal totalPedido) {
        try {
            log.info("Verificando saldo cliente: " + cliente.getId() + 
                    ", Máximo descubierto: " + cliente.getMaximoDescubierto() + 
                    ", Total pedido: " + totalPedido);
                    
            if (cliente.getMaximoDescubierto().compareTo(totalPedido) >= 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("Error al verificar saldo del cliente: " + cliente.getId(), e);
            return false;
        }
    }

    private boolean actualizarStockProductos(List<DetallePedido> detalles) {
        boolean stockDisponible = true;
        
        for (DetallePedido detalle : detalles) {
            try {
                // Crear DTO para actualizar stock
                StockUpdateDTO stockUpdate = new StockUpdateDTO();
                stockUpdate.setIdProducto(detalle.getProducto().getId());
                stockUpdate.setCantidad(detalle.getCantidad());
                stockUpdate.setPrecio(detalle.getPrecioUnitario());
                
                // Llamar al microservicio de productos para actualizar stock
                HttpEntity<StockUpdateDTO> request = new HttpEntity<>(stockUpdate);
                ResponseEntity<StockUpdateDTO> response = restTemplate.exchange(
                    PRODUCTOS_URL + "/" + detalle.getProducto().getId() + "/actualizar-stock",
                    HttpMethod.PUT, 
                    request, 
                    StockUpdateDTO.class);
                
                if (response.getBody() != null && response.getBody().getCantidad() < 0) {
                    log.warn("Stock insuficiente para producto: " + detalle.getProducto().getId());
                    stockDisponible = false;
                }
                
                log.info("Stock actualizado para producto: " + detalle.getProducto().getId() + 
                        ", cantidad solicitada: " + detalle.getCantidad());
                        
            } catch (Exception e) {
                log.error("Error al actualizar stock para producto: " + detalle.getProducto().getId(), e);
                stockDisponible = false;
            }
        }
        
        return stockDisponible;
    }
     public Pedido actualizarEstado(String id, EstadoPedido nuevoEstado) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);

        if (pedidoOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado");
        }

        Pedido pedido = pedidoOpt.get();
        pedido.setEstado(nuevoEstado);

        if (nuevoEstado == EstadoPedido.CANCELADO) {
            enviarMensajeReponerStock(pedido);
        }

        return pedidoRepository.save(pedido);
    }

    private void enviarMensajeReponerStock(Pedido pedido) {
        for (DetallePedido dp : pedido.getDetalle()) {
            String mensaje = dp.getProducto().getId() + ";" + dp.getCantidad();
            rabbitTemplate.convertAndSend("STOCK_UPDATE_QUEUE", mensaje);
        }
    }

    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido getPedidoById(String id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    public void deletePedido(String id) {
        pedidoRepository.deleteById(id);
    }
}
