package isi.dan.ms_productos.utils;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

import isi.dan.ms_productos.dao.CategoriaRepository;
import isi.dan.ms_productos.modelo.*;
import isi.dan.ms_productos.dto.*;
import isi.dan.ms_productos.exception.CategoriaNotFoundException;


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
        producto.setCategoria((categoriarepository.findByName((productoDTO.getNombreCategoria()))).orElseThrow(() -> new CategoriaNotFoundException(productoDTO.getNombreCategoria())));
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
    


}