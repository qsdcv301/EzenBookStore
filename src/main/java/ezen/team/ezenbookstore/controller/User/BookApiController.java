package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.entity.*;
import ezen.team.ezenbookstore.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/book")
public class BookApiController {

    private final BookService bookService;
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;
    private final FileUploadService fileUploadService;
    private final ReviewService reviewService;

    @GetMapping("/search")
    public String bookSearch(@RequestParam(name = "keyword", defaultValue = "", required = false) String keyword,
                             @RequestParam(name = "val", defaultValue = "", required = false) String val,
                             @RequestParam(name = "page", defaultValue = "0", required = false) int page,
                             @RequestParam(name = "sort", defaultValue = "count", required = false) String sort,
                             @RequestParam(name = "direction", defaultValue = "asc", required = false) String direction,
                             @RequestParam(name = "ifkr", defaultValue = "", required = false) String ifkr,
                             @RequestParam(name = "category", defaultValue = "", required = false) String category,
                             @RequestParam(name = "subcategory", defaultValue = "", required = false) String subcategory,
                             Model model) {

        // 페이지 설정
        int size = 5;
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        List<Book> filteredBooks = new ArrayList<>();

        // 아무런 검색 조건이 없는 경우 모든 책을 조회
        if (keyword.isEmpty() && val.isEmpty() && category.isEmpty() && subcategory.isEmpty() && ifkr.isEmpty()) {
            List<Book> allBooks = bookService.findAll();  // 전체 데이터를 한 번 가져옴
            filteredBooks.addAll(allBooks);
        } else {
            boolean isFirstFilter = true;

            // 국내/국외 필터링 (교집합)
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

            // 카테고리 필터링 (교집합)
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

            // 서브카테고리 필터링 (교집합)
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

            // 키워드와 값을 쉼표로 나누기
            String[] keywordGroups = keyword.split("\\],\\[");
            String[] valueGroups = val != null ? val.split(",") : new String[0];

            // 그룹 길이 맞추기
            int maxGroupSize = Math.max(keywordGroups.length, valueGroups.length);

            for (int groupIndex = 0; groupIndex < maxGroupSize; groupIndex++) {
                String[] keywords = groupIndex < keywordGroups.length
                        ? keywordGroups[groupIndex].replaceAll("[\\[\\]]", "").split(",")
                        : new String[0];
                String searchValue = groupIndex < valueGroups.length
                        ? valueGroups[groupIndex].trim()
                        : "";

                // 현재 그룹 검색 결과를 저장할 임시 리스트
                List<Book> groupBooks = new ArrayList<>();

                // 각 키워드로 검색 수행
                for (String currentKeyword : keywords) {
                    List<Book> books = switch (currentKeyword.trim()) {
                        case "title" -> bookService.findByTitleContaining(searchValue);
                        case "author" -> bookService.findByAuthorContaining(searchValue);
                        case "publisher" -> bookService.findByPublisherContaining(searchValue);
                        case "isbn" -> bookService.findByIsbnContaining(searchValue);
                        default -> List.of();  // 잘못된 키워드의 경우 빈 리스트 반환
                    };

                    // 그룹별 검색 조건에 따라 합집합 추가
                    if (groupBooks.isEmpty()) {
                        groupBooks.addAll(books);  // 처음에는 모두 추가
                    } else {
                        groupBooks.addAll(books); // 조건에 맞게 합집합 수행
                    }
                }

                // 이전 그룹 결과와 교집합 수행
                if (isFirstFilter) {
                    filteredBooks.addAll(groupBooks);  // 첫 번째 그룹 결과를 초기값으로 사용
                    isFirstFilter = false;
                } else {
                    filteredBooks.retainAll(groupBooks);  // 이후 그룹별 결과를 재검색
                }
            }

            // 아무런 필터링 결과가 없는 경우 모든 책을 조회
            if (filteredBooks.isEmpty() && !keyword.isEmpty()) {
                filteredBooks = new ArrayList<>(); // 빈 리스트로 초기화
            }
        }

        // 필터링된 리스트를 정렬
        Comparator<Book> comparator = switch (sort) {
            case "title" -> Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER);
            case "author" -> Comparator.comparing(Book::getAuthor, String.CASE_INSENSITIVE_ORDER);
            case "publisher" -> Comparator.comparing(Book::getPublisher, String.CASE_INSENSITIVE_ORDER);
            case "price" -> Comparator.comparingDouble(Book::getPrice);
            default -> Comparator.comparing(Book::getId); // 기본적으로 id로 정렬
        };

        if (sortDirection == Sort.Direction.DESC) {
            comparator = comparator.reversed();
        }

        filteredBooks.sort(comparator);

        // 검색된 결과에 해당하는 카테고리 및 서브카테고리 목록 생성
        Map<Category, Set<SubCategory>> domesticCategoryMap = new HashMap<>();
        Map<Category, Set<SubCategory>> foreignCategoryMap = new HashMap<>();

        filteredBooks.stream()
                .filter(book -> book.getIfkr() == 0)  // 국내 책 필터링
                .forEach(book -> {
                    Category categoryObj = book.getCategory();
                    SubCategory subCategoryObj = book.getSubcategory();
                    domesticCategoryMap.computeIfAbsent(categoryObj, k -> new HashSet<>()).add(subCategoryObj);
                });

        filteredBooks.stream()
                .filter(book -> book.getIfkr() == 1)  // 국외 책 필터링
                .forEach(book -> {
                    Category categoryObj = book.getCategory();
                    SubCategory subCategoryObj = book.getSubcategory();
                    foreignCategoryMap.computeIfAbsent(categoryObj, k -> new HashSet<>()).add(subCategoryObj);
                });

        // 모델에 추가
        model.addAttribute("domesticCategoryMap", domesticCategoryMap);
        model.addAttribute("foreignCategoryMap", foreignCategoryMap);

        // 페이징 적용
        int start = Math.min((int) pageable.getOffset(), filteredBooks.size());
        int end = Math.min((start + pageable.getPageSize()), filteredBooks.size());
        List<Book> pagedBooks = filteredBooks.subList(start, end);
        Page<Book> bookPage = new PageImpl<>(pagedBooks, pageable, filteredBooks.size());
        List<String> ImageList = new ArrayList<>();
        for (Book book : bookPage.getContent()) {
            String imagePath = fileUploadService.findImageFilePath(book.getId(), "book");
            if (imagePath != null) {
                ImageList.add(imagePath);
            } else {
                ImageList.add("");
            }
        }
        model.addAttribute("imageList", ImageList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("val", val);
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("bookList", bookPage.getContent());
        model.addAttribute("page", bookPage);
        model.addAttribute("ifkr", ifkr);
        model.addAttribute("category", category);
        model.addAttribute("subcategory", subcategory);
        return "bookSearch";
    }

    @GetMapping("/detail")
    public String bookDetail(@RequestParam(name = "bookId") Long bookId, Model model) {
        Book book = bookService.findById(bookId);
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
                .count(book.getCount() + 1)
                .discount(book.getDiscount())
                .bookdescription(book.getBookdescription())
                .review(book.getReview())
                .build();
        bookService.update(newBook);
        // 이미지 파일 경로 찾기
        String imagePath = fileUploadService.findImageFilePath(bookId, "book");
        List<String> reviewImagePathList = new ArrayList<>();
        List<Review> reviewList = reviewService.findAllByBookId(bookId);
        for (Review review : reviewList) {
            String reviewImagePath = fileUploadService.findImageFilePath(review.getId(), "review");
            if (imagePath != null) {
                reviewImagePathList.add(reviewImagePath);
            } else {
                reviewImagePathList.add("");
            }
        }
        if (imagePath != null) {
            model.addAttribute("imagePath", imagePath);
        } else {
            model.addAttribute("imagePath", "");
        }
        model.addAttribute("reviewImagePathList", reviewImagePathList);
        model.addAttribute("book", book);
        return "bookDetail";
    }

}
