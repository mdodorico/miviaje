package net.miViaje.security;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class DatabaseWebSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
		.usersByUsernameQuery("select username, password, estatus from Usuarios where username=?")
		.authoritiesByUsernameQuery("select u.username, p.perfil from UsuarioPerfil up " + 
			"inner join Usuarios u on u.id = up.idUsuario " + 
			"inner join Perfiles p on p.id = up.idPerfil " + 
			"where u.username = ?");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests() 

        .antMatchers(
                "/bootstrap/**",                        
                "/images/**",
                "/tinymce/**",
                "/imagenes/**").permitAll()

        .antMatchers("/", 
   			         "/login",
   			         "/signup",
   			         "/bcrypt/**",
   			         "/faqs",
   			         "/construccion").permitAll()
        
        .antMatchers("/alquileres/view/**").hasAnyAuthority("Administrador", "Usuario")
        .antMatchers("/alquileres/indexPaginate").hasAnyAuthority("Administrador")
        .antMatchers("/alquileres/create").hasAnyAuthority("Administrador")
        .antMatchers("/alquileres/edit/**").hasAnyAuthority("Administrador")
        .antMatchers("/alquileres/delete/**").hasAnyAuthority("Administrador")
        
        .antMatchers("/categorias/**").hasAnyAuthority("Administrador")
        .antMatchers("/usuarios/**").hasAnyAuthority("Administrador")
        
        .anyRequest().authenticated()
        .and().formLogin().loginPage("/login").permitAll()        
        .and().logout().permitAll();
    }
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
}