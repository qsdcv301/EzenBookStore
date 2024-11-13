package ezen.team.ezenbookstore.repository;

import ezen.team.ezenbookstore.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findByTitleContaining(String title, Pageable pageable);

    Page<Book> findByAuthorContaining(String author, Pageable pageable);

    Page<Book> findByPublisherContaining(String publisher, Pageable pageable);

    Page<Book> findByIsbnContaining(String isbn, Pageable pageable);

    // 주석 처리된 메서드들 페이징 추가
    // Page<Book> findByTitleContainingAndCategoryIdAndSubcategoryId(String title, Integer categoryId, Integer subcategoryId, Pageable pageable);

    // Page<Book> findByAuthorContainingAndCategoryIdAndSubcategoryId(String author, Integer categoryId, Integer subcategoryId, Pageable pageable);

    // Page<Book> findByPublisherContainingAndCategoryIdAndSubcategoryId(String publisher, Integer categoryId, Integer subcategoryId, Pageable pageable);

    // Page<Book> findByIsbnContainingAndCategoryIdAndSubcategoryId(String isbn, Integer categoryId, Integer subcategoryId, Pageable pageable);

    Page<Book> findByTitleContainingAndCategoryIdAndSubcategoryIdAndIfkr(String title, Integer categoryId, Integer subcategoryId, Byte ifkr, Pageable pageable);

    Page<Book> findByAuthorContainingAndCategoryIdAndSubcategoryIdAndIfkr(String author, Integer categoryId, Integer subcategoryId, Byte ifkr, Pageable pageable);

    Page<Book> findByPublisherContainingAndCategoryIdAndSubcategoryIdAndIfkr(String publisher, Integer categoryId, Integer subcategoryId, Byte ifkr, Pageable pageable);

    Page<Book> findByIsbnContainingAndCategoryIdAndSubcategoryIdAndIfkr(String isbn, Integer categoryId, Integer subcategoryId, Byte ifkr, Pageable pageable);

}
