package com.project.polaroid.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "chatroomjoin_Table")
public class ChatRoomJoinEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "memberId", nullable = false)
    private MemberEntity member;

    @ManyToOne
    @JoinColumn(name = "roomId", nullable = false)
    private ChatRoomEntity chatRoom;

    public ChatRoomJoinEntity(MemberEntity member, ChatRoomEntity chatRoom){
        this.member = member;
        this.chatRoom = chatRoom;
    }
}
