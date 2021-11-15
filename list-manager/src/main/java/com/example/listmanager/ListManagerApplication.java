package com.example.listmanager;

import com.example.listmanager.model.*;
import com.example.listmanager.model.dto.ItemListItemVerboseToGetDto;
import com.example.listmanager.model.dto.ItemListToGetDto;
import com.example.listmanager.model.dto.UserToGetDto;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@SpringBootApplication
@RestController
//@EnableSwagger2
public class ListManagerApplication {

	private static List<String> team = new ArrayList<String>();

	public static void main(String[] args) {
		SpringApplication.run(ListManagerApplication.class, args);
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
		// FIXME: now using json body, html form probably send form body
		return new String(Files.readAllBytes(Paths.get("H:\\IMKN\\java\\project_list_manager\\ListManager\\list-manager\\src\\main\\resources\\static\\login.html")));

	}

	static class ItemListConverter implements Converter<List<ItemList>, List<Object>> {
		public List<Object> convert(MappingContext<List<ItemList>, List<Object>> context) {
			List<Long> ilIds = new ArrayList<Long>();
			for (ItemList il : context.getSource()) {
				ilIds.add(il.getId());
			}
			return context.getMappingEngine().map(context.create(ilIds, context.getDestinationType()));
		}
	}

	static class RoleConverter implements Converter<List<Role>, List<Object>> {
		public List<Object> convert(MappingContext<List<Role>, List<Object>> context) {
			List<Long> roleIds = new ArrayList<Long>();
			for (Role r : context.getSource()) {
				roleIds.add(r.getId());
			}
			return context.getMappingEngine().map(context.create(roleIds, context.getDestinationType()));
		}
	}

	static class CategoryConverter implements Converter<Category, Object> {
		public Object convert(MappingContext<Category, Object> context) {
			return context.getMappingEngine().map(context.create(context.getSource().getId(), context.getDestinationType()));
		}
	}

	static class UserToIdConverter implements Converter<User, Object> {
		public Object convert(MappingContext<User, Object> context) {
			return context.getMappingEngine().map(context.create(context.getSource().getId(), context.getDestinationType()));
		}
	}

	static class ItemToIdConverter implements Converter<List<Item>, List<Object>> {
		public List<Object> convert(MappingContext<List<Item>, List<Object>> context) {
			List<Long> roleIds = new ArrayList<Long>();
			for (Item r : context.getSource()) {
				roleIds.add(r.getId());
			}
			return context.getMappingEngine().map(context.create(roleIds, context.getDestinationType()));
		}
	}

	@Bean
	public ModelMapper modelMapper() {
		var modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<User, UserToGetDto>() {
			@Override
			protected void configure() {
				using(new ItemListConverter()).map(source.getLists()).setLists(null);
				using(new RoleConverter()).map(source.getRoles()).setRoles(null);
			}
		});

		modelMapper.addMappings(new PropertyMap<ItemList, ItemListToGetDto>() {
			@Override
			protected void configure() {
				using(new CategoryConverter()).map(source.getCategory()).setCategory(null);
				using(new UserToIdConverter()).map(source.getUser()).setUser(null);
				using(new ItemToIdConverter()).map(source.getItems()).setItems(null);
			}
		});

		modelMapper.addMappings(new PropertyMap<ItemList, ItemListItemVerboseToGetDto>() {
			@Override
			protected void configure() {
				using(new CategoryConverter()).map(source.getCategory()).setCategory(null);
				using(new UserToIdConverter()).map(source.getUser()).setUser(null);
			}
		});


		return modelMapper;
	}
}

