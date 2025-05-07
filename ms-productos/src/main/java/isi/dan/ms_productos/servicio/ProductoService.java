package isi.dan.ms_productos.servicio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isi.dan.ms_productos.conf.RabbitMQConfig;
import isi.dan.ms_productos.dao.ProductoRepository;
import isi.dan.ms_productos.dto.*;
import isi.dan.ms_productos.exception.CategoriaNotFoundException;
import isi.dan.ms_productos.exception.ProductoNotFoundException;
import isi.dan.ms_productos.modelo.Producto;

import java.math.BigDecimal;
import java.util.List;

import isi.dan.ms_productos.utils.Mapper;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private Mapper mapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    Logger log = LoggerFactory.getLogger(ProductoService.class);

    @RabbitListener(queues = RabbitMQConfig.STOCK_UPDATE_QUEUE)
    public void handleStockUpdate(StockUpdateDTO stockUpdateDTO) throws ProductoNotFoundException {
        log.info("Recibido mensaje para actualizar stock: {}", stockUpdateDTO);
        updateProductoStock(stockUpdateDTO.getIdProducto(), stockUpdateDTO);
    }

    public ProductoDTO newProducto(ProductoDTO productoDTO) throws CategoriaNotFoundException{
    return mapper.productoToDTO(productoRepository.save(mapper.dtoToProducto(productoDTO)));
    }

    public UpdateProductoDTO updateProducto (UpdateProductoDTO updateProductoDTO, Long id) throws CategoriaNotFoundException, ProductoNotFoundException{
        Producto producto = productoRepository.findById(id).orElseThrow(( )-> new ProductoNotFoundException(id));

        return mapper.productoToUpdate(productoRepository.save(mapper.updateToProducto(updateProductoDTO, producto)));
    }

    public List<ProductoDTO> getAllProductos() {
        return mapper.listaPToDTO(productoRepository.findAll());
    }

    public ProductoDTO getProductoById(Long id) throws ProductoNotFoundException{
        return mapper.productoToDTO(productoRepository.findById(id).orElseThrow(() -> new ProductoNotFoundException(id)));
    }
        public void deleteProducto(Long id) throws ProductoNotFoundException{
        Producto producto = productoRepository.findById(id).orElseThrow(() -> new ProductoNotFoundException(id));
        productoRepository.deleteById(producto.getId());
    }

    public ProductoDTO updateDescuento(Long id, BigDecimal desc ) throws ProductoNotFoundException {
        Producto producto = productoRepository.findById(id).orElseThrow(() -> new ProductoNotFoundException(id));
        producto.setDescuentoPromocional(desc);
        productoRepository.save(producto);
        return mapper.productoToDTO(producto);
    }

    public UpdateProductoDTO updateProductoStock(Long id, StockUpdateDTO stockUpdateDTO) throws ProductoNotFoundException{
        Producto producto = productoRepository.findById(id).orElseThrow(() -> new ProductoNotFoundException(id));
        producto.setStockActual(producto.getStockActual()+ stockUpdateDTO.getCantidad());
        producto.setPrecio(stockUpdateDTO.getPrecio());
        productoRepository.save(producto);
    // ahora código para mensaje
    rabbitTemplate.convertAndSend(RabbitMQConfig.STOCK_UPDATE_QUEUE, stockUpdateDTO);
    log.info("Mensaje enviado a RabbitMQ: {}", stockUpdateDTO);
        return mapper.productoToUpdate(producto);
    }


}

