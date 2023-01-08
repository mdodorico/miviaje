package net.miViaje.service.db;

import java.util.List;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import net.miViaje.model.Alquiler;

public interface IAlquileresService {
	
	List<Alquiler> buscarTodos();
	Alquiler buscarPorId(Integer id);
	void guardar(Alquiler alquiler);
	void eliminar(Integer id);
	List<Alquiler> buscarByExample(Example<Alquiler> example);
	Page<Alquiler> buscarTodos(Pageable page);
}
