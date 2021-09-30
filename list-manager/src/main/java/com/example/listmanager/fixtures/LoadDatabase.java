package com.example.listmanager.fixtures;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.listmanager.repos.*;
import com.example.listmanager.model.*;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(
            CategoryRepository categoryRepository,
            ItemListRepository itemListRepository,
            ItemRepository itemRepository,
            ItemStatusRepository itemStatusRepository,
            StatusRepository statusRepository,
            UserRepository userRepository
    ) {
        return args -> {
            log.info("Preloading fixtures...");
            Status status = statusRepository.save(new Status("Unknown", "Not authorized"));
            log.info("Preloading " + status);
            User user = userRepository.save(new User("Adam", status, "unknown@unknown.com"));
            log.info("Preloading " + user);
            // Creating lists
            Category category = categoryRepository.save(new Category("ToDo"));
            log.info("Preloading " + category);
            ItemList list = new ItemList("ToDo", category);
            Item item = itemRepository.save(new Item(1, "Call Joe"));
            log.info("Preloading " + item);
            List<Item> items = new ArrayList<>();
            items.add(item);
            list.setItems(items);
            itemListRepository.save(list);
            log.info("Preloading " + list);

            List<ItemList> lists = new ArrayList<>();
            lists.add(list);

            user.setLists(lists);
            log.info("Fixtures loaded");
        };
    }
}