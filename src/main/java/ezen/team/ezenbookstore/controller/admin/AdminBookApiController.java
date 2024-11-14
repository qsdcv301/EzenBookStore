package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.entity.Book;
import ezen.team.ezenbookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping("/admin/book") //책 관리 컨트롤러
public class AdminBookApiController {

    private final BookService bookService;

    public AdminBookApiController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("")
    public String bookControl(Model model,
                              @RequestParam(defaultValue = "0") int page,  // 페이지 번호 (기본값 0)
                              @RequestParam(defaultValue = "10") int size) { // 페이지 크기 (기본값 10)
        Pageable pageable = PageRequest.of(page, size);  // Pageable 생성
        Page<Book> bookList = bookService.findAll(pageable);  // bookService에 Pageable 전달

        model.addAttribute("bookList", bookList);

        return "/admin/bookControl";
    }

    // ID로 책 조회
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.findById(id);
        return book != null ? ResponseEntity.ok(book) : ResponseEntity.notFound().build();
    }

    // 새 책 추가 메서드
    @PostMapping("/add")
    public ResponseEntity<String> addBook(@RequestBody Book book) {
        bookService.addBook(book);
        return ResponseEntity.ok("Book added successfully");
    }

    @PutMapping("/update")
    @ResponseBody
    public ResponseEntity<Book> updateBook(@RequestBody Book book) {
        Book updatedBook = bookService.update(book.getId(), book);
        return ResponseEntity.ok(updatedBook);
    }

    // 책 삭제 메서드
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.ok("Book deleted successfully");
    }


    // 책 상세 조회 << 모달이나 접힘 방식이면 필요 없을 예정
//    @PostMapping("/detail")
//    public ResponseEntity<Book> getBookDetail(@RequestParam Long bookId) {
//        // 책 상세 조회 로직
//        Book book = new Book(); // 예시 데이터
//        return ResponseEntity.ok(book);
//    }


}
