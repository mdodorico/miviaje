package net.miViaje.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.miViaje.model.Categoria;

public interface CategoriasRepository extends JpaRepository<Categoria, Integer> {
	
}
