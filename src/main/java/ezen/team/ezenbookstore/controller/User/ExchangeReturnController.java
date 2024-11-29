package ezen.team.ezenbookstore.controller.User;

import ezen.team.ezenbookstore.entity.ExchangeReturn;
import ezen.team.ezenbookstore.entity.User;
import ezen.team.ezenbookstore.service.ExchangeReturnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/er")
public class ExchangeReturnController {

    private final ExchangeReturnService exchangeReturnService;

    @PostMapping("/{questionId}")
    public ResponseEntity<Map<String, String>> findEA(@PathVariable(required = false) String questionId) {
        Map<String, String> response = exchangeReturnService.findExchangeReturnDetails(questionId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Boolean>> addQnA(@ModelAttribute ExchangeReturn er,
                                                       @RequestParam(name = "orderItemId") long orderItemId,
                                                       @RequestParam(name = "file") MultipartFile file,
                                                       @ModelAttribute("user") User user) {
        Map<String, Boolean> response = exchangeReturnService.addExchangeReturn(er, orderItemId, user, file);
        return ResponseEntity.ok(response);
    }

}
