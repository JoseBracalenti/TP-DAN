package isi.dan.ms_productos.utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import isi.dan.ms_productos.dao.*;
import isi.dan.ms_productos.modelo.*;
import isi.dan.ms_productos.dto.*;
import isi.dan.ms_productos.exception.CategoriaNotFoundException;

@Component
public class Mapper{

    @Autowired
    private CategoriaRepository categoriarepository;


    public CategoriaDTO categoriaToDTO(Categoria categoria){
        CategoriaDTO categoriaDTo = new CategoriaDTO();
        categoriaDTo.setNombre(categoria.getNombre());
        categoriaDTo.setId(categoria.getId());
    return  categoriaDTo;
    }
    public Categoria dtoToCategoria(CategoriaDTO categoriaDTO){
        Categoria categoria = new Categoria();
        categoria.setNombre(categoriaDTO.getNombre());
        categoria.setId(categoriaDTO.getId());
        return categoria;
    }

    public ProductoDTO productoToDTO(Producto producto){
        ProductoDTO respuestaDTO = new ProductoDTO();      
        respuestaDTO.setNombre(producto.getNombre());
        respuestaDTO.setDescripcion(producto.getDescripcion());
        respuestaDTO.setStockMinimo(producto.getStockMinimo());
        respuestaDTO.setPrecio(producto.getPrecio());
        respuestaDTO.setDescuentoPromocional(producto.getDescuentoPromocional());
        respuestaDTO.setNombreCategoria((producto.getCategoria()).getNombre());
        return respuestaDTO;
    }

    public Producto dtoToProducto (ProductoDTO productoDTO)throws CategoriaNotFoundException{

        Producto producto = new Producto();

        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setStockMinimo(productoDTO.getStockMinimo());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setDescuentoPromocional(productoDTO.getDescuentoPromocional());
        producto.setCategoria((categoriarepository.findByNombre((productoDTO.getNombreCategoria()))).orElseThrow(() -> new CategoriaNotFoundException(productoDTO.getNombreCategoria())));
        return producto;
    }

        public List<CategoriaDTO> listaToDTO(List<Categoria> categorias) {
        return categorias.stream()
                .map(this::categoriaToDTO)
                .collect(Collectors.toList());
    }
    public List<ProductoDTO> listaPToDTO(List<Producto> productos) {
        return productos.stream()
                .map(this::productoToDTO)
                .collect(Collectors.toList());
    }
    
    public UpdateProductoDTO productoToUpdate(Producto producto){
        UpdateProductoDTO respuestaDTO = new UpdateProductoDTO();      
        respuestaDTO.setNombre(producto.getNombre());
        respuestaDTO.setDescripcion(producto.getDescripcion());
        respuestaDTO.setStockMinimo(producto.getStockMinimo());
        respuestaDTO.setStockActual(producto.getStockActual());
        respuestaDTO.setPrecio(producto.getPrecio());
        respuestaDTO.setDescuentoPromocional(producto.getDescuentoPromocional());
        respuestaDTO.setNombreCategoria((producto.getCategoria()).getNombre());
        return respuestaDTO;
    }

    public Producto updateToProducto(UpdateProductoDTO updateProductoDTO,Producto producto)throws CategoriaNotFoundException{

        if (updateProductoDTO.getNombre() != null) {
            producto.setNombre(updateProductoDTO.getNombre()) ;
        }
        if (updateProductoDTO.getDescripcion() != null){
            producto.setDescripcion(updateProductoDTO.getDescripcion());
        }
        if (updateProductoDTO.getStockMinimo() >= 0) { 
            producto.setStockMinimo(updateProductoDTO.getStockMinimo());
        }
        if (updateProductoDTO.getStockActual() >= 0) { 
            producto.setStockActual(updateProductoDTO.getStockActual());
        }
        if (updateProductoDTO.getPrecio() != null && updateProductoDTO.getPrecio().compareTo(BigDecimal.ZERO) > 0) {
            producto.setPrecio(updateProductoDTO.getPrecio());
        }
        if (updateProductoDTO.getDescuentoPromocional() != null && updateProductoDTO.getDescuentoPromocional().compareTo(BigDecimal.ZERO) >= 0) {
            producto.setDescuentoPromocional(updateProductoDTO.getDescuentoPromocional());
        }
        if (updateProductoDTO.getNombreCategoria() != null) {
            producto.setCategoria((categoriarepository.findByNombre((updateProductoDTO.getNombreCategoria()))).orElseThrow(() -> new CategoriaNotFoundException(updateProductoDTO.getNombreCategoria())));
        }
        return producto;
    }
    
    public Categoria updateToCategoria( UpdateCategoriaDTO updateCategoriaDTO, Categoria categoria){
        categoria.setNombre(updateCategoriaDTO.getNombre());
        return categoria;
    }
    public UpdateCategoriaDTO categoriaToUpdate(Categoria categoria){
        UpdateCategoriaDTO updateCategoriaDTO = new UpdateCategoriaDTO();
        updateCategoriaDTO.setNombre(categoria.getNombre());
        return updateCategoriaDTO;
    }



}

