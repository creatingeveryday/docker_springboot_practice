package com.code.sample.core.api.controller;

import com.code.sample.core.api.domain.Member;
import com.code.sample.core.api.domain.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberControllerTest {

    @Autowired
    private MemberService memberService;

    @Test
    void signUp() {
        Member member = new Member("회원1", "member1@gmail.com");
        Long id = memberService.signUp(member);
        assertThat(id).isEqualTo(1L);
    }
}