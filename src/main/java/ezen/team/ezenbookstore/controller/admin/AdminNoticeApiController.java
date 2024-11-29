package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.entity.Notice;
import ezen.team.ezenbookstore.service.FileUploadService;
import ezen.team.ezenbookstore.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/notice")
public class AdminNoticeApiController {

    private final NoticeService noticeService;
    private final FileUploadService fileUploadService;

    // 공지사항 페이지 이동 및 목록 조회
    @GetMapping
    public String noticeControl(@RequestParam(required = false) String searchType,
                                @RequestParam(required = false) String keyword,
                                @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                Model model) {
        Page<Notice> noticePage;
        List<Long> noticeIds= noticeService.noticeIds();
        // 검색 조건에 따라 데이터를 조회
        if ("title".equals(searchType)) {
            noticePage = noticeService.searchByTitle(keyword, pageable);
        } else if ("content".equals(searchType)) {
            noticePage = noticeService.searchByContent(keyword, pageable);
        }else {
            noticePage = noticeService.findAll(pageable); // 전체 조회
        }
        //이미지가 한개이상일때 확장성을위해 int로 받아옴 만약 2개이상이면 id 값을 바꿔주는 행동이 필요함
//        for(Long imgId : noticeIds){
//            noticeService.findById(imgId,pageable);
//        }
//        int intGetImg = fileUploadService.getImageCount(imgId,"notice");
//        if (intGetImg > 0) {
//            String imagePath =fileUploadService.findImageFilePath(imgId,"notice");
//            if(imagePath != null && !imagePath.isEmpty()) {
//                model.addAttribute("imagePath", imagePath);
//            }
//        }else{
//            model.addAttribute("imagePath", "isNullImg");
//        }
        int totalPages = noticePage.getTotalPages(); // 총 페이지 수
        int currentPage = noticePage.getNumber();
        int pageGroupSize = 10; // 페이지 그룹 크기

        int startPage = Math.max(0, (currentPage / pageGroupSize) * pageGroupSize); // 시작 페이지 번호
        int endPage = Math.min(startPage + pageGroupSize, totalPages); // 끝 페이지 번호
//        System.out.println("currentPage: " + currentPage);
//        System.out.println("startPage: " + startPage);
//        System.out.println("endPage: " + endPage);
//        System.out.println("totalPages: " + totalPages);

        // 페이징 결과 및 검색 조건 추가
        model.addAttribute("noticePage", noticePage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchType", searchType);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "admin/noticeControl"; // View 경로
    }

    // 공지사항 생성 - AJAX 요청 처리
    @PostMapping("/add")
    public ResponseEntity< Map<String, String>> addNotice(@RequestParam("title") String title,
                                            @RequestParam("content") String content,
                                            @RequestPart(value="imageAdd",required=false)MultipartFile image) {
        Map<String, String> response = new HashMap<>();
        try{
            Notice newNotice = Notice.builder()
                            .title(title).content(content).build();
            Notice createNotice = noticeService.create(newNotice);
            if(image != null && image.getSize() > 0) {
                fileUploadService.uploadFile(image,createNotice.getId().toString(),"notice");
            }
            response.put("success", "true");
            return ResponseEntity.ok(response); // 성공 시 200 OK와 함께 반환
        } catch (Exception e) {
            response.put("success", "false");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 공지사항 수정 - AJAX 요청 처리
    @PostMapping("/update")
    public ResponseEntity<String> updateNotice(@RequestBody Notice notice) {
        try {
            noticeService.update(notice);
            return ResponseEntity.ok("공지사항이 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("공지사항 수정 실패");
        }
    }

    //공지사항 삭제
    @PostMapping("/delete")
    public ResponseEntity<String> deleteNotices(@RequestBody List<Long> ids) {
        try {
            for (Long id : ids) {
                noticeService.delete(id);
            }
            return ResponseEntity.ok("선택된 공지사항이 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("공지사항 삭제 실패");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Notice>> searchNotices(
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Notice> noticePage;

        if ("title".equals(searchType)) {
            noticePage = noticeService.searchByTitle(keyword, pageable);
        } else if ("content".equals(searchType)) {
            noticePage = noticeService.searchByContent(keyword, pageable);
        } else {
            noticePage = noticeService.findAll(pageable); // 전체 조회
        }

        return ResponseEntity.ok(noticePage);
    }
    @GetMapping("/all")
    public ResponseEntity<Page<Notice>> allNotices(
            @RequestParam(required = false)String searchType,
            @RequestParam(required = false)String keyword,
            @PageableDefault(size = 10,sort = "id",direction = Sort.Direction.DESC) Pageable pageable){
        Page<Notice> noticePage;
        noticePage = noticeService.findAll(pageable);
        return ResponseEntity.ok(noticePage);
    }

}
