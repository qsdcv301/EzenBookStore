package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.entity.BookDescription;
import ezen.team.ezenbookstore.service.BookDescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/admin/bookdescription")
public class AdminBookDescriptionApiController {

    private final BookDescriptionService bookDescriptionService;

    public AdminBookDescriptionApiController(BookDescriptionService bookDescriptionService) {
        this.bookDescriptionService = bookDescriptionService;
    }

    //수정모달의 상세정보를 업데이트
    @PutMapping("/update/{id}")
    public ResponseEntity<BookDescription> updateBookDescription(
            @PathVariable Long id,
            @RequestBody BookDescription bookDescription) {
        bookDescription.setId(id); // ID를 설정하여 업데이트
        BookDescription updatedBookDescription = bookDescriptionService.update(bookDescription);
        return ResponseEntity.ok(updatedBookDescription);
    }

    //수정모달 안에있는 상세정보를 상세정보 id로 동적 변환
    @GetMapping("/{id}")
    public ResponseEntity<BookDescription> getBookDescriptionById(@PathVariable Long id) {
        Optional<BookDescription> bookDescription = Optional.ofNullable(bookDescriptionService.findById(id));
        return bookDescription.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}

