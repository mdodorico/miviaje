package net.miViaje.service.db;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.miViaje.model.Categoria;
import net.miViaje.repository.CategoriasRepository;


@Service
@Primary
public class CategoriasServiceImp implements ICategoriasService{
	
	@Autowired
	private CategoriasRepository repoCategorias;
	
	@Override
	public List<Categoria> buscarTodas() {
		return repoCategorias.findAll();
	}

	@Override
	public Categoria buscarPorId(Integer id) {
		Optional<Categoria> optional = repoCategorias.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	@Override
	public void guardar(Categoria categoria) {
		repoCategorias.save(categoria);
	}

	@Override
	public void eliminar(Integer id) {
		repoCategorias.deleteById(id);
	}

	@Override
	public Page<Categoria> buscarTodas(Pageable page) {
		return repoCategorias.findAll(page);
	}
}


