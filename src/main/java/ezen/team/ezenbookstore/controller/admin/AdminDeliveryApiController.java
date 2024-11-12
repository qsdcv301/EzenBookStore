package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.entity.Delivery;
import ezen.team.ezenbookstore.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/delivery")
public class AdminDeliveryApiController {

    private final DeliveryService deliveryService;

    @GetMapping("")
    public String deliveryControl(Model model) {
        List<Delivery> deliveryList = deliveryService.findAll();

        model.addAttribute("deliveryList", deliveryList);

        return "/admin/bookControl";
    }

    // 배송 목록 조회
    @PostMapping("/list")
    public String getDeliveryList() {
        // 배송 목록 조회 로직
        return "redirect:/admin/delivery"; // 배송 목록 페이지로 리다이렉트
    }

    // 배송 상세 조회
    @PostMapping("/detail")
    public String getDeliveryDetail(@RequestParam Long deliveryId) {
        // 배송 상세 조회 로직
        return "redirect:/admin/delivery"; // 상세 페이지로 리다이렉트 (예시로 목록 페이지로 설정)
    }

    // 배송 상태 업데이트
    @PostMapping("/updateStatus")
    public String updateDeliveryStatus(@RequestParam Long deliveryId, @RequestParam String status) {
        // 배송 상태 업데이트 로직
        return "redirect:/admin/delivery"; // 상태 업데이트 후 목록 페이지로 리다이렉트
    }


    //이하 기능이 필요한지는 논의

    // 배송 삭제
//    @PostMapping("/delete")
//    public ResponseEntity<String> deleteDelivery(@RequestParam Long deliveryId) {
//        // 배송 삭제 로직
//        return ResponseEntity.ok("배송 정보가 삭제되었습니다.");
//    }

    // 배송 생성 (예를 들어 배송 준비 상태로 생성)
//    @PostMapping("/create")
//    public ResponseEntity<String> createDelivery(@RequestBody Delivery delivery) {
//        // 배송 생성 로직
//        return ResponseEntity.ok("새 배송 정보가 생성되었습니다.");
//    }

}
