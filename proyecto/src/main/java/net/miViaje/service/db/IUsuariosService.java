package net.miViaje.service.db;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import net.miViaje.model.Perfil;
import net.miViaje.model.Usuario;

public interface IUsuariosService {
	
	List<Usuario> buscarTodos();
	List<Perfil> buscarPerfiles();
	Usuario buscarPorId(Integer id);
	Usuario buscarPorUsername(String username);
	void guardar(Usuario usuario);
	void eliminar(Integer id);
	int bloquear(int idUsuario);
	int activar(int idUsuario);
	Page<Usuario> buscarTodos(Pageable page);
}
