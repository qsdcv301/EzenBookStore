package ezen.team.ezenbookstore.repository;

import ezen.team.ezenbookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitleContaining(String title);

    List<Book> findByAuthorContaining(String author);

    List<Book> findByPublisherContaining(String publisher);

    List<Book> findByIsbnContaining(Integer isbn);

//    List<Book> findByTitleContainingAndCategoryIdAndSubcategoryId(String title, Integer categoryId, Integer subcategoryId);
//
//    List<Book> findByAuthorContainingAndCategoryIdAndSubcategoryId(String author, Integer categoryId, Integer subcategoryId);
//
//    List<Book> findByPublisherContainingAndCategoryIdAndSubcategoryId(String publisher, Integer categoryId, Integer subcategoryId);
//
//    List<Book> findByIsbnContainingAndCategoryIdAndSubcategoryId(String isbn, Integer categoryId, Integer subcategoryId);

    List<Book> findByTitleContainingAndCategoryIdAndSubcategoryIdAndIfkr(String title, Integer categoryId, Integer subcategoryId, Byte ifkr);

    List<Book> findByAuthorContainingAndCategoryIdAndSubcategoryIdAndIfkr(String author, Integer categoryId, Integer subcategoryId, Byte ifkr);

    List<Book> findByPublisherContainingAndCategoryIdAndSubcategoryIdAndIfkr(String publisher, Integer categoryId, Integer subcategoryId, Byte ifkr);

    List<Book> findByIsbnContainingAndCategoryIdAndSubcategoryIdAndIfkr(String isbn, Integer categoryId, Integer subcategoryId, Byte ifkr);

}
