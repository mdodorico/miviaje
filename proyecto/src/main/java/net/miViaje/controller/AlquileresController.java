package net.miViaje.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.miViaje.model.Alquiler;
import net.miViaje.service.db.IAlquileresService;
import net.miViaje.service.db.ICategoriasService;
import net.miViaje.util.Utileria;

@Controller
@RequestMapping("/alquileres")
public class AlquileresController {
	
	@Autowired
	private IAlquileresService serviceAlquileres;
	
	@Autowired
	private ICategoriasService serviceCategorias;
	
	@GetMapping(value = "/indexPaginate")
	public String mostrarIndexPaginado(Model model, Pageable page) {
		Page<Alquiler> lista = serviceAlquileres.buscarTodos(page);
		model.addAttribute("alquileres", lista);
		return "alquileres/listAlquileres";
	}
	
	@GetMapping("/create")
	public String crear(Alquiler alquiler, Model model) {
		model.addAttribute("categorias", serviceCategorias.buscarTodas());
		return "alquileres/formAlquiler";
	}
	
	@PostMapping("/save")
	public String guardar(Alquiler alquiler, BindingResult result, RedirectAttributes attributes, Model model, @RequestParam("archivoImagen") MultipartFile multiPart) {
		model.addAttribute("categorias", serviceCategorias.buscarTodas());
		
		if(result.hasErrors()) {
			for(ObjectError error : result.getAllErrors()) {
				System.out.println("Ocurrió un error " + error.getDefaultMessage());
			}
			return "alquileres/formAlquiler";
		}
		
		if(!multiPart.isEmpty()) {
			String ruta = "c:/viajes/imagenes/";
			String nombreImagen = Utileria.guardarArchivo(multiPart, ruta);
			if(nombreImagen != null) {
				alquiler.setImagen(nombreImagen);
			}
		}

		serviceAlquileres.guardar(alquiler);
		attributes.addFlashAttribute("msg", "Registro guardado");
		System.out.println("Alquiler nuevo: " + alquiler);

		return "redirect:/alquileres/indexPaginate";
	}
	
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idAlquiler, RedirectAttributes attributes) {
		System.out.println("Borrando alquiler con id: " + idAlquiler);
		serviceAlquileres.eliminar(idAlquiler);
		attributes.addFlashAttribute("msg", "Alquiler eliminado");
		return "redirect:/alquileres/indexPaginate";
	}
	
	@GetMapping("/edit/{id}")
	public String editar(@PathVariable("id") int idAlquiler, Model model) {
		Alquiler alquiler = serviceAlquileres.buscarPorId(idAlquiler);
		model.addAttribute("alquiler", alquiler);
		return "alquileres/formAlquiler";
	}
	
	@GetMapping("/view/{id}")
	public String verDetalle(@PathVariable("id") int idAlquiler, Model model) {
		Alquiler alquiler = serviceAlquileres.buscarPorId(idAlquiler);
		System.out.println("Alquiler: " + alquiler);
		model.addAttribute("alquiler", alquiler);
		
		return "alquileres/detalle";
	}
	
	@ModelAttribute
	public void setCategorias(Model model) {
		model.addAttribute("categorias", serviceCategorias.buscarTodas());
	}

	@GetMapping("/mensaje")
	public String consultar() {
		return "alquileres/mensaje";
	}
	
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder){
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
}