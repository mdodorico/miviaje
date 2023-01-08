package net.miViaje.service.db;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.miViaje.model.Alquiler;
import net.miViaje.repository.AlquileresRepository;


@Service
@Primary
public class AlquileresServiceImp implements IAlquileresService{

	@Autowired
	private AlquileresRepository repoAlquileres;
	
	@Override
	public List<Alquiler> buscarTodos() {
		return repoAlquileres.findAll();
	}

	@Override
	public Alquiler buscarPorId(Integer id) {
		Optional<Alquiler> optional = repoAlquileres.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public void guardar(Alquiler alquiler) {
		repoAlquileres.save(alquiler);
	}

	@Override
	public void eliminar(Integer id) {
		repoAlquileres.deleteById(id);
	}

	@Override
	public List<Alquiler> buscarByExample(Example<Alquiler> example) {
		return repoAlquileres.findAll(example);
	}

	@Override
	public Page<Alquiler> buscarTodos(Pageable page) {
		return repoAlquileres.findAll(page);
	}
}
