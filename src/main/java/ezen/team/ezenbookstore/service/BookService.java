package ezen.team.ezenbookstore.service;

import ezen.team.ezenbookstore.entity.Book;
import ezen.team.ezenbookstore.entity.BookDescription;
import ezen.team.ezenbookstore.entity.Category;
import ezen.team.ezenbookstore.entity.SubCategory;
import ezen.team.ezenbookstore.repository.BookDescriptionRepository;
import ezen.team.ezenbookstore.repository.BookRepository;
import ezen.team.ezenbookstore.repository.CategoryRepository;
import ezen.team.ezenbookstore.repository.SubCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService implements BookServiceInterface {

    private final BookRepository bookRepository;
    private final BookDescriptionRepository bookDescriptionRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;
    private final BookDescriptionService bookDescriptionService;

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
    public Book addBook(Book book, BookDescription bookDescription) {
        BookDescription newBookDescription = bookDescriptionService.create(bookDescription);
        Book newBook = Book.builder()
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
                .discount(book.getDiscount())
                .bookdescription(newBookDescription)
                .build();
        // 카테고리와 서브카테고리, 설명이 포함된 상태로 책 저장
        return bookRepository.save(newBook);
    }

    @Override
    public Book update(Book book) {
        // Category를 영속 상태로 변환
        Category category = null;
        if (book.getCategory() != null && book.getCategory().getId() != null) {
            category = categoryRepository.findById(book.getCategory().getId())
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 카테고리 ID입니다: " + book.getCategory().getId()));
        }

        // SubCategory를 영속 상태로 변환
        SubCategory subCategory = null;
        if (book.getSubcategory() != null && book.getSubcategory().getId() != null) {
            subCategory = subCategoryRepository.findById(book.getSubcategory().getId())
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 서브 카테고리 ID입니다: " + book.getSubcategory().getId()));
        }

        // BookDescription을 영속 상태로 변환
        BookDescription bookDescription = null;
        if (book.getBookdescription() != null && book.getBookdescription().getId() != null) {
            bookDescription = bookDescriptionRepository.findById(book.getBookdescription().getId())
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 책 설명 ID입니다: " + book.getBookdescription().getId()));

            // 기존 값을 유지하면서 필요한 필드만 업데이트
            bookDescription.setDescription(book.getBookdescription().getDescription());
            bookDescription.setWriter(book.getBookdescription().getWriter());
            bookDescription.setContents(book.getBookdescription().getContents());
            bookDescriptionRepository.save(bookDescription);
        }


        // 기존 엔터티를 업데이트
        Book existingBook = bookRepository.findById(book.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 도서를 찾을 수 없습니다: " + book.getId()));

        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setPublisher(book.getPublisher());
        existingBook.setPublishDate(book.getPublishDate());
        existingBook.setIsbn(book.getIsbn());
        existingBook.setStock(book.getStock());
        existingBook.setIfkr(book.getIfkr());
        existingBook.setPrice(book.getPrice());
        existingBook.setCount(book.getCount());
        existingBook.setDiscount(book.getDiscount());
        existingBook.setCategory(category);
        existingBook.setSubcategory(subCategory);
        existingBook.setBookdescription(bookDescription);

        // 업데이트 후 저장
        return bookRepository.save(existingBook);
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
    @Override
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
    @Override
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
    @Override
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
    @Override
    public void deleteBooksByIds(List<Long> bookIds) {
        bookRepository.deleteAllById(bookIds); // Repository 호출
    }

    @Transactional
    @Override
    public Page<Book> adminFilteredBooks(String keyword, String ifkr, String category, String subcategory, int page) {
        List<Book> filteredBooks = findAll(); // 기본적으로 전체 책 조회
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        // 키워드로 필터링
        if (!keyword.isEmpty()) {
            filteredBooks = findByTitleContaining(keyword);
        }

        // 국내/국외 필터링
        if (!ifkr.isEmpty()) {
            byte ifkrValue = Byte.parseByte(ifkr);
            filteredBooks.retainAll(findAllByIfkr(ifkrValue));
        }

        // 카테고리 필터링
        if (!category.isEmpty()) {
            Category selectedCategory = categoryService.findById(Long.parseLong(category));
            if (selectedCategory != null) {
                filteredBooks.retainAll(findAllByCategoryId(selectedCategory.getId()));
            }
        }

        // 서브카테고리 필터링
        if (!subcategory.isEmpty()) {
            SubCategory selectedSubCategory = subCategoryService.findById(Long.parseLong(subcategory));
            if (selectedSubCategory != null) {
                filteredBooks.retainAll(findAllBySubcategoryId(selectedSubCategory.getId()));
            }
        }

        // 페이지네이션 적용
        int start = Math.min((int) pageable.getOffset(), filteredBooks.size());
        int end = Math.min((start + pageable.getPageSize()), filteredBooks.size());
        List<Book> pagedBooks = filteredBooks.subList(start, end);
        return new PageImpl<>(pagedBooks, pageable, filteredBooks.size());
    }
}
