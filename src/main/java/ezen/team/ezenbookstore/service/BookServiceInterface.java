package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Book;
import ezen.team.ezenbookstore.entity.BookDescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface BookServiceInterface {

    Book findById(Long id);

    Book create(Book book);

    // 새 책 추가 메서드
    Book addBook(Book book, BookDescription bookDescription);

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

    void updateDiscountForBooks(Map<String, Object> payload);

    void updateDiscountByBookId(Long bookId, byte discount);

    void deleteBooksByIdsRaw(List<?> bookIdsRaw);

    void deleteBooksByIds(List<Long> bookIds);

    Page<Book> adminFilteredBooks(String keyword, String ifkr, String category, String subcategory, int page);
}
