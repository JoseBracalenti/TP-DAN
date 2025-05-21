package isi.dan.ms.pedidos.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import isi.dan.ms.pedidos.dto.PedidoDTO;
import isi.dan.ms.pedidos.modelo.EstadoPedido;
import isi.dan.ms.pedidos.modelo.Pedido;
import isi.dan.ms.pedidos.servicio.PedidoService;
import isi.dan.ms.pedidos.utils.Mapper;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    
    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private Mapper mapper;
    @PostMapping
    public ResponseEntity<PedidoDTO> createPedido(@RequestBody PedidoDTO pedidoDTO) {
        Pedido savedPedido = pedidoService.savePedido(mapper.DTOtoPedido(pedidoDTO));
        return ResponseEntity.ok(mapper.pedidoToDTO(savedPedido));
    }

    @GetMapping
    public List<Pedido> getAllPedidos() {
        return pedidoService.getAllPedidos();
    }
    @PutMapping("/{id}/estado")
    public ResponseEntity<PedidoDTO> actualizarEstado(@PathVariable String id, @RequestParam EstadoPedido nuevoEstado) {
        Pedido pedidoActualizado = pedidoService.actualizarEstado(id, nuevoEstado);
    return pedidoActualizado != null ? ResponseEntity.ok(mapper.pedidoToDTO(pedidoActualizado)) : ResponseEntity.notFound().build();
}
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getPedidoById(@PathVariable String id) {
        Pedido pedido = pedidoService.getPedidoById(id);
        return pedido != null ? ResponseEntity.ok(pedido) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePedido(@PathVariable String id) {
        pedidoService.deletePedido(id);
        return ResponseEntity.noContent().build();
    }
}

