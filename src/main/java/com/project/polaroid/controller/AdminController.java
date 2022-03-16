package com.project.polaroid.controller;

import com.project.polaroid.entity.SellerEntity;
import com.project.polaroid.service.MemberService;
import com.project.polaroid.service.SellerRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    public final SellerRoleService sellerRoleService;

    @GetMapping("admin/adminPage")
    public String adminPage(Model model){
        List <SellerEntity> sellerEntityList = sellerRoleService.findAll();
        model.addAttribute("sellerList",sellerEntityList);
        return "admin/adminPage";
    }

    @GetMapping("admin/giveRole/{memberId}")
    public String giveRole(@PathVariable Long memberId){
        sellerRoleService.giveRole(memberId);
        return "redirect:/admin/adminPage";
    }
}
