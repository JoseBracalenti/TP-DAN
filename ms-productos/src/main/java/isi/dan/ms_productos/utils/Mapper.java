package isi.dan.ms_productos;

import isi.dan.ms_productos.*;


public class Mapper{

    public CategoriaDTO categoriaToDTO(Categoria categoria){
        CategoriaDTO categoriaDTo = new CategoriaDTO();
        categoriaDTo.setNombre(categoria.getNombre());
        categoriaDTo.setId(categoria.getId());
    return  categoriaDTo;
    }

    public ProductoDTO productoToDTO(Producto producto){
        ProductoDTO respuestaDTO = new ProductoDTO();      
        respuestaDTO.setNombre(savedProducto.getNombre());
        respuestaDTO.setDescripcion(savedProducto.getDescripcion());
        respuestaDTO.setStockMinimo(savedProducto.getStockMinimo());
        respuestaDTO.setPrecioInicial(savedProducto.getPrecio());
        respuestaDTO.setDescuentoPromocional(savedProducto.getDescuentoPromocional());
        respuestaDTO.setNombreCategoria((savedProducto.getCategoria()).getNombre());
        return respuestaDTO;
    }

}