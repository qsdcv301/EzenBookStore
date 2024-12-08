package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Book;
import ezen.team.ezenbookstore.entity.BookDescription;
import ezen.team.ezenbookstore.repository.BookDescriptionRepository;
import ezen.team.ezenbookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService implements BookServiceInterface {

    private final BookRepository bookRepository;
    private final BookDescriptionRepository bookDescriptionRepository;

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public Book create(Book book) {
        return bookRepository.save(book);
    }

    // 새 책 추가 메서드
    @Override
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

    @Override
    public Book update(Book book) {
        Book newBook = Book.builder()
                .id(book.getId())
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

    @Override
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<Book> findByTitleContaining(String title) {
        return bookRepository.findByTitleContaining(title);
    }

    @Override
    public List<Book> findByAuthorContaining(String author) {
        return bookRepository.findByAuthorContaining(author);
    }

    @Override
    public List<Book> findByPublisherContaining(String publisher) {
        return bookRepository.findByPublisherContaining(publisher);
    }

    @Override
    public List<Book> findByIsbnContaining(String isbn) {
        return bookRepository.findByIsbnContaining(isbn);
    }

    @Override
    public Page<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> findAllByIfkr(Byte ifkr) {
        return bookRepository.findAllByIfkr(ifkr);
    }

    @Override
    public List<Book> findAllByCategoryId(Long categoryId) {
        return bookRepository.findAllByCategoryId(categoryId);
    }

    @Override
    public List<Book> findAllBySubcategoryId(Long subcategoryId) {
        return bookRepository.findAllBySubcategoryId(subcategoryId);
    }

    @Override
    public Page<Book> findAllByIfkrAndCategoryNameAndSubcategoryName(byte ifkr, String categoryName, String subcategoryName, Pageable pageable) {
        return bookRepository.findAllByIfkrAndCategoryNameAndSubcategoryName(ifkr, categoryName, subcategoryName, pageable);
    }

    @Override
    public Page<Book> findAllByIfkrAndCategoryName(byte ifkr, String categoryName, Pageable pageable) {
        return bookRepository.findAllByIfkrAndCategoryName(ifkr, categoryName, pageable);
    }

    @Override
    public Page<Book> findAllByIfkr(byte ifkr, Pageable pageable) {
        return bookRepository.findAllByIfkr(ifkr, pageable);
    }

    @Override
    public Book findByTitle(String title) {
        return bookRepository.findByTitle(title).orElse(null);
    }

    @Override
    public List<Book> findTop8ByOrderByCountDesc() {
        return bookRepository.findTop8ByOrderByCountDesc();
    }

    @Override
    public List<Book> findTop8ByOrderByPublishDateDesc() {
        return bookRepository.findTop8ByOrderByPublishDateDesc();
    }

    @Override
    public Book updateBookCount(Long bookId) {
        Book book = findById(bookId);
        Book updatedBook = Book.builder()
                .id(book.getId())
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
                .count(book.getCount() + 1)
                .discount(book.getDiscount())
                .bookdescription(book.getBookdescription())
                .review(book.getReview())
                .build();
        return update(updatedBook);
    }

    @Transactional
    public void updateDiscountForBooks(Map<String, Object> payload) {
        // 데이터 추출 및 변환
        List<Integer> bookIds = (List<Integer>) payload.get("bookIds");
        byte discount;

        try {
            discount = Byte.parseByte(payload.get("discount").toString());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid discount value. It must be a valid byte.");
        }

        // 검증
        if (bookIds == null || bookIds.isEmpty()) {
            throw new IllegalArgumentException("No book IDs provided.");
        }
        if (discount < 0) {
            throw new IllegalArgumentException("Discount value must be non-negative.");
        }

        // 각 Book의 할인율 업데이트
        for (Integer bookId : bookIds) {
            updateDiscountByBookId(bookId.longValue(), discount);
        }
    }

    @Transactional
    public void updateDiscountByBookId(Long bookId, byte discount) {
        // 데이터베이스에서 Book을 조회
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + bookId));

        // 할인율 설정
        book.setDiscount(discount);

        // 변경 사항 저장
        bookRepository.save(book);
    }

    @Transactional
    public void deleteBooksByIdsRaw(List<?> bookIdsRaw) {
        // Integer -> Long 변환
        List<Long> bookIds = bookIdsRaw.stream()
                .map(id -> {
                    if (id instanceof Integer) {
                        return ((Integer) id).longValue(); // Integer -> Long 변환
                    } else if (id instanceof Long) {
                        return (Long) id;
                    } else {
                        throw new IllegalArgumentException("Invalid ID type: " + id.getClass());
                    }
                })
                .collect(Collectors.toList());

        // 삭제 로직 수행
        deleteBooksByIds(bookIds);
    }

    @Transactional
    public void deleteBooksByIds(List<Long> bookIds) {
        bookRepository.deleteAllById(bookIds); // Repository 호출
    }
}
