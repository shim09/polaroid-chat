package com.project.polaroid.repository;

import com.project.polaroid.entity.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FollowRepository extends JpaRepository<FollowEntity,Long> {

    // 팔로잉 수
    @Query(value = "SELECT count(a) FROM FollowEntity a WHERE a.followFollowing.id= :memberId ")
    public int followingCount(Long memberId);

    // 팔로워 수
    @Query(value = "SELECT count(a) FROM FollowEntity a WHERE a.followMember.id= :memberId ")
    public int followerCount(Long memberId);

    // 팔로워 리스트
    @Query(value = "SELECT a FROM FollowEntity a WHERE a.followMember.id= :memberId ")
    public List<FollowEntity> follower(Long memberId);

    // 팔로잉 리스트
    @Query(value = "SELECT a FROM FollowEntity a WHERE a.followFollowing.id= :memberId ")
    public List<FollowEntity> following(Long memberId);
}
