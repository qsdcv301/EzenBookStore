package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.entity.Book;
import ezen.team.ezenbookstore.entity.Review;
import ezen.team.ezenbookstore.entity.User;
import ezen.team.ezenbookstore.service.BookService;
import ezen.team.ezenbookstore.service.ReviewService;
import ezen.team.ezenbookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/book")
public class BookApiController {

    private final BookService bookService;
    private final UserService userService;
    private final ReviewService reviewService;

    @GetMapping("/search")
    public String book(@RequestParam(name = "keyword", defaultValue = "none", required = false) String keyword,
                       @RequestParam(name = "val", required = false) String val,
                       @RequestParam(name = "page", defaultValue = "0", required = false) int page,
                       @RequestParam(name = "sort", defaultValue = "count", required = false) String sort,
                       @RequestParam(name = "direction", defaultValue = "asc", required = false) String direction,
                       Model model) {
        String userEmail = userService.getUserEmail();
        User user = userService.findByEmail(userEmail);
        Page<Book> bookPage = null;
        int size = 5; // 가져올 항목 개수
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        bookPage = switch (keyword) {
            case "title" -> bookService.findByTitleContaining(val, pageable);
            case "author" -> bookService.findByAuthorContaining(val, pageable);
            case "isbn" -> bookService.findByIsbnContaining(Integer.parseInt(val), pageable);
            default -> bookService.findAll(pageable);
        };
        List<Book> bookList = bookPage.getContent();
        model.addAttribute("user", user);
        model.addAttribute("keyword", keyword);
        model.addAttribute("val",val);
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
        model.addAttribute("user", user);
        model.addAttribute("book", book);
        return "bookDetail";
    }

}
