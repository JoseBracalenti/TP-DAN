package isi.dan.ms_productos.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import isi.dan.ms_productos.modelo.Categoria;


public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    public Optional<Categoria> save(String name);
    public Optional<Categoria> findByNombre(String name);
    public void deleteByNombre (String name);
    
}

