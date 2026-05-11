package com.fruitshop.service;

import com.fruitshop.entity.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();

    Category getById(Long id);
}
