package com.project.polaroid.controller;

import com.project.polaroid.auth.PrincipalDetails;
import com.project.polaroid.dto.ChatRoomDTO;
import com.project.polaroid.entity.ChatMessageEntity;
import com.project.polaroid.entity.ChatRoomEntity;
import com.project.polaroid.entity.ChatRoomJoinEntity;
import com.project.polaroid.entity.MemberEntity;
import com.project.polaroid.service.ChatRoomJoinService;
import com.project.polaroid.service.ChatRoomService;
import com.project.polaroid.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ChatRoomController {
    private final MemberService memberService;
    private final ChatRoomJoinService chatRoomJoinService;
    private final ChatRoomService chatRoomService;
    @GetMapping("/chat")
    public String chatHome(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model){
        model.addAttribute("nickname",principalDetails.getMember().getMemberNickname());
        MemberEntity member = memberService.findByNickname(principalDetails.getMember().getMemberNickname());
        List<ChatRoomJoinEntity> chatRoomJoins = chatRoomJoinService.findByMember(member);
        List<ChatRoomDTO> chatRooms = chatRoomService.setting(chatRoomJoins,member);
        model.addAttribute("chatRooms", chatRooms);
        for (ChatRoomDTO c : chatRooms) {
            MemberEntity byId = memberService.findById(c.getId());
            c.setMember(byId);
        }
        if (member == null){
            model.addAttribute("memberName", "");
            model.addAttribute("memberId",0);
        } else {
            model.addAttribute("memberName",member.getMemberNickname());
            model.addAttribute("memberId",member.getId());
        }
        return "chat/main";
    }
    @PostMapping("/chat/newChat")
    public String newChat(@RequestParam("receiver") String member1, @RequestParam("sender") String member2){
        Long chatRoomId = chatRoomJoinService.newRoom(member1,member2);
        return "redirect:/personalChat/" + chatRoomId;
    }

    @RequestMapping("/personalChat/{chatRoomId}")
    public String goChat(@PathVariable("chatRoomId")Long chatRoomId, Model model,
                         @AuthenticationPrincipal PrincipalDetails principalDetails){
        Optional<ChatRoomEntity> opt = chatRoomService.findById(chatRoomId);
        ChatRoomEntity chatRoom = opt.get();
        List<ChatMessageEntity> messages = chatRoom.getMessageEntityList();
        Collections.sort(messages, (t1, t2) -> {
            if (t1.getId() > t2.getId()) return 1;
            else return -1;
        });
        if (principalDetails.getMember() == null){
            model.addAttribute("memberName", "");
            model.addAttribute("memberId", 0);
        }else {
            model.addAttribute("MemberName", principalDetails.getMember().getMemberNickname());
            model.addAttribute("memberId", principalDetails.getMember().getId());
        }
        List<ChatRoomJoinEntity> list = chatRoomJoinService.findByChatRoom(chatRoom);
        model.addAttribute("messages", messages);
        model.addAttribute("nickname",principalDetails.getMember().getMemberNickname());
        model.addAttribute("profile",principalDetails.getMember().getMemberFilename());
        model.addAttribute("chatRoomId",chatRoomId);
        int cnt = 0;
        for (ChatRoomJoinEntity join : list){
            if (join.getMember().getMemberNickname().equals(principalDetails.getMember().getMemberNickname()) == false){
                model.addAttribute("receiver", join.getMember().getMemberNickname());
                ++cnt;
            }
        }
        if (cnt >= 2){
            return "redirect:/chat";
        }
        if (cnt == 0){
            model.addAttribute("receiver", "");
        }
        return "chat/chatRoom";
    }

    // 닉네임 체크
    @GetMapping("/users/nameChk/{name}")
    public @ResponseBody String nameChk(@PathVariable("name") String name){
        if(memberService.findByNickname(name)!= null){
            return "success";
        }
        return "false";
    }

}
