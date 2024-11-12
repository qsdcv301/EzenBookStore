package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Book;
import ezen.team.ezenbookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Page<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Book create(Book book) {
        return bookRepository.save(book);
    }

    public Book update(Long id, Book book) {
        Book newBook = Book.builder()
                .id(id)
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .publishDate(book.getPublishDate())
                .isbn(book.getIsbn())
                .stock(book.getStock())
                .ifkr(book.getIfkr())
                .price(book.getPrice())
                .category(book.getCategory())
                .subcategory(book.getSubcategory())
                .count(book.getCount())
                .discount(book.getDiscount())
                .bookdescription(book.getBookdescription())
                .build();
        return bookRepository.save(newBook);
    }

    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    public Page<Book> findByTitleContaining(String title, Pageable pageable) {
        return bookRepository.findByTitleContaining(title, pageable);
    }

    public Page<Book> findByAuthorContaining(String author, Pageable pageable) {
        return bookRepository.findByAuthorContaining(author, pageable);
    }

    public Page<Book> findByPublisherContaining(String publisher, Pageable pageable) {
        return bookRepository.findByPublisherContaining(publisher, pageable);
    }

    public Page<Book> findByIsbnContaining(Integer isbn, Pageable pageable) {
        return bookRepository.findByIsbnContaining(isbn, pageable);
    }

    public Page<Book> findByTitleContainingAndCategoryIdAndSubcategoryIdAndIfkr(
            String title, Integer categoryId, Integer subcategoryId, Byte ifkr, Pageable pageable) {
        return bookRepository.findByTitleContainingAndCategoryIdAndSubcategoryIdAndIfkr(title, categoryId, subcategoryId, ifkr, pageable);
    }

    public Page<Book> findByAuthorContainingAndCategoryIdAndSubcategoryIdAndIfkr(
            String author, Integer categoryId, Integer subcategoryId, Byte ifkr, Pageable pageable) {
        return bookRepository.findByAuthorContainingAndCategoryIdAndSubcategoryIdAndIfkr(author, categoryId, subcategoryId, ifkr, pageable);
    }

    public Page<Book> findByPublisherContainingAndCategoryIdAndSubcategoryIdAndIfkr(
            String publisher, Integer categoryId, Integer subcategoryId, Byte ifkr, Pageable pageable) {
        return bookRepository.findByPublisherContainingAndCategoryIdAndSubcategoryIdAndIfkr(publisher, categoryId, subcategoryId, ifkr, pageable);
    }

    public Page<Book> findByIsbnContainingAndCategoryIdAndSubcategoryIdAndIfkr(
            String isbn, Integer categoryId, Integer subcategoryId, Byte ifkr, Pageable pageable) {
        return bookRepository.findByIsbnContainingAndCategoryIdAndSubcategoryIdAndIfkr(isbn, categoryId, subcategoryId, ifkr, pageable);
    }

}
