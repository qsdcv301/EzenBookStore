package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Book;
import ezen.team.ezenbookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
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
                .review(book.getReview())
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

    public List<Book> findByIsbnContaining(String isbn) {
        return bookRepository.findByIsbnContaining(isbn);
    }

    public Page<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }


    public List<Book> findAll() {
        return bookRepository.findAll();
    }

}
