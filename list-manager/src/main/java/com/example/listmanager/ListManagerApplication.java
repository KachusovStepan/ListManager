package com.example.listmanager;

import com.example.listmanager.model.Role;
import com.example.listmanager.model.User;
import com.example.listmanager.services.UserService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.HTML;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@SpringBootApplication
@RestController
public class ListManagerApplication {

	private static List<String> team = new ArrayList<String>();

	public static void main(String[] args) {
		SpringApplication.run(ListManagerApplication.class, args);
//		Configuration conf = new Configuration().configure().addAnnotatedClass(User.class);
//		ServiceRegistry sr = new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
//		SessionFactory sf = conf.buildSessionFactory(sr);
//		Session session = sf.openSession();
//
//		Transaction ts = session.beginTransaction();
//		team = (List<String>)session.createCriteria(User.class).list().stream().map(x -> ((User)x).getUser_name()).collect(Collectors.toList());
//		ts.commit();
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		System.out.println(name);

		return "Hello, " + name + "!" + String.join(",", team);
	}

	@GetMapping("/set-cookie")
	public ResponseEntity<?> setCookie(HttpServletResponse response) {
		Cookie cookie = new Cookie("name", "default-user");
		cookie.setPath("/");
		cookie.setMaxAge(100000);
		response.addCookie(cookie);
		response.setContentType("text/plain");
		return ResponseEntity.ok().build();
	}

	@GetMapping("/hello-cookie")
	public String helloCookie(@CookieValue(value = "name", defaultValue = "aboba") String name) {
		String name_ = "default-user";
		if (name.contentEquals(name_))
			return "OK";
		return "not OK";
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@GetMapping("/login")
	public String getLoggingPage() throws IOException {
		return new String(Files.readAllBytes(Paths.get("H:\\IMKN\\java\\project_list_manager\\ListManager\\list-manager\\src\\main\\resources\\static\\login.html")));

	}

	@Bean
	CommandLineRunner run(UserService userService) {
		return args -> {
			userService.saveRole(new Role(null, "ROLE_USER"));
			userService.saveRole(new Role(null, "ROLE_MANAGER"));
			userService.saveRole(new Role(null, "ROLE_ADMIN"));

			userService.saveUser(new User(null, "JC", "jc@admin.com", "1111", new ArrayList<>()));
			userService.addRoleToUser("JC", "ROLE_USER");
			userService.addRoleToUser("JC", "ROLE_MANAGER");
			userService.addRoleToUser("JC", "ROLE_ADMIN");
		};
	}
}

