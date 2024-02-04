package com.code.sample.core.api.controller;

import com.code.sample.core.api.domain.Member;
import com.code.sample.core.api.domain.MemberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/signup")
    public Long signUp(Member member){
        return memberService.signUp(member);
    }

    @GetMapping("/hello")
    public Member helloMember1(){
        return new Member("멤버1", "mail@.com");
    }
}
