package com.example.listmanager.services;


import com.example.listmanager.model.*;

import java.util.List;


public interface IListService {
    ItemList saveList(ItemList list);
    void addListToUser(User user, ItemList list);
    Item saveItem(Item item);
    Category saveCategory(Category category);
    ItemStatus saveItemStatus(ItemStatus itemStatus);
    ItemList getListByUserAndListName(String username, String listName);
    Category getCategory(String name);
    ItemStatus getItemStatus(String name);
    List<Category> getListCategories();
    List<ItemStatus> getItemStatuses();
    List<ItemList> getItemLists();
    boolean deleteList(Long id);
}
