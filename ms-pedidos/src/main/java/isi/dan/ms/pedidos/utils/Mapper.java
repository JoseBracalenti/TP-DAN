package isi.dan.ms.pedidos.utils;

import org.springframework.stereotype.Service;

import isi.dan.ms.pedidos.dto.ClienteDTO;
import isi.dan.ms.pedidos.dto.CrearPedidoDTO;
import isi.dan.ms.pedidos.dto.ObraDTO;
import isi.dan.ms.pedidos.dto.PedidoDTO;
import isi.dan.ms.pedidos.modelo.Cliente;
import isi.dan.ms.pedidos.modelo.DetallePedido;
import isi.dan.ms.pedidos.modelo.EstadoPedido;
import isi.dan.ms.pedidos.modelo.Obra;
import isi.dan.ms.pedidos.modelo.Pedido;
import isi.dan.ms.pedidos.modelo.Producto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Mapper {
    public ClienteDTO clienteToDTO(Cliente cliente){
        ClienteDTO dto = new ClienteDTO();
        dto.setCorreoElectronico(cliente.getCorreoElectronico());
        dto.setCuit(cliente.getCuit());
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setObra(cliente.getObra() != null ? obraToObraDTO(cliente.getObra()) : null);
        return dto; 
    }

    // Add this helper method for obra conversion
    public ObraDTO obraToObraDTO(Obra obra) {
        ObraDTO dto = new ObraDTO();
        dto.setId(obra.getId());
        dto.setDireccion(obra.getDireccion());
        dto.setEsRemodelacion(obra.getEsRemodelacion());
        dto.setLat(obra.getLat());
        dto.setLng(obra.getLng());
        dto.setCliente(obra.getCliente() != null ? clienteToDTO(obra.getCliente()) : null);
        dto.setPresupuesto(obra.getPresupuesto());
        return dto;
    }
    public Cliente DTOtoCliente(ClienteDTO dto){
        Cliente cliente = new Cliente();
        cliente.setCorreoElectronico(dto.getCorreoElectronico());
        cliente.setCuit(dto.getCuit());
        cliente.setId(dto.getId());
        cliente.setNombre(dto.getNombre());
        cliente.setObra(dto.getObra() != null ? obraDTOtoObra(dto.getObra()) : null);
        
        return cliente;
    }

    // Add this helper method for ObraDTO to Obra conversion
    public Obra obraDTOtoObra(ObraDTO obraDTO) {
        Obra obra = new Obra();
        obra.setId(obraDTO.getId());
        obra.setDireccion(obraDTO.getDireccion());
        obra.setEsRemodelacion(obraDTO.getEsRemodelacion());
        obra.setLat(obraDTO.getLat());
        obra.setLng(obraDTO.getLng());
        obra.setCliente(obraDTO.getCliente() != null ? DTOtoCliente(obraDTO.getCliente()) : null);
        obra.setPresupuesto(obraDTO.getPresupuesto());
        return obra;
    }
    public PedidoDTO pedidoToDTO(Pedido pedido){
        PedidoDTO dto = new PedidoDTO();
        dto.setCliente(clienteToDTO(pedido.getCliente()));
        dto.setDetalle(pedido.getDetalle());
        dto.setEstado(pedido.getEstado());
        dto.setFecha(pedido.getFecha());
        dto.setId(pedido.getId());
        dto.setNumeroPedido(pedido.getNumeroPedido());
        dto.setObservaciones(pedido.getObservaciones());
        dto.setTotal(pedido.getTotal());
        dto.setUsuario(pedido.getUsuario());
        return dto;
    }
    public Pedido DTOtoPedido(PedidoDTO dto){
        Pedido pedido = new Pedido();
        pedido.setCliente(DTOtoCliente(dto.getCliente()));
        pedido.setDetalle(dto.getDetalle());
        pedido.setEstado(dto.getEstado());
        pedido.setFecha(dto.getFecha());
        pedido.setId(dto.getId());
        pedido.setNumeroPedido(dto.getNumeroPedido());
        pedido.setObservaciones(dto.getObservaciones());
        pedido.setTotal(dto.getTotal());
        pedido.setUsuario(dto.getUsuario());
        return pedido;
    }
    
    public Pedido crearPedidoDTOtoPedido(CrearPedidoDTO dto, String usuario) {
        Pedido pedido = new Pedido();
        
        // Crear cliente básico con solo el ID - sin obra para evitar relaciones circulares
        Cliente cliente = new Cliente();
        cliente.setId(dto.getClienteId());
        cliente.setNombre("Cliente " + dto.getClienteId()); // Nombre temporal
        pedido.setCliente(cliente);
        
        pedido.setObservaciones(dto.getObservaciones());
        pedido.setUsuario(usuario);
        pedido.setFecha(Instant.now());
        pedido.setEstado(EstadoPedido.ACEPTADO);
        
        // Convertir productos a detalles
        List<DetallePedido> detalles = dto.getProductos().stream()
            .map(productoPedido -> {
                DetallePedido detalle = new DetallePedido();
                
                Producto producto = new Producto();
                producto.setId(productoPedido.getId());
                producto.setNombre("Producto " + productoPedido.getId()); // Nombre temporal
                detalle.setProducto(producto);
                
                detalle.setCantidad(productoPedido.getCantidad());
                detalle.setDescuento(BigDecimal.ZERO);
                // Los precios se establecerán en el servicio
                
                return detalle;
            })
            .collect(Collectors.toList());
            
        pedido.setDetalle(detalles);
        
        return pedido;
    }
}
