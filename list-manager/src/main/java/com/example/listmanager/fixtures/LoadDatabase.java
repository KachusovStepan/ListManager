package com.example.listmanager.fixtures;

import com.example.listmanager.services.IListService;
import com.example.listmanager.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.listmanager.repos.*;
import com.example.listmanager.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    private static Random random = new Random();
    public static <T> T AnyItem(List<T> arr) {
        int index = random.nextInt(arr.size());
        T item = arr.get(index);
        return item;
    }

    public static ItemList GenerateItemList(
            IListService listService, int count, User user,
            List<Category> categories, List<ItemStatus> statuses, List<String> tasks, List<String> listNames) {
        List<Item> items = new ArrayList<>();
        for(int i = 0; i < count; i++) {
//            items.add(listService.saveItem(new Item(i, random.nextInt(10), AnyItem(tasks), AnyItem(statuses))));
            items.add(new Item(i, random.nextInt(10), AnyItem(tasks), AnyItem(statuses)));
        }
        return new ItemList(AnyItem(listNames), user, AnyItem(categories), items);
    }

    @Bean
    CommandLineRunner initDatabase(
            IListService listService,
            IUserService userService
    ) {
        return args -> {
            log.info("Started loading fixtures...");
            userService.saveRole(new Role("ROLE_USER", "General user"));
            userService.saveRole(new Role("ROLE_MANAGER", "General user with acces to some user lists"));
            userService.saveRole(new Role("ROLE_ADMIN", "Can do everything"));

            userService.saveUser(new User("shiny", "shiny@admin.com", "1111"));
            userService.addRoleToUser("shiny", "ROLE_USER");
            userService.addRoleToUser("shiny", "ROLE_MANAGER");
            userService.addRoleToUser("shiny", "ROLE_ADMIN");

            List<Category> categories = Arrays.asList(
                    listService.saveCategory(new Category("ToDo")),
                    listService.saveCategory(new Category("GroceryList")),
                    listService.saveCategory(new Category("Classes"))
            );

            List<ItemStatus> itemStatuses = Arrays.asList(
                    listService.saveItemStatus(new ItemStatus("To Do")),
                    listService.saveItemStatus(new ItemStatus("In Progress")),
                    listService.saveItemStatus(new ItemStatus("Done"))
            );

            List<String> taskNames = Arrays.asList(
                    "Buy Flowers", "Get Shoes", "Call Joe", "Push changes", "Read a book",
                    "Water flowers", "Tie shoes", "Make the bed"
            );

            List<String> listNames = Arrays.asList(
                    "To Do", "Agenda", "Classes", "Team meeting"
            );

            List<User> users = Arrays.asList(
//                    userService.saveUser(new User("Adam", "adam@example.com","1234", itemLists))
                    userService.saveUser(new User("Adam", "adam@example.com","1234"))
            );
            users.forEach(u -> userService.addRoleToUser(u.getUsername(), "ROLE_USER"));

//            ItemList il = GenerateItemList(listService, 10, categories, itemStatuses, taskNames, listNames);
            List<ItemList> itemLists = Arrays.asList(
                    GenerateItemList(listService, 10, users.get(0), categories, itemStatuses, taskNames, listNames),
                    GenerateItemList(listService, 7, users.get(0), categories, itemStatuses, taskNames, listNames),
                    GenerateItemList(listService, 4, users.get(0), categories, itemStatuses, taskNames, listNames),
                    GenerateItemList(listService, 12, users.get(0), categories, itemStatuses, taskNames, listNames),
                    GenerateItemList(listService, 2, users.get(0), categories, itemStatuses, taskNames, listNames)
            );

            users.get(0).getLists().addAll(itemLists);
            itemLists.forEach(il -> listService.saveList(il));
            userService.saveUser(users.get(0));

            log.info("Fixtures loaded");
        };
    }
}