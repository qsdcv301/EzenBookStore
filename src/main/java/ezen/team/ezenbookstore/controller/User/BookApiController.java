package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.entity.Book;
import ezen.team.ezenbookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/book")
public class BookApiController {

    private final BookService bookService;

    @GetMapping
    public String book(Model model) {
//        List<Book> bookList = bookService.findAll();
//        model.addAttribute("bookList", bookList);
        return "book";
    }

}
