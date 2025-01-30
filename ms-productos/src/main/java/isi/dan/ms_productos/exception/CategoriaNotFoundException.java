package isi.dan.ms_productos.exception;


public class CategoriaNotFoundException extends Exception {
    
    public CategoriaNotFoundException(String name){
        super("Categoria "+name+" no encontrada");
    }

}
