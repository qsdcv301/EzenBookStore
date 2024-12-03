package ezen.team.ezenbookstore.service.facade;

import ezen.team.ezenbookstore.dto.RecentBookCookieDto;
import ezen.team.ezenbookstore.entity.*;
import ezen.team.ezenbookstore.service.*;
import ezen.team.ezenbookstore.util.CookieUtil;
import ezen.team.ezenbookstore.util.ParseUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
@Service
public class BookFacadeService implements BookFacadeServiceInterface {

    private final BookService bookService;
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;
    private final FileUploadService fileUploadService;
    private final ReviewService reviewService;

    @Override
    public Page<Book> getBooks(String sort, String direction, int page, String ifkr, String category, String subcategory) {
        int size = 20;
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        Page<Book> bookList;

        if (category.isEmpty() && subcategory.isEmpty() && ifkr.isEmpty()) {
            bookList = bookService.findAll(pageable);
        } else {
            byte ifkrValue = (byte) (ifkr.equals("1") ? 1 : 0);
            if (!ifkr.isEmpty() && category.isEmpty() && subcategory.isEmpty()) {
                bookList = bookService.findAllByIfkr(ifkrValue, pageable);
            } else if (!ifkr.isEmpty() && !category.isEmpty() && subcategory.isEmpty()) {
                bookList = bookService.findAllByIfkrAndCategoryName(ifkrValue, category, pageable);
            } else {
                bookList = bookService.findAllByIfkrAndCategoryNameAndSubcategoryName(ifkrValue, category, subcategory, pageable);
            }
        }
        return bookList;
    }

    @Override
    public List<String> getImageList(List<Book> books) {
        List<String> imageList = new ArrayList<>();
        for (Book book : books) {
            String imagePath = fileUploadService.findImageFilePath(book.getId(), "book");
            imageList.add(imagePath != null ? imagePath : "");
        }
        return imageList;
    }

    @Override
    public Page<Book> searchBooks(String keyword, String val, String ifkr, String category, String subcategory, String sort, String direction, int page) {
        int size = 5;
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        List<Book> filteredBooks = new ArrayList<>();

        if (keyword.isEmpty() && val.isEmpty() && category.isEmpty() && subcategory.isEmpty() && ifkr.isEmpty()) {
            List<Book> allBooks = bookService.findAll();
            filteredBooks.addAll(allBooks);
        } else {
            boolean isFirstFilter = true;

            if (!ifkr.isEmpty()) {
                byte ifkrValue = (byte) (ifkr.equals("1") ? 1 : 0);
                List<Book> booksByIfkr = bookService.findAllByIfkr(ifkrValue);
                if (isFirstFilter) {
                    filteredBooks.addAll(booksByIfkr);
                    isFirstFilter = false;
                } else {
                    filteredBooks.retainAll(booksByIfkr);
                }
            }

            if (!category.isEmpty()) {
                Category categories = categoryService.findByName(category);
                List<Book> booksByCategory = bookService.findAllByCategoryId(categories.getId());
                if (isFirstFilter) {
                    filteredBooks.addAll(booksByCategory);
                    isFirstFilter = false;
                } else {
                    filteredBooks.retainAll(booksByCategory);
                }
            }

            if (!subcategory.isEmpty()) {
                SubCategory subCategories = subCategoryService.findByName(subcategory);
                List<Book> booksBySubCategory = bookService.findAllBySubcategoryId(subCategories.getId());
                if (isFirstFilter) {
                    filteredBooks.addAll(booksBySubCategory);
                    isFirstFilter = false;
                } else {
                    filteredBooks.retainAll(booksBySubCategory);
                }
            }

            String[] keywordGroups = keyword.split("\\],\\[");
            String[] valueGroups = val != null ? val.split(",") : new String[0];
            int maxGroupSize = Math.max(keywordGroups.length, valueGroups.length);

            for (int groupIndex = 0; groupIndex < maxGroupSize; groupIndex++) {
                String[] keywords = groupIndex < keywordGroups.length
                        ? keywordGroups[groupIndex].replaceAll("[\\[\\]]", "").split(",")
                        : new String[0];
                String searchValue = groupIndex < valueGroups.length
                        ? valueGroups[groupIndex].trim()
                        : "";

                List<Book> groupBooks = new ArrayList<>();

                for (String currentKeyword : keywords) {
                    List<Book> books = switch (currentKeyword.trim()) {
                        case "title" -> bookService.findByTitleContaining(searchValue);
                        case "author" -> bookService.findByAuthorContaining(searchValue);
                        case "publisher" -> bookService.findByPublisherContaining(searchValue);
                        case "isbn" -> bookService.findByIsbnContaining(searchValue);
                        default -> List.of();
                    };

                    if (groupBooks.isEmpty()) {
                        groupBooks.addAll(books);
                    } else {
                        groupBooks.addAll(books);
                    }
                }

                if (isFirstFilter) {
                    filteredBooks.addAll(groupBooks);
                    isFirstFilter = false;
                } else {
                    filteredBooks.retainAll(groupBooks);
                }
            }

            if (filteredBooks.isEmpty() && !keyword.isEmpty()) {
                filteredBooks = new ArrayList<>();
            }
        }

        Comparator<Book> comparator = switch (sort) {
            case "title" -> Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER);
            case "author" -> Comparator.comparing(Book::getAuthor, String.CASE_INSENSITIVE_ORDER);
            case "publisher" -> Comparator.comparing(Book::getPublisher, String.CASE_INSENSITIVE_ORDER);
            case "price" -> Comparator.comparingDouble(Book::getPrice);
            default -> Comparator.comparing(Book::getId);
        };

        if (sortDirection == Sort.Direction.DESC) {
            comparator = comparator.reversed();
        }

        filteredBooks.sort(comparator);
        int start = Math.min((int) pageable.getOffset(), filteredBooks.size());
        int end = Math.min((start + pageable.getPageSize()), filteredBooks.size());
        List<Book> pagedBooks = filteredBooks.subList(start, end);
        return new PageImpl<>(pagedBooks, pageable, filteredBooks.size());
    }

    @Override
    public String getBookImagePath(Long bookId) {
        String imagePath = fileUploadService.findImageFilePath(bookId, "book");
        return imagePath != null ? imagePath : "";
    }

    @Override
    public List<String> getReviewImagePathList(Long bookId) {
        List<Review> reviewList = reviewService.findAllByBookId(bookId);
        List<String> reviewImagePathList = new ArrayList<>();
        for (Review review : reviewList) {
            String reviewImagePath = fileUploadService.findImageFilePath(review.getId(), "review");
            reviewImagePathList.add(reviewImagePath != null ? reviewImagePath : "");
        }
        return reviewImagePathList;
    }

    @Override
    public void recentBookCookie(Long bookId, HttpServletResponse response) {
        // 쿠키 이름 설정
        String cookieName = "book_" + bookId;

        // fileUploadService를 통해 가져온 값을 직렬화
        String bookIdValue = fileUploadService.findImageFilePath(bookId, "book");

        // 데이터를 직렬화 (Base64로 변환하여 안전하게 저장)
        String serializedValue = CookieUtil.serialize(bookIdValue);

        // 쿠키의 만료 기간 설정 2일
        int maxAge = 2 * 24 * 60 * 60;

        // 직렬화된 데이터로 쿠키 추가
        CookieUtil.addCookie(response, cookieName, serializedValue, maxAge);
    }

    @Override
    public List<RecentBookCookieDto> getRecentBookCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        List<RecentBookCookieDto> recentBookCookieDtoList = new ArrayList<>();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // 특정 접두사가 있는 쿠키만 처리
                if (cookie.getName().startsWith("book_")) {
                    try {
                        // bookId 추출
                        Long bookId = ParseUtils.parseLong(cookie.getName().substring(5));
                        Book book = bookService.findById(bookId);

                        // Book 데이터 추출
                        String title = book.getTitle();
                        String author = book.getAuthor();
                        String publisher = book.getPublisher();
                        String imgPath = CookieUtil.deserialize(cookie, String.class);

                        // DTO 생성 후 리스트에 추가
                        RecentBookCookieDto recentBookCookieDto = RecentBookCookieDto.builder()
                                .title(title)
                                .author(author)
                                .publisher(publisher)
                                .imgPath(imgPath)
                                .build();

                        recentBookCookieDtoList.add(recentBookCookieDto);
                    } catch (Exception e) {
                        // 예외가 발생한 쿠키는 무시
                        System.out.println("예외 처리된 쿠키: " + cookie.getName() + " - " + e.getMessage());
                    }
                }
            }
        }

        return recentBookCookieDtoList;
    }

}