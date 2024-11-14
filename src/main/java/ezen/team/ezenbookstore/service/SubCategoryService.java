package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Category;
import ezen.team.ezenbookstore.entity.SubCategory;
import ezen.team.ezenbookstore.repository.SubCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;

    public SubCategory findById(Long id) {
        return subCategoryRepository.findById(id).orElse(null);
    }

    public List<SubCategory> findAll() {
        return subCategoryRepository.findAll();
    }

    public SubCategory create(SubCategory subCategory) {
        return subCategoryRepository.save(subCategory);
    }

    public SubCategory update(Long id, SubCategory subCategory) {
        SubCategory newSubCategory = SubCategory.builder()
                .id(id)
                .category(subCategory.getCategory())
                .name(subCategory.getName())
                .build();
        return subCategoryRepository.save(newSubCategory);
    }

    public List<SubCategory> getSubCategoriesByCategoryId(Long categoryId) {
        return subCategoryRepository.findByCategoryId(categoryId);
    }

    public void delete(Long id) {
        subCategoryRepository.deleteById(id);
    }

    public SubCategory findByName(String name) {
        return subCategoryRepository.findByName(name);
    }

}
