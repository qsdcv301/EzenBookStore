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
public class SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;

    public SubCategory findById(Long id) {
        return subCategoryRepository.findById(id).orElse(null);
    }

    public List<SubCategory> findAll() {
        return subCategoryRepository.findAll();
    }

    public SubCategory create(SubCategory subCategory) {
        return subCategoryRepository.save(subCategory);
    }

//    public SubCategory update(Long id, SubCategory subCategory) {
//        SubCategory newSubCategory = SubCategory.builder()
//                .id(id)
//                .category(subCategory.getCategory())
//                .name(subCategory.getName())
//                .build();
//        return subCategoryRepository.save(newSubCategory);
//    }

    // 서브카테고리 수정
    public void update(Long subCategoryId, SubCategory subCategory) {
        SubCategory existingSubCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new IllegalArgumentException("서브카테고리가 존재하지 않습니다."));
        existingSubCategory.setName(subCategory.getName());
        subCategoryRepository.save(existingSubCategory);
    }

    public List<SubCategory> getSubCategoriesByCategoryId(Long categoryId) {
        return subCategoryRepository.findByCategoryId(categoryId);
    }

    // 서브카테고리 삭제
    public void delete(Long subCategoryId) {
        subCategoryRepository.deleteById(subCategoryId);
    }

    public SubCategory findByName(String name) {
        return subCategoryRepository.findByName(name);
    }

    // 서브카테고리 추가
    public void addSubCategory(Long categoryId, SubCategory subCategory) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리가 존재하지 않습니다."));
        subCategory.setCategory(category);
        subCategoryRepository.save(subCategory);
    }


}
