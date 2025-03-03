package isi.dan.msclientes.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import isi.dan.msclientes.model.Obra;

@Repository
public interface ObraRepository extends JpaRepository<Obra, Integer> {

    List<Obra> findByPresupuestoGreaterThanEqual(BigDecimal price);

    List<Obra> findByClienteId(Integer id);

    // Find Obras by Cliente ID and Estado (estado field of EstadoDeObra)
    List<Obra> findByClienteIdAndEstado_Estado(Integer clienteId, String estado);
}
