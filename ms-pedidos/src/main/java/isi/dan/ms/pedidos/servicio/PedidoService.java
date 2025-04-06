package isi.dan.ms.pedidos.servicio;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import isi.dan.ms.pedidos.dao.PedidoRepository;
import isi.dan.ms.pedidos.dto.ClienteDTO;
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

    private final String CLIENTES_URL = "http://ms-clientes-svc:8080/api/clientes";
    private final String PRODUCTOS_URL = "http://ms-productos-svc:8080/api/productos";



   public Pedido savePedido(Pedido pedido) {
        pedido.setFecha(Instant.now());
        BigDecimal total = calcularMontoTotal(pedido);
        pedido.setTotal(total);

        boolean tieneSaldo = verificarSaldoCliente(pedido.getCliente().getId(), total);
        if (!tieneSaldo) {
            pedido.setEstado(EstadoPedido.RECHAZADO);
            return pedidoRepository.save(pedido);
        }

        boolean stockDisponible = actualizarStockProductos(pedido.getDetalle());
        if (stockDisponible) {
            pedido.setEstado(EstadoPedido.EN_PREPARACION);
        } else {
            pedido.setEstado(EstadoPedido.ACEPTADO);
        }

        return pedidoRepository.save(pedido);
    }

    private BigDecimal calcularMontoTotal(Pedido pedido) {
        return pedido.getDetalle().stream().map(dp -> dp.getPrecioFinal().multiply(BigDecimal.valueOf(dp.getCantidad()))).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean verificarSaldoCliente(Integer clienteId, BigDecimal totalPedido) {
        try {
            ClienteDTO cliente = restTemplate.getForObject(
                CLIENTES_URL + "/api/cliente/" + clienteId, ClienteDTO.class);
            if (cliente.getMaximoDescubierto().intValue() >= totalPedido.intValue()) return true;
            else return false;
            
        } catch (Exception e) {
            return false;
        }
    }

    private boolean actualizarStockProductos(List<DetallePedido> detalles) {
        boolean stockDisponible = true;
        for (DetallePedido dp : detalles) {
            Boolean response = restTemplate.exchange(
                PRODUCTOS_URL + dp.getProducto().getId()+ "/actualizar-stock/" ,
                HttpMethod.PUT, null, Boolean.class).getBody();
            
            if (Boolean.FALSE.equals(response)) {
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
