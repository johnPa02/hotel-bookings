package com.tdtu.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tdtu.backend.repository.CategoryRepository;
import com.tdtu.backend.model.Category;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }
    public Category getById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }
    public Category save(Category category) {
        return categoryRepository.save(category);
    }
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
