package net.miViaje.service.db;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.miViaje.model.Perfil;
import net.miViaje.model.Usuario;
import net.miViaje.repository.PerfilesRepository;
import net.miViaje.repository.UsuariosRepository;


@Service
public class UsuariosServiceImp implements IUsuariosService{

	@Autowired
	private UsuariosRepository usuariosRepo;
	
	@Autowired
	private PerfilesRepository perfilesRepo;
	
	@Override
	public void guardar(Usuario usuario) {
		usuariosRepo.save(usuario);
	}

	@Override
	public void eliminar(Integer idUsuario) {
		usuariosRepo.deleteById(idUsuario);
	}

	@Override
	public List<Usuario> buscarTodos() {
		return usuariosRepo.findAll();
	}

	@Override
	public Usuario buscarPorId(Integer idUsuario) {
		Optional<Usuario> optional = usuariosRepo.findById(idUsuario);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public Usuario buscarPorUsername(String username) {
		return usuariosRepo.findByUsername(username);
	}

	@Transactional
	@Override
	public int bloquear(int idUsuario) {
		int rows = usuariosRepo.lock(idUsuario);
		return rows;
	}

	@Transactional
	@Override
	public int activar(int idUsuario) {
		int rows = usuariosRepo.unlock(idUsuario);
		return rows;
	}

	@Override
	public Page<Usuario> buscarTodos(Pageable page) {
		return usuariosRepo.findAll(page);
	}

	@Override
	public List<Perfil> buscarPerfiles() {
		return perfilesRepo.findAll();
	}
}
