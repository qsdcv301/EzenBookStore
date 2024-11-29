package ezen.team.ezenbookstore.service.facade;

import ezen.team.ezenbookstore.entity.*;
import ezen.team.ezenbookstore.service.*;
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
    public Book updateBookCount(Long bookId) {
        Book book = bookService.findById(bookId);
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
        return bookService.update(updatedBook);
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

}