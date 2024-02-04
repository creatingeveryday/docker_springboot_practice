package com.code.sample.core.api.domain;

import com.code.sample.storage.member.MemberEntity;
import com.code.sample.storage.member.MemberEntityRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private final MemberEntityRepository memberEntityRepository;

    public MemberService(MemberEntityRepository memberEntityRepository) {
        this.memberEntityRepository = memberEntityRepository;
    }


    public Long signUp(Member member) {
        return memberEntityRepository.save(member.toEntity()).getId();
    }

    public List<Member> selectAll() {
        List<MemberEntity> list = memberEntityRepository.findAll();

        List<Member> result = new ArrayList<>();
        for (MemberEntity memberEntity : list) {
            Member member = Member.fromEntity(memberEntity);
            result.add(member);
        }
        return result;
    }
}
