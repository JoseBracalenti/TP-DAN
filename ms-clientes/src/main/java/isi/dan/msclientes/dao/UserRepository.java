package isi.dan.msclientes.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import isi.dan.msclientes.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public Optional<User> findByCorreoElectronico(String correoElectronico);   
}
