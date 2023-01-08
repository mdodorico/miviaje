package net.miViaje.service.db;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import net.miViaje.model.Categoria;

public interface ICategoriasService {

	List<Categoria> buscarTodas();
	Categoria buscarPorId(Integer id);
	void guardar(Categoria categoria);
	void eliminar(Integer id);
	Page<Categoria> buscarTodas(Pageable page);
}