package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Category;
import ezen.team.ezenbookstore.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category findById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    public Category update(Long id, Category category) {
        Category newCategory = Category.builder()
                .id(id)
                .name(category.getName())
                .build();
        return categoryRepository.save(newCategory);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    public Category findByName(String name) {
        return categoryRepository.findByName(name);
    }

}
