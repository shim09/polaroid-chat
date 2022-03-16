package com.project.polaroid.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="follow_table")
public class FollowEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "follow_id")
    private Long followId;

    // 팔로잉수
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follow_following")
    private MemberEntity followFollowing;

    // 팔로워수
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity followMember;
}
