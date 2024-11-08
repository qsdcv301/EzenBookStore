package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Book;
import ezen.team.ezenbookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
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
                .imagePath(book.getImagePath())
                .ifkr(book.getIfkr())
                .price(book.getPrice())
                .categoryId(book.getCategoryId())
                .subcategoryId(book.getSubcategoryId())
                .count(book.getCount())
                .build();
        return bookRepository.save(newBook);
    }

    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    public List<Book> findByTitleContaining(String title) {
        return bookRepository.findByTitleContaining(title);
    }

    public List<Book> findByAuthorContaining(String author) {
        return bookRepository.findByAuthorContaining(author);
    }

    public List<Book> findByPublisherContaining(String publisher) {
        return bookRepository.findByPublisherContaining(publisher);
    }

    public List<Book> findByIsbnContaining(Integer isbn) {
        return bookRepository.findByIsbnContaining(isbn);
    }

//    public List<Book> findByTitleContainingAndCategoryIdAndSubcategoryId(String title, Integer categoryId, Integer subcategoryId) {
//        return bookRepository.findByTitleContainingAndCategoryIdAndSubcategoryId(title, categoryId, subcategoryId);
//    }
//
//    public List<Book> findByAuthorContainingAndCategoryIdAndSubcategoryId(String author, Integer categoryId, Integer subcategoryId) {
//        return bookRepository.findByAuthorContainingAndCategoryIdAndSubcategoryId(author, categoryId, subcategoryId);
//    }
//
//    public List<Book> findByPublisherContainingAndCategoryIdAndSubcategoryId(String publisher, Integer categoryId, Integer subcategoryId) {
//        return bookRepository.findByPublisherContainingAndCategoryIdAndSubcategoryId(publisher, categoryId, subcategoryId);
//    }
//
//    public List<Book> findByIsbnContainingAndCategoryIdAndSubcategoryId(String isbn, Integer categoryId, Integer subcategoryId) {
//        return bookRepository.findByIsbnContainingAndCategoryIdAndSubcategoryId(isbn, categoryId, subcategoryId);
//    }

    public List<Book> findByTitleContainingAndCategoryIdAndSubcategoryIdAndIfkr(String title, Integer categoryId, Integer subcategoryId, Byte ifkr) {
        return bookRepository.findByTitleContainingAndCategoryIdAndSubcategoryIdAndIfkr(title, categoryId, subcategoryId, ifkr);
    }

    public List<Book> findByAuthorContainingAndCategoryIdAndSubcategoryIdAndIfkr(String author, Integer categoryId, Integer subcategoryId, Byte ifkr) {
        return bookRepository.findByAuthorContainingAndCategoryIdAndSubcategoryIdAndIfkr(author, categoryId, subcategoryId, ifkr);
    }

    public List<Book> findByPublisherContainingAndCategoryIdAndSubcategoryIdAndIfkr(String publisher, Integer categoryId, Integer subcategoryId, Byte ifkr) {
        return bookRepository.findByPublisherContainingAndCategoryIdAndSubcategoryIdAndIfkr(publisher, categoryId, subcategoryId, ifkr);
    }

    public List<Book> findByIsbnContainingAndCategoryIdAndSubcategoryIdAndIfkr(String isbn, Integer categoryId, Integer subcategoryId, Byte ifkr) {
        return bookRepository.findByIsbnContainingAndCategoryIdAndSubcategoryIdAndIfkr(isbn, categoryId, subcategoryId, ifkr);
    }

}
