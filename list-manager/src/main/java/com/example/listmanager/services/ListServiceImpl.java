package com.example.listmanager.services;

import com.example.listmanager.model.*;
import com.example.listmanager.repos.CategoryRepository;
import com.example.listmanager.repos.ItemListRepository;
import com.example.listmanager.repos.ItemRepository;
import com.example.listmanager.repos.ItemStatusRepository;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


@Service
@Transactional
public class ListServiceImpl implements IListService {
    private final ItemListRepository itemListRepository;
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ItemStatusRepository itemStatusRepository;
    private final IUserService userService;

    public ListServiceImpl(
            ItemListRepository itemListRepository,
            ItemRepository itemRepository,
            CategoryRepository categoryRepository,
            ItemStatusRepository itemStatusRepository,
            IUserService userService
    ) {
        this.itemListRepository = itemListRepository;
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.itemStatusRepository = itemStatusRepository;
        this.userService = userService;
    }

    @Override
    public ItemList saveList(ItemList list) {
        return itemListRepository.save(list);
    }

    @Override
    public void addListToUser(User user, ItemList list) {
        if (user.getLists().stream().noneMatch(l -> list.getId() == l.getId())) {
            user.getLists().add(list);
        }
    }

    @Override
    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public ItemStatus saveItemStatus(ItemStatus itemStatus) {
        return itemStatusRepository.save(itemStatus);
    }

    @Override
    public ItemList getListByUserAndListName(String username, String listName) {
        User user = userService.getUser(username);
        if (user == null) {
            return null;
        }
        Optional<ItemList> optionalItemList = user.getLists().stream().filter(l -> l.getName().equals(listName)).findFirst();
        return optionalItemList.get();
    }

    @Override
    public Category getCategory(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public ItemStatus getItemStatus(String name) {
        return itemStatusRepository.findByName(name);
    }

    @Override
    public List<Category> getListCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<ItemStatus> getItemStatuses() {
        return itemStatusRepository.findAll();
    }

    @Override
    public List<ItemList> getItemLists() {
        return itemListRepository.findAll();
    }

    @Override
    public boolean deleteList(Long id) {
        Optional<ItemList> optionalItemList = itemListRepository.findById(id);
        if (optionalItemList.isEmpty()) {
            return false;
        }
        User owner = optionalItemList.get().getUser();
        owner.getLists().removeIf(l -> l.getId().equals(id));
        itemListRepository.deleteById(id);
        return true;
    }
}
