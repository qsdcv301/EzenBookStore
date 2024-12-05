package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Category;
import ezen.team.ezenbookstore.entity.SubCategory;
import ezen.team.ezenbookstore.repository.CategoryRepository;
import ezen.team.ezenbookstore.repository.SubCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryServiceInterface {

    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
//
//    public Category create(Category category) {
//        return categoryRepository.save(category);
//    }

//    public Category update(Long id, Category category) {
//        Category newCategory = Category.builder()
//                .id(id)
//                .name(category.getName())
//                .build();
//        return categoryRepository.save(newCategory);
//    }

    @Override
    public Category create(Category category) {
        if (categoryRepository.findByName(category.getName()) != null) {
            throw new IllegalArgumentException("이미 존재하는 카테고리 이름입니다.");
        }

        return categoryRepository.save(category);
    }

    @Override
    public Category update(Long id, Category category) {
        Category existingCategory = categoryRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("카테고리가 존재하지 않습니다. ID: " + id));

        existingCategory.setName(category.getName());
        return categoryRepository.save(existingCategory);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Category findByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public void deleteCategoryWithSubcategories(Long categoryId) {
        // 해당 카테고리에 연결된 서브카테고리 삭제
        List<SubCategory> subCategories = subCategoryRepository.findByCategoryId(categoryId);
        for (SubCategory subCategory : subCategories) {
            subCategoryRepository.deleteById(subCategory.getId());
        }

        // 카테고리 삭제
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public List<Category> searchByName(String keyword) {
        return categoryRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<Category> findCategoriesByName(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

}
