package isi.dan.ms_productos.servicio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isi.dan.ms_productos.conf.RabbitMQConfig;
import isi.dan.ms_productos.dao.CategoriaRepository;
import isi.dan.ms_productos.dao.ProductoRepository;
import isi.dan.ms_productos.dto.*;
import isi.dan.ms_productos.dao.ProductoRepository;
import isi.dan.ms_productos.exception.ProductoNotFoundException;
import isi.dan.ms_productos.modelo.Producto;

import java.util.List;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    Logger log = LoggerFactory.getLogger(ProductoService.class);

    @RabbitListener(queues = RabbitMQConfig.STOCK_UPDATE_QUEUE)
    public void handleStockUpdate(Message msg) {
        log.info("Recibido {}", msg);
        // buscar el producto
        // actualizar el stock
        // verificar el punto de pedido y generar un pedido
    }


    public Producto newProducto(ProductoDTO productoDTO){
    // Crear el objeto Producto a través de un DTO
    Producto producto = new Producto();
    producto.setNombre(productoDTO.getNombre());
    producto.setDescripcion(productoDTO.getDescripcion());
    producto.setStockActual(0); // Stock inicial siempre es 0
    producto.setStockMinimo(productoDTO.getStockMinimo());
    producto.setPrecio(productoDTO.getPrecioInicial());
    producto.setCategoria(categoriaRepository.findByName(productoDTO.getNombreCategoria()));
    // Guardar el producto
    return productoRepository.saveProducto(producto);
    }

    public Producto saveProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    public Producto getProductoById(Long id) throws ProductoNotFoundException{
        return productoRepository.findById(id).orElseThrow(() -> new ProductoNotFoundException(id));
    }

    public void deleteProducto(Long id) {
        productoRepository.deleteById(id);
    }
}

