package isi.dan.ms.pedidos.utils;

import org.springframework.beans.factory.annotation.Autowired;

import isi.dan.ms.pedidos.dao.PedidoRepository;
import isi.dan.ms.pedidos.dto.ClienteDTO;
import isi.dan.ms.pedidos.dto.PedidoDTO;
import isi.dan.ms.pedidos.modelo.Cliente;
import isi.dan.ms.pedidos.modelo.Pedido;

public class Mapper {
    @Autowired
    private PedidoRepository pedidoDAO;
    public ClienteDTO clienteToDTO(Cliente cliente){
        ClienteDTO dto = new ClienteDTO();
        dto.setCorreoElectronico(cliente.getCorreoElectronico());
        dto.setCuit(cliente.getCuit());
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        return dto;
    }
    public Cliente DTOtoCliente(ClienteDTO dto){
        Cliente cliente = new Cliente();
        cliente.setCorreoElectronico(dto.getCorreoElectronico());
        cliente.setCuit(dto.getCuit());
        cliente.setId(dto.getId());
        cliente.setNombre(dto.getNombre());
        return cliente;
    }
    public PedidoDTO pedidoToDTO(Pedido pedido){
        PedidoDTO dto = new PedidoDTO();
        dto.setCliente(pedido.getCliente());
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
        pedido.setCliente(dto.getCliente());
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
}
