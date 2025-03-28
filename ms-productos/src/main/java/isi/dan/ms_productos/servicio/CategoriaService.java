package isi.dan.ms_productos.servicio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isi.dan.ms_productos.conf.RabbitMQConfig;
import isi.dan.ms_productos.dao.CategoriaRepository;
import isi.dan.ms_productos.dto.*;
import isi.dan.ms_productos.exception.CategoriaNotFoundException;
import isi.dan.ms_productos.modelo.Categoria;

import java.util.List;


import isi.dan.ms_productos.utils.Mapper;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    Logger log = LoggerFactory.getLogger(CategoriaService.class);
    @Autowired
    private Mapper mapper;

    @RabbitListener(queues = RabbitMQConfig.STOCK_UPDATE_QUEUE)
    public void handleStockUpdate(Message msg) {
        log.info("Recibido {}", msg);
        // buscar el producto
        // actualizar el stock
        // verificar el punto de pedido y generar un pedido
    }
   
    public CategoriaDTO newCategoria(CategoriaDTO categoriaDTO){
        return mapper.categoriaToDTO(categoriaRepository.save(mapper.dtoToCategoria(categoriaDTO)));
    }
  

    public UpdateCategoriaDTO updateCategoria(UpdateCategoriaDTO updateCategoriaDTO, String nombre) throws CategoriaNotFoundException {
        Categoria categoria = categoriaRepository.findByNombre(nombre).orElseThrow(()-> new CategoriaNotFoundException(nombre));
        return mapper.categoriaToUpdate(categoriaRepository.save(mapper.updateToCategoria(updateCategoriaDTO, categoria)));
    }

    //Entiendo que no es necesario al excepción porque findAll a lo sumo devuelve una lista vacia. 
    // Si hay un error a la hora de realizar la búsqueda se encarga spring?
    public List<CategoriaDTO> getAllCategorias() {
        return mapper.listaToDTO(categoriaRepository.findAll());
    }

    
    public CategoriaDTO getCategoriaByName(String name) throws CategoriaNotFoundException{
        return mapper.categoriaToDTO(categoriaRepository.findByNombre(name).orElseThrow(() -> new CategoriaNotFoundException(name)));
    }
 

    public void deleteCategoria(String name) {
        categoriaRepository.deleteByNombre(name);
    }
}

