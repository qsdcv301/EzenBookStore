package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Category;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

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

    @Transactional
    List<Category> filterCategories(String keyword);

    @Transactional
    List<Category> paginateCategories(List<Category> categoryList, int page, int size);

    @Transactional
    int calculateTotalPages(long totalCategories, int size);

    @Transactional
    void addAttributesToModel(Model model, List<Category> categoryList, int totalPages, int currentPage,
                              long totalCount, String keyword, int pageSize);
}
