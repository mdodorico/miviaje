package net.miViaje.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import net.miViaje.model.Alquiler;

public interface AlquileresRepository extends JpaRepository<Alquiler, Integer> {
}