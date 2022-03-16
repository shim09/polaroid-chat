package com.project.polaroid.controller;

import com.project.polaroid.auth.PrincipalDetails;
import com.project.polaroid.dto.MemberAddInfo;
import com.project.polaroid.dto.MemberUpdateDTO;
import com.project.polaroid.entity.MemberEntity;
import com.project.polaroid.service.FollowService;
import com.project.polaroid.service.MemberService;
import com.project.polaroid.service.SellerRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final FollowService followService;
    private final SellerRoleService sellerRoleService;
    private final JavaMailSender javaMailSender;


    @GetMapping("/addInfo")
    public String addInfoForm(){
        return "member/addInfo";
    }

    // oauth로그인시 추가정보
    @PostMapping("/addInfo")
    public String addInfo(@ModelAttribute MemberAddInfo memberAddInfo,
                          @AuthenticationPrincipal PrincipalDetails principalDetails){
        System.out.println("principalDetails.getMember().getId() = " + principalDetails.getMember().getId());
        memberService.memberAddInfo(memberAddInfo,principalDetails.getMember().getId());
        return "index";
    }

    // 마이페이지 출력 (팔로워 수, 내 정보)
    @GetMapping("/mypage")
    public String mypageForm(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model){
        MemberEntity member=memberService.findById(principalDetails.getMember().getId());
        model.addAttribute("member",member);
        ArrayList<Integer> followCount=followService.followCount(principalDetails.getMember().getId());
        System.out.println("MemberController.mypageForm");
        System.out.println(followCount.get(0));
        System.out.println(followCount.get(1));
        model.addAttribute("follower",followCount.get(0));
        model.addAttribute("following",followCount.get(1));
        return "member/myPage";
    }

    // 판매자 권한신청
    @GetMapping("/sellerRole")
    public @ResponseBody String sellerRole(@AuthenticationPrincipal PrincipalDetails principalDetails){
        System.out.println("MemberController.sellerRole");
        String duplicate=sellerRoleService.save(principalDetails.getMember().getId());
        String result=null;
        if(duplicate=="ok"){
            result="<script>alert('판매자권한 신청이 완료되었습니다.');location.href='mypage'</script>";
        }
        else {
            result="<script>alert('이미 판매자권한을 신청 하셨습니다.');location.href='mypage'</script>";
        }
        return result;
    }

    // 본인 확인페이지
    @GetMapping("/selfAuthentication")
   public String selfAuthentication (){
        return "member/selfAuthentication";
    }

    // 본인인증코드 발송
    String sendCode="";
    @PostMapping("/submitCode")
    public @ResponseBody String submitCode(@AuthenticationPrincipal PrincipalDetails principalDetails){
        System.out.println("MemberController.submitCode");
        MemberEntity member=memberService.findById(principalDetails.getMember().getId());
        String mail=member.getMemberCheckmail();
        Random random=new Random();
        String code="";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mail); // 인증코드 받들 사용자 메일 주소
        //인증코드 생성
        for(int i =0; i<3;i++) {
            int index=random.nextInt(25)+65; //A~Z까지 랜덤 알파벳 생성
            code+=(char)index;
        }
        int numIndex=random.nextInt(9999)+1000; //4자리 랜덤 정수를 생성

        code+=numIndex;
        sendCode=code;

        System.out.println("MemberController.submitCode");
        System.out.println("확인용 나중에 삭제 key = " + code);

        message.setSubject("본인 확인을 위한 인증코드 메일 전송");
        message.setText("인증 번호 : "+code);
        javaMailSender.send(message);
        return "ok";
    }

    // 본인확인 인증코드 확인
    @PostMapping("codeCheck")
    public @ResponseBody String codeCheck(@RequestParam("code") String code){
        if (code.equals(sendCode)) {
            return "ok";
        }
        else {
            return "no";
        }
    }

    // 본인확인 -> 수정페이지
    @PostMapping("selfAuthentication")
    public String selfAuthentication(@AuthenticationPrincipal PrincipalDetails principalDetails,Model model){
        MemberEntity member=memberService.findById(principalDetails.getMember().getId());
        model.addAttribute("member",member);
        sendCode= UUID.randomUUID().toString();
        return "member/update";
    }

    // 회원정보 변경
    @PostMapping("/update")
    public String memberUpdate(@ModelAttribute MemberUpdateDTO member,@AuthenticationPrincipal PrincipalDetails principalDetails ) throws Exception{
        memberService.memberUpdate(member,principalDetails.getMember().getId());
        return "index";
    }

    // 회원탈퇴 페이지
    @GetMapping("/resign")
    public String memberResignForm(@AuthenticationPrincipal PrincipalDetails principalDetails,Model model){
        MemberEntity member=memberService.findById(principalDetails.getMember().getId());
        model.addAttribute("member",member);
        return "member/resign";
    }

    //회원탈퇴 처리
    @PostMapping("/resign")
    public String memberResign(@AuthenticationPrincipal PrincipalDetails principalDetails){
        memberService.memberResign(principalDetails.getMember().getId());
        return "redirect:http://localhost:8081/logout";
    }

    // 팔로우 팔로잉 리스트 페이지
    @GetMapping("/followList")
    public String followList(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model){
        MemberEntity member=memberService.findById(principalDetails.getMember().getId());
        model.addAttribute("member",member);
        ArrayList<Integer> followCount=followService.followCount(principalDetails.getMember().getId());

        // 팔로윙 리스트
        model.addAttribute("followingList",followService.followingList(principalDetails.getMember().getId()));
        // 팔로우 리스트
        model.addAttribute("followerList",followService.followerList(principalDetails.getMember().getId()));

        System.out.println("MemberController.mypageForm");
        System.out.println(followCount.get(0));
        System.out.println(followCount.get(1));
        model.addAttribute("follower",followCount.get(0));
        model.addAttribute("following",followCount.get(1));
        return "member/followList";
    }
}
