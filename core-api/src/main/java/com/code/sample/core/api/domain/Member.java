package com.code.sample.core.api.domain;

import com.code.sample.storage.member.MemberEntity;

public class Member {
    private Long id;
    private String name;

    private String email;

    public Member(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Member(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }


    public MemberEntity toEntity() {
        return new MemberEntity(name, email);
    }

    public static Member fromEntity(MemberEntity memberEntity) {
        return new Member(memberEntity.getName(), memberEntity.getEmail());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
