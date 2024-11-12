package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.entity.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/book") //책 관리 컨트롤러
public class AdminBookApiController {

    // 책 등록
    @PostMapping("/add")
    public String addBook(@RequestBody Book book) {
        // 책 등록 로직
        return "redirect:/admin/book";
    }

    // 책 수정
    @PostMapping("/update")
    public String updateBook(@RequestBody Book book) {
        // 엔티티 Book을 그대로 사용하여 책 수정 로직 수행
        return "redirect:/admin/book";
    }

    // 책 삭제
    @PostMapping("/delete")
    public String deleteBook(@RequestParam Long bookId) {
        // 책 삭제 로직 수행
        return "redirect:/admin/book";
    }

    // 책 목록 조회
    @PostMapping("/list")
    public ResponseEntity<List<Book>> getBookList() {
        // 책 목록 조회 로직
        List<Book> books = new ArrayList<>(); // 예시 데이터
        return ResponseEntity.ok(books);
    }

    // 책 상세 조회 << 모달이나 접힘 방식이면 필요 없을 예정
//    @PostMapping("/detail")
//    public ResponseEntity<Book> getBookDetail(@RequestParam Long bookId) {
//        // 책 상세 조회 로직
//        Book book = new Book(); // 예시 데이터
//        return ResponseEntity.ok(book);
//    }


}
