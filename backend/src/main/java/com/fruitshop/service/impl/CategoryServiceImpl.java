package com.fruitshop.service.impl;

import com.fruitshop.entity.Category;
import com.fruitshop.mapper.CategoryMapper;
import com.fruitshop.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryMapper.findAll();
    }

    @Override
    public Category getById(Long id) {
        return categoryMapper.findById(id);
    }
}
