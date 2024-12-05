package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.entity.*;
import ezen.team.ezenbookstore.service.BookService;
import ezen.team.ezenbookstore.service.facade.BookFacadeService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    private final BookFacadeService bookFacadeService;
    private final BookService bookService;

    @GetMapping
    public String book(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                       @RequestParam(name = "sort", defaultValue = "id", required = false) String sort,
                       @RequestParam(name = "direction", defaultValue = "asc", required = false) String direction,
                       @RequestParam(name = "ifkr", defaultValue = "", required = false) String ifkr,
                       @RequestParam(name = "category", defaultValue = "", required = false) String category,
                       @RequestParam(name = "subcategory", defaultValue = "", required = false) String subcategory,
                       Model model) {
        Page<Book> bookList = bookFacadeService.getBooks(sort, direction, page, ifkr, category, subcategory);
        List<String> imageList = bookFacadeService.getImageList(bookList.getContent());
        model.addAttribute("imageList", imageList);
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("bookList", bookList.getContent());
        model.addAttribute("page", bookList);
        model.addAttribute("category", category);
        model.addAttribute("subcategory", subcategory);
        return "bookMain";
    }

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
        Page<Book> bookPage = bookFacadeService.searchBooks(keyword, val, ifkr, category, subcategory, sort, direction, page);
        List<String> imageList = bookFacadeService.getImageList(bookPage.getContent());
        model.addAttribute("imageList", imageList);
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
    public String bookDetail(@RequestParam(name = "bookId") Long bookId, HttpServletResponse response, Model model) {
        Book book = bookService.updateBookCount(bookId);
        String imagePath = bookFacadeService.getBookImagePath(bookId);
        List<String> reviewImagePathList = bookFacadeService.getReviewImagePathList(bookId);
        bookFacadeService.recentBookCookie(bookId, response);
        model.addAttribute("imagePath", imagePath);
        model.addAttribute("reviewImagePathList", reviewImagePathList);
        model.addAttribute("book", book);
        return "bookDetail";
    }

}