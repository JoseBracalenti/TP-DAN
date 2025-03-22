package isi.dan.msclientes.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import isi.dan.msclientes.model.EstadoDeObra;

@Repository
public interface EstadoDeObraRepository extends JpaRepository<EstadoDeObra, Integer> {
    Optional<EstadoDeObra> findByEstado(String estado);
}