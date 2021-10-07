package com.example.listmanager.services;

import com.example.listmanager.model.*;
import com.example.listmanager.repos.CategoryRepository;
import com.example.listmanager.repos.ItemListRepository;
import com.example.listmanager.repos.ItemRepository;
import com.example.listmanager.repos.ItemStatusRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional // to not save entities explicitly
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
        // FIXME: Check if it will save nested entity
        return itemListRepository.save(list);
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
}
