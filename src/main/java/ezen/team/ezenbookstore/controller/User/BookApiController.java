package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.entity.Book;
import ezen.team.ezenbookstore.entity.User;
import ezen.team.ezenbookstore.service.BookService;
import ezen.team.ezenbookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/book")
public class BookApiController {

    private final BookService bookService;
    private final UserService userService;

    @GetMapping("/search")
    public String book(@RequestParam(name = "keyword", defaultValue = "none", required = false) String keyword,
                       @RequestParam(name = "val", required = false) String val,
                       @RequestParam(name = "page", defaultValue = "0", required = false) int page,
                       @RequestParam(name = "sort", defaultValue = "count", required = false) String sort,
                       @RequestParam(name = "direction", defaultValue = "asc", required = false) String direction,
                       @RequestParam(name = "research", required = false) String research,
                       Model model) {

        String userEmail = userService.getUserEmail();
        User user = userService.findByEmail(userEmail);

        // 페이지 설정
        int size = 5; // 가져올 항목 개수
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        // 키워드와 값을 쉼표로 나누기
        String[] keywords = keyword.split(",");
        String[] values = val != null ? val.split(",") : new String[0];

        // 검색 결과 저장 리스트
        Page<Book> bookPage = null;

        // 각 조건을 조합하여 검색 수행
        for (int i = 0; i < keywords.length; i++) {
            String currentKeyword = keywords[i];
            String currentVal = i < values.length ? values[i] : "";

            switch (currentKeyword) {
                case "title":
                    bookPage = bookService.findByTitleContaining(currentVal, pageable);
                    break;
                case "author":
                    bookPage = bookService.findByAuthorContaining(currentVal, pageable);
                    break;
                case "isbn":
                    bookPage = bookService.findByIsbnContaining(currentVal, pageable);
                    break;
                default:
                    bookPage = bookService.findAll(pageable);
                    break;
            }

            // 결과 내 재검색을 위해 각 단계의 검색 결과에서만 검색을 수행하도록 설정
            if (bookPage != null && bookPage.hasContent()) {
                List<Book> bookList = bookPage.getContent();
                pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort)); // 페이지 설정 유지
            }
        }

        List<Book> bookList = bookPage != null ? bookPage.getContent() : List.of();

        model.addAttribute("user", user);
        model.addAttribute("keyword", keyword);
        model.addAttribute("val", val);
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("bookList", bookList);
        model.addAttribute("page", bookPage);
        return "bookSearch";
    }


    @GetMapping("/detail")
    public String bookDetail(@RequestParam(name = "bookId") Long bookId, Model model) {
        String userEmail = userService.getUserEmail();
        User user = userService.findByEmail(userEmail);
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
        bookService.update(bookId, newBook);
        model.addAttribute("user", user);
        model.addAttribute("book", book);
        return "bookDetail";
    }

}
