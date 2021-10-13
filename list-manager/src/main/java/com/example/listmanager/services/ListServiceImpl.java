package com.example.listmanager.services;

import com.example.listmanager.model.*;
import com.example.listmanager.repos.CategoryRepository;
import com.example.listmanager.repos.ItemListRepository;
import com.example.listmanager.repos.ItemRepository;
import com.example.listmanager.repos.ItemStatusRepository;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


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
        return itemListRepository.save(list);
    }

//    @Override
//    public ItemList trySaveItemList(ItemList newList) {
//        ItemList resultList;
//        boolean listCreated = false;
//        if (newList.getId() != null) {
//            resultList = itemListRepository.getById(newList.getId());
//            if (resultList == null) {
//                return null;
//            }
//        } else {
//            listCreated = true;
//            resultList = new ItemList();
//        }
//        if (resultList.getItems() == null) {
//            resultList.setItems(new ArrayList<>());
//        }
//        resultList.setName(newList.getName());
//        Category category = categoryRepository.findByName(newList.getCategory().getName());
//        resultList.setCategory(category);
//        List<Item> newItems = newList.getItems();
//        for (Item newItem : newItems) {
//            Item resultItem;
//            boolean itemCreated = false;
//            if (newItem.getId() != null) {
//                Optional<Item> optionalItem = resultList.getItems().stream()
//                        .filter(i -> i.getId() == newItem.getId()).findFirst();
//                if (optionalItem.isEmpty()) {
//                    return null;
//                }
//                resultItem = optionalItem.get();
//            } else {
//                itemCreated = true;
//                resultItem = new Item();
//            }
//            resultItem.setNumber(newItem.getNumber());
//            resultItem.setPriority(newItem.getPriority());
//            resultItem.setDescription(newItem.getDescription());
//            ItemStatus itemStatus = itemStatusRepository.findByName(newItem.getStatus().getName());
//            if (itemStatus == null) {
//                itemStatus = itemStatusRepository.findByName("ToDo");
//            }
//            resultItem.setStatus(itemStatus);
//
//            if (itemCreated) {
//                itemRepository.save(resultItem);
//            }
//            resultList.getItems().add(resultItem);
//        }
//
//        if (listCreated) {
//            itemListRepository.save(resultList);
//        }
//        return resultList;
//    }

    @Override
    public void addListToUser(User user, ItemList list) {
//        User user = userService.getUser(username);
        if (user.getLists().stream().noneMatch(l -> list.getId() == l.getId())) {
            user.getLists().add(list);
        }
    }

//    public boolean tryPrepareItemListCompounds(ItemList list, ItemList resList) {
//        Category category = categoryRepository.findByName(list.getCategory().getName());
//        resList.setCategory(category);
//        List<Item> items = list.getItems();
//        List<Item> resItems = resList.getItems();
//        Stream<Item> resItemsStream = resItems.stream();
//        if(items != null) {
//            for(Item item : items) {
//                Long itemId = item.getId();
//                if (itemRepository.existsById(itemId)) {
//                    if (resItemsStream.anyMatch(i -> i.getId() == itemId)) {
//                        // change
//                    } else {
//                        // error
//                        return false;
//                    }
//                }
//            }
//        }
//    }

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
}
