package com.code.sample.storage.member;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberEntityRepositoryTest {

    @Autowired
    private MemberEntityRepository memberEntityRepository;

    @Test
    void add() {
        memberEntityRepository.save(new MemberEntity("회원1", "회원1@mail.com"));

        MemberEntity findMember = memberEntityRepository.findById(1L).get();

        assertThat(findMember.getName()).isEqualTo("회원1");
    }

}