package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.SubCategory;

import java.util.List;

public interface SubCategoryServiceInterface {

    SubCategory findById(Long id);

    List<SubCategory> findAll();

    SubCategory create(SubCategory subCategory);

    void update(Long subCategoryId, SubCategory subCategory);

    List<SubCategory> getSubCategoriesByCategoryId(Long categoryId);

    void delete(Long subCategoryId);

    SubCategory findByName(String name);

    void addSubCategory(Long categoryId, SubCategory subCategory);

}
