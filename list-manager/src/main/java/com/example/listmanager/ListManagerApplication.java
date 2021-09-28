package com.example.listmanager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@SpringBootApplication
@RestController
public class ListManagerApplication {

	private static List<String> team;

	public static void main(String[] args) {
		SpringApplication.run(ListManagerApplication.class, args);
		Configuration conf = new Configuration().configure().addAnnotatedClass(User.class);
		ServiceRegistry sr = new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
		SessionFactory sf = conf.buildSessionFactory(sr);
		Session session = sf.openSession();

		Transaction ts = session.beginTransaction();
		team = (List<String>)session.createCriteria(User.class).list().stream().map(x -> ((User)x).getUser_name()).collect(Collectors.toList());
		ts.commit();
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		System.out.println(name);
		return "Hello, " + name + "!" + String.join(",", team);
	}
}

