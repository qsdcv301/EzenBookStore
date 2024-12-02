package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Category;

import java.util.List;

public interface CategoryServiceInterface {

    Category findById(Long id);

    List<Category> findAll();

    Category create(Category category);

    Category update(Long id, Category category);

    void delete(Long id);

    Category findByName(String name);

    void deleteCategoryWithSubcategories(Long categoryId);

    List<Category> searchByName(String keyword);

    List<Category> findCategoriesByName(String name);

    List<Category> getAllCategories();

}
