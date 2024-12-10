package ezen.team.ezenbookstore.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ezen.team.ezenbookstore.entity.Book;
import ezen.team.ezenbookstore.entity.Category;
import ezen.team.ezenbookstore.entity.SubCategory;
import ezen.team.ezenbookstore.service.BookService;
import ezen.team.ezenbookstore.service.CategoryService;
import ezen.team.ezenbookstore.service.FileUploadService;
import ezen.team.ezenbookstore.service.SubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/book") //책 관리 컨트롤러
public class AdminBookApiController {

    private final BookService bookService;
    private final FileUploadService fileUploadService;
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;

    @GetMapping("")
    public String bookControl(
            @RequestParam(name = "keyword", defaultValue = "", required = false) String keyword,
            @RequestParam(name = "ifkr", defaultValue = "", required = false) String ifkr,
            @RequestParam(name = "category", defaultValue = "", required = false) String category,
            @RequestParam(name = "subcategory", defaultValue = "", required = false) String subcategory,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            Model model) {

        List<Book> filteredBooks = bookService.findAll(); // 기본적으로 전체 책 조회
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        // 키워드로 필터링
        if (!keyword.isEmpty()) {
            filteredBooks = bookService.findByTitleContaining(keyword);
        }

        // 국내/국외 필터링
        if (!ifkr.isEmpty()) {
            byte ifkrValue = Byte.parseByte(ifkr);
            filteredBooks.retainAll(bookService.findAllByIfkr(ifkrValue));
        }

        // 카테고리 필터링
        if (!category.isEmpty()) {
            Category selectedCategory = categoryService.findById(Long.parseLong(category));
            if (selectedCategory != null) {
                filteredBooks.retainAll(bookService.findAllByCategoryId(selectedCategory.getId()));
            }
        }

        // 서브카테고리 필터링
        if (!subcategory.isEmpty()) {
            SubCategory selectedSubCategory = subCategoryService.findById(Long.parseLong(subcategory));
            if (selectedSubCategory != null) {
                filteredBooks.retainAll(bookService.findAllBySubcategoryId(selectedSubCategory.getId()));
            }
        }

        // 페이지네이션 적용
        int start = Math.min((int) pageable.getOffset(), filteredBooks.size());
        int end = Math.min((start + pageable.getPageSize()), filteredBooks.size());
        List<Book> pagedBooks = filteredBooks.subList(start, end);
        Page<Book> bookPage = new PageImpl<>(pagedBooks, pageable, filteredBooks.size());


        List<String> imagePaths = bookPage.getContent().stream()
                .map(book -> fileUploadService.findImageFilePath(book.getId(), "book"))
                .map(path -> path != null ? path : "/images/default.png")
                .collect(Collectors.toList());

        // 필터 값과 페이지 데이터 모델에 추가
        model.addAttribute("bookList", bookPage.getContent());
        model.addAttribute("page", bookPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("ifkr", ifkr);
        model.addAttribute("category", category);
        model.addAttribute("subcategory", subcategory);
        model.addAttribute("imagePaths", imagePaths);
        return "admin/bookControl";
    }

    //id로 책 조회
    @PostMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getBookById(@PathVariable Long id) {
        Book book = bookService.findById(id);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }

        // 이미지 경로 추가
        String imagePath = fileUploadService.findImageFilePath(id, "book");

        // 응답 데이터 생성
        Map<String, Object> response = new HashMap<>();
        response.put("book", book);
        response.put("imagePath", imagePath != null ? imagePath : "/images/default.jpg");

        return ResponseEntity.ok(response);
    }


    // 새 책 추가 메서드
    @PostMapping("/add")
    public ResponseEntity<String> addBook(
            @ModelAttribute Book book,
            @RequestParam(name = "publish_Date", required = false) String publishDate,
            @RequestParam(name = "files", required = false) List<MultipartFile> files) {
        try {
            // publishDate를 수동 변환
            if (publishDate != null && !publishDate.isEmpty()) {
                System.out.println("Received publishDate: " + publishDate); // 디버깅용 로그
                String fullDateTime = publishDate + " 00:00:00";
                Timestamp convertedTimestamp = Timestamp.valueOf(fullDateTime);
                book.setPublishDate(convertedTimestamp); // 수동으로 변환된 값 설정
                System.out.println("Converted publishDate to Timestamp: " + book.getPublishDate());
            }

            // 데이터베이스에 저장
            Book newBook = bookService.addBook(book);

            // 파일 업로드 처리
            if (files != null && !files.isEmpty()) {
                for (MultipartFile file : files) {
                    fileUploadService.uploadFile(file, newBook.getId().toString(), "book");
                }
            }
            return ResponseEntity.ok("Book added successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Book addition failed: " + e.getMessage());
        }
    }



    //책 수정 메서드
    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<Book> updateBook(
            @RequestPart("book") String bookJson,
            @RequestParam(name = "publish_Date", required = false) String publishDate,
            @RequestPart(name = "files", required = false) List<MultipartFile> files) {

        // JSON 문자열을 Book 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        Book book;
        try {
            book = objectMapper.readValue(bookJson, Book.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(null);
        }

        // publishDate를 수동 변환
        if (publishDate != null && !publishDate.isEmpty()) {
            String fullDateTime = publishDate + " 00:00:00";
            Timestamp convertedTimestamp = Timestamp.valueOf(fullDateTime);
            book.setPublishDate(convertedTimestamp); // 수동으로 변환된 값 설정
        }

        // Book 업데이트
        Book updatedBook = bookService.update(book);

        // 파일 업로드 처리
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                fileUploadService.uploadFile(file, updatedBook.getId().toString(), "book");
            }
        }
        return ResponseEntity.ok(updatedBook);
    }


    // 책 삭제 메서드
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.ok("Book deleted successfully");
    }

    @PostMapping("/discount")
    public ResponseEntity<String> updateDiscountForBooks(@RequestBody Map<String, Object> payload) {
        // 서비스에 데이터 전달
        bookService.updateDiscountForBooks(payload);
        return ResponseEntity.ok("Discounts updated successfully");
    }

    @PostMapping("/delete/batch")
    public ResponseEntity<String> deleteSelectedBooks(@RequestBody Map<String, Object> payload) {
        List<?> bookIdsRaw = (List<?>) payload.get("bookIds");

        if (bookIdsRaw == null || bookIdsRaw.isEmpty()) {
            return ResponseEntity.badRequest().body("No valid book IDs provided.");
        }

        try {
            bookService.deleteBooksByIdsRaw(bookIdsRaw); // 서비스 호출
            return ResponseEntity.ok("Books deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }




}
