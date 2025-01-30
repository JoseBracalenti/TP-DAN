package isi.dan.ms_productos.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import isi.dan.ms_productos.modelo.Categoria;


public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    public Categoria findByName(String name);
    public void deleteByName (String name);

}

