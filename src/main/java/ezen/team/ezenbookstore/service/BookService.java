package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Book;
import ezen.team.ezenbookstore.entity.BookDescription;
import ezen.team.ezenbookstore.repository.BookDescriptionRepository;
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
    private final BookDescriptionRepository bookDescriptionRepository;

    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Book create(Book book) {
        return bookRepository.save(book);
    }

    // 새 책 추가 메서드
    public Book addBook(Book book) {
        // 책 설명이 포함된 경우 처리
        if (book.getBookdescription() != null) {
            BookDescription description = book.getBookdescription();
            bookDescriptionRepository.save(description);
            book.setBookdescription(description); // 저장된 설명을 책에 설정
        }

        // 카테고리와 서브카테고리, 설명이 포함된 상태로 책 저장
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

    public List<Book> findAllByIfkr(Byte ifkr) {
        return bookRepository.findAllByIfkr(ifkr);
    }

    public List<Book> findAllByCategoryId(Long categoryId){
        return bookRepository.findAllByCategoryId(categoryId);
    }

    public List<Book> findAllBySubcategoryId(Long subcategoryId){
        return bookRepository.findAllBySubcategoryId(subcategoryId);
    }

    public Page<Book> findAllByIfkrAndCategoryNameAndSubcategoryName(byte ifkr, String categoryName, String subcategoryName, Pageable pageable) {
        return bookRepository.findAllByIfkrAndCategoryNameAndSubcategoryName(ifkr, categoryName, subcategoryName, pageable);
    }

    public Page<Book> findAllByIfkrAndCategoryName(byte ifkr, String categoryName, Pageable pageable) {
        return bookRepository.findAllByIfkrAndCategoryName(ifkr, categoryName, pageable);
    }

    public Page<Book> findAllByIfkr(byte ifkr, Pageable pageable) {
        return bookRepository.findAllByIfkr(ifkr, pageable);
    }

}
