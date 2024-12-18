package com.repuestos.repository;

import com.repuestos.model.Cliente;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmailOrTelefono(String email, String telefono);
}

