package net.miViaje.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.miViaje.model.Alquiler;
import net.miViaje.model.Perfil;
import net.miViaje.model.Usuario;
import net.miViaje.service.db.IAlquileresService;
import net.miViaje.service.db.ICategoriasService;
import net.miViaje.service.db.IUsuariosService;
import net.miViaje.util.Utileria;

@Controller
public class HomeController {
	
	@Autowired
	private ICategoriasService serviceCategorias;
	
    @Autowired
	private IAlquileresService serviceAlquileres;
    
    @Autowired
   	private IUsuariosService serviceUsuarios;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
  
	@GetMapping("/")
	public String mostrarHome() {
		return "home";
	}
	
	@GetMapping("/index")
	public String mostrarIndex(Authentication authentication, HttpSession session) {		
		
		String username = authentication.getName();	
		System.out.println("nombre del usuario: " + username);
		
		for(GrantedAuthority rol: authentication.getAuthorities()) {
			System.out.println("ROL: " + rol.getAuthority());
		}
		
		return "redirect:/";
	}
	
	@GetMapping("/signup")
	public String registrarse(Usuario usuario) {
		return "formRegistro";
	}
	
	@PostMapping("/signup")
	public String guardarRegistro(Usuario usuario, BindingResult result, RedirectAttributes attributes, Model model, @RequestParam("archivoImagen") MultipartFile multiPart) {
		
		if(result.hasErrors()) {
			for(ObjectError error : result.getAllErrors()) {
				System.out.println("Ocurrió un error " + error.getDefaultMessage());
			}
			return "usuarios/formUsuarios";
		}
		
		String pwdPlano = usuario.getPassword();
		String pwdEncriptado = passwordEncoder.encode(pwdPlano); 

		usuario.setPassword(pwdEncriptado);	
		usuario.setEstatus(1); 
		
		if(!multiPart.isEmpty()) {
			String ruta = "c:/viajes/imagenes/";
			String nombreImagen = Utileria.guardarArchivo(multiPart, ruta);
			if(nombreImagen != null) {
				usuario.setImagen(nombreImagen);
			}
		} 
		
		Perfil perfil = new Perfil();
		perfil.setId(2);
		usuario.setPerfil(perfil);
		
		serviceUsuarios.guardar(usuario);
		attributes.addFlashAttribute("msg", "Has sido registrado. ¡Ahora puedes ingresar al sistema!");
		
		return "redirect:/login";
	}
	
	@GetMapping("/search")
	public String buscar(@ModelAttribute("search") Alquiler alquiler, Model model) {
		System.out.println("Buscando por: " + alquiler);
		
		ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("descripcion", ExampleMatcher.GenericPropertyMatchers.contains());
		Example<Alquiler> example = Example.of(alquiler, matcher);
		List<Alquiler> lista = serviceAlquileres.buscarByExample(example);
		model.addAttribute("alquileres", lista);
		
		return "home";
	}
	
	@GetMapping("/construccion")
	public String sitioEnConstruccion(Model model) {
		return "construccion";
	}
	
	@GetMapping("/faqs")
	public String preguntasFrecuentes(Model model) {
		return "faqs";
	}
	
	@GetMapping("/login")
	public String mostrarLogin() {
		return "formLogin";
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(request, null, null);
		return "redirect:/";
	}
	
    @GetMapping("/bcrypt/{texto}")
    @ResponseBody
   	public String encriptar(@PathVariable("texto") String texto) {    	
   		return texto + " Encriptado en Bcrypt: " + passwordEncoder.encode(texto);
   	}
	
    @ModelAttribute
	public void setGenericos(Model model) {
		Alquiler alquilerSearch = new Alquiler();
		alquilerSearch.reset();
		model.addAttribute("alquileres", serviceAlquileres.buscarTodos());
		model.addAttribute("categorias", serviceCategorias.buscarTodas());
		model.addAttribute("search", alquilerSearch);
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
	    binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}
}
