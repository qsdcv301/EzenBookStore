package ezen.team.ezenbookstore.controller.admin;

import ezen.team.ezenbookstore.dto.AdminDashBoardDto;
import ezen.team.ezenbookstore.service.OrdersService;
import ezen.team.ezenbookstore.service.facade.AdminDashBoardServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminDashBoardApiController {

    private final AdminDashBoardServiceImpl adminDashBoardServiceImpl;

    @GetMapping({"","/"})
    public String adminDashBoard(Model model) {
        AdminDashBoardDto adminDashBoardDto = adminDashBoardServiceImpl.AdminDashBoard();
        model.addAttribute("adminDashBoard", adminDashBoardDto);
        return "admin/adminDashBoard";
    }

}
