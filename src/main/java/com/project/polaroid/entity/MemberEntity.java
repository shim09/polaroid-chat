package com.project.polaroid.entity;

import com.project.polaroid.dto.MemberUpdateDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="member_table")
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "member_id")
    private Long id;

    @Column(unique = true, length = 50)
    private String memberEmail;

    // 암호화되어 저장되어 길이가 김
    @Column(length = 70)
    private String memberPw;

    @Column(unique = true, length = 20)
    private String memberNickname;

    @Column(length = 11)
    private String memberPhone;

    @Column(length = 100)
    private String memberAddress;

    @Column(length = 100)
    private String memberFilename;

    @Column(length = 20)
    private String memberRole;

    @Column(length = 20)
    private String memberProvider;

    @Column(length = 50)
    private String memberProviderId;

    @Column(length = 30)
    private String  memberCheckmail;

    // 팔로우 테이블
    @OneToMany(mappedBy = "followFollowing", cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.LAZY)
    private List<FollowEntity> memberFollowing = new ArrayList<>();

    @OneToMany(mappedBy = "followMember", cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.LAZY)
    private List<FollowEntity> memberFollower = new ArrayList<>();

    // 판매자 권한 테이블
    @OneToMany(mappedBy = "memberId", cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.LAZY)
    private List<SellerEntity> sellerEntityList = new ArrayList<>();

//    // 채팅 메시지 테이블
//    @OneToMany(mappedBy = "memberId", cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.LAZY)
//    private List<ChatMessageEntity> chatMessageEntityList = new ArrayList<>();


    @Builder
    public MemberEntity(String memberEmail, String memberPw, String memberFilename, String memberRole, String memberProvider, String memberProviderId, String memberCheckmail) {
        this.memberEmail = memberEmail;
        this.memberPw = memberPw;
        this.memberFilename = memberFilename;
        this.memberRole = memberRole;
        this.memberProvider = memberProvider;
        this.memberProviderId = memberProviderId;
        this.memberCheckmail = memberCheckmail;
    }

    public static MemberEntity UpdateDTOtoEntity(MemberUpdateDTO memberUpdateDTO){
        MemberEntity member=new MemberEntity();
        member.setMemberFilename(memberUpdateDTO.getMemberFilename());
        member.setMemberAddress(memberUpdateDTO.getMemberAddress());
        member.setMemberNickname(memberUpdateDTO.getMemberNickname());
        member.setMemberPhone(memberUpdateDTO.getMemberPhone());
        return member;
    }


}
