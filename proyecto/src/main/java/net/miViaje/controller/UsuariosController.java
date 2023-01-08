package net.miViaje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.miViaje.model.Perfil;
import net.miViaje.model.Usuario;
import net.miViaje.service.db.IUsuariosService;
import net.miViaje.util.Utileria;

@Controller
@RequestMapping("/usuarios")
public class UsuariosController {
	
    @Autowired
    private IUsuariosService serviceUsuarios;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping("/indexPaginate")
	public String mostrarIndexPaginado(Model model, Pageable page) {
    	Page<Usuario> lista = serviceUsuarios.buscarTodos(page);
    	model.addAttribute("usuarios", lista);
		return "usuarios/listUsuarios";
	}
    
    @ModelAttribute
	public void setGenericos(Model model) {
		model.addAttribute("perfiles", serviceUsuarios.buscarPerfiles());
	}
    
    @GetMapping("/create")
	public String crear(Usuario usuario, Model model) {
		return "usuarios/formUsuarios";
	}
    
    @PostMapping("/save")
	public String guardar(Usuario usuario, BindingResult result, RedirectAttributes attributes, Model model, @RequestParam("archivoImagen") MultipartFile multiPart) {
		
    	if(result.hasErrors()) {
			for(ObjectError error : result.getAllErrors()) {
				System.out.println("Ocurrió un error " + error.getDefaultMessage());
			}
			return "usuarios/formUsuarios";
		}
    	
    	String pwdPlano = usuario.getPassword();
		String pwdEncriptado = passwordEncoder.encode(pwdPlano); 

		usuario.setPassword(pwdEncriptado);	
		
		if(!multiPart.isEmpty()) {
			String ruta = "c:/viajes/imagenes/";
			String nombreImagen = Utileria.guardarArchivo(multiPart, ruta);
			if(nombreImagen != null) {
				usuario.setImagen(nombreImagen);
			}
		} 
		
		Perfil perfil = (Perfil) usuario.getPerfil();
		perfil.setId(perfil.getId());
		usuario.setPerfil(perfil);
		
		serviceUsuarios.guardar(usuario);
		attributes.addFlashAttribute("msg", "Registro guardado");
		System.out.println("Usuario nuevo: " + usuario);

		return "redirect:/usuarios/indexPaginate";
	}
    
    @GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") int idUsuario, RedirectAttributes attributes) {
    	serviceUsuarios.eliminar(idUsuario);	
		attributes.addFlashAttribute("msg", "El usuario fue eliminado.");
		return "redirect:/usuarios/indexPaginate";
	}
    
    @GetMapping("/edit/{id}")
	public String editar(@PathVariable("id") int idUser, Model model) {
		Usuario user = serviceUsuarios.buscarPorId(idUser);
		model.addAttribute("usuario", user);
		return "usuarios/formUsuarios";
	}
    
    @GetMapping("/unlock/{id}")
	public String activar(@PathVariable("id") int idUsuario, RedirectAttributes attributes) {		
    	serviceUsuarios.activar(idUsuario);
		attributes.addFlashAttribute("msg", "El usuario fue activado y ahora tiene acceso al sistema.");		
		return "redirect:/usuarios/indexPaginate";
	}
    
	@GetMapping("/lock/{id}")
	public String bloquear(@PathVariable("id") int idUsuario, RedirectAttributes attributes) {		
		serviceUsuarios.bloquear(idUsuario);
		attributes.addFlashAttribute("msg", "El usuario fue bloqueado y no tendra acceso al sistema.");		
		return "redirect:/usuarios/indexPaginate";
	}
}
