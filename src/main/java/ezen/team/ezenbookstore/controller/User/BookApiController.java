package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.entity.Book;
import ezen.team.ezenbookstore.entity.Review;
import ezen.team.ezenbookstore.entity.User;
import ezen.team.ezenbookstore.service.BookService;
import ezen.team.ezenbookstore.service.ReviewService;
import ezen.team.ezenbookstore.service.UserService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public String book(Model model) {
//        List<Book> bookList = bookService.findAll();
//        model.addAttribute("bookList", bookList);
        return "book";
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
