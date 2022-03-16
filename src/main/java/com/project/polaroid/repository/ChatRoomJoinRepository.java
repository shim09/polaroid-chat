package com.project.polaroid.repository;

import com.project.polaroid.entity.ChatRoomEntity;
import com.project.polaroid.entity.ChatRoomJoinEntity;
import com.project.polaroid.entity.MemberEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChatRoomJoinRepository extends CrudRepository<ChatRoomJoinEntity,Long> {

//    public List<ChatRoomJoinEntity> findByUser(MemberEntity member);

    public List<ChatRoomJoinEntity> findByChatRoom(ChatRoomEntity chatRoom);

    public List<ChatRoomJoinEntity> findByMember(MemberEntity member);
}
