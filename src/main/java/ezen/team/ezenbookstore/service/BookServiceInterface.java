package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookServiceInterface {

    Book findById(Long id);

    Book create(Book book);

    Book addBook(Book book);

    Book update(Book book);

    void delete(Long id);

    List<Book> findByTitleContaining(String title);

    List<Book> findByAuthorContaining(String author);

    List<Book> findByPublisherContaining(String publisher);

    List<Book> findByIsbnContaining(String isbn);

    Page<Book> findAll(Pageable pageable);

    List<Book> findAll();

    List<Book> findAllByIfkr(Byte ifkr);

    List<Book> findAllByCategoryId(Long categoryId);

    List<Book> findAllBySubcategoryId(Long subcategoryId);

    Page<Book> findAllByIfkrAndCategoryNameAndSubcategoryName(byte ifkr, String categoryName, String subcategoryName, Pageable pageable);

    Page<Book> findAllByIfkrAndCategoryName(byte ifkr, String categoryName, Pageable pageable);

    Page<Book> findAllByIfkr(byte ifkr, Pageable pageable);

    Book findByTitle(String title);

    List<Book> findTop8ByOrderByCountDesc();

    List<Book> findTop8ByOrderByPublishDateDesc();

    Book updateBookCount(Long bookId);

}
