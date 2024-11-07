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
        Book newBook = findById(id);
        newBook.Builder(newBook.getId(), book.getTitle(), book.getAuthor(),
                book.getPublisher(), book.getPublishDate(), book.getIsbn(),
                book.getStock(), book.getImagePath(), book.getIfkr(), book.getPrice(),
                book.getCategoryId(), book.getSubcategoryId(), book.getCount());
        return bookRepository.save(newBook);
    }

    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}
