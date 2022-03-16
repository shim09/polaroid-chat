package com.project.polaroid.controller;

import com.project.polaroid.entity.MemberEntity;
import com.project.polaroid.service.IndexService;
import com.project.polaroid.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final MemberService memberService;
    private final IndexService indexService;
    private final JavaMailSender javaMailSender;

    // 시작 페이지
    @GetMapping({"","/"})
    public String index() {
        return "index";
    }

    // 인증코드 발송
    String sendCode="";
    @PostMapping("/sendMail")
    public @ResponseBody String sendMail(@RequestParam("mail") String mail) {
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

        System.out.println("확인용 나중에 삭제 key = " + code);

        message.setSubject("Polaroid 인증번호 입력을 위한 메일 전송");
        message.setText("인증 번호 : "+code);
        javaMailSender.send(message);
        return "ok";
    }

    // 인증코드 확인
    @PostMapping("codeCheck")
    public @ResponseBody String codeCheck(@RequestParam("code") String code){
        if (code.equals(sendCode)) {
            return "ok";
        }
        else {
            return "no";
        }
    }

    // 이메일 중복체크
    @PostMapping("/mailDuplicate")
    public @ResponseBody String mailDuplicate(@RequestParam ("mail") String mail){
        String result=memberService.mailDuplicate(mail);
        return result;
    }

    // 닉네임 중복체크
    @PostMapping({"/nicknameDuplicate","/member/nicknameDuplicate"})
    public @ResponseBody String nicknameDuplicate(@RequestParam ("nickname") String nickname){
        String result=memberService.nicknameDuplicate(nickname);
        return result;
    }

    // 로그인페이지 이동
    @GetMapping("/login")
    public String loginForm(){
        return "login";
    }

    // 회원가입페이지 이동
    @GetMapping("/join")
    public  String joinForm(){
        return "join";
    }

    // 회원가입 처리
    @PostMapping("/join")
    public String join(MemberEntity member){
        memberService.memberSave(member);
        sendCode=UUID.randomUUID().toString();
        return "redirect:/login";
    }

    // 비밀번호 변경 페이지 이동
    @GetMapping("/lostPassword")
    public String lostPasswordForm(){
        return "lostPassword";
    }

    // 비밀번호 변경 처리
    @PostMapping("/lostPassword")
    public @ResponseBody String lostPassword(@RequestParam ("memberEmail") String memberEmail){
        // 변경할 비밀번호 이메일로 전송
        MemberEntity member=indexService.findPassword(memberEmail);
        String result=null;
        if(member != null) {
            String mail = member.getMemberCheckmail();

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(mail); // 인증코드 받들 사용자 메일 주소

            String password = UUID.randomUUID().toString();
            ;
            System.out.println("확인용 나중에 삭제 password = " + password);

            message.setSubject("Polaroid 비밀번호 변경");
            message.setText("변경된 비밀번호 : " + password);
            javaMailSender.send(message);

            // 비밀번호 변경
            Long memberId = member.getId();
            indexService.lostPassword(password, memberId);

            result="<script>alert('등록된 이메일을 확인해 주세요.');location.href='login'</script>";
        }
        else{
            result="<script>alert('등록되지 않은 아이디 입니다.');location.href='login'</script>";
        }
        return result;
    }

    // 권한없을때
    @GetMapping("/accessDenied")
    public String accessDenied(){
        // alert 처리
        return "accessDenied";
    }

    // 권한 테스트
    @GetMapping("/member")
    public @ResponseBody String member(){
        return "member";
    }


    // 권한 테스트
    @GetMapping("/seller")
    public @ResponseBody String seller(){
        return "seller";
    }

    // 권한 테스트
    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }


}