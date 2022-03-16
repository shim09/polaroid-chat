package com.project.polaroid.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "chatmessage_Table")
public class ChatMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "memberId", nullable = false)
    private MemberEntity writer;

    @ManyToOne
    @JoinColumn(name = "roomId", nullable = false)
    private ChatRoomEntity chatRoom;

    public ChatMessageEntity(String message, LocalDateTime time, ChatRoomEntity chatRoom, MemberEntity writer){
        this.message = message;
        this.time = time;
        this.chatRoom = chatRoom;
        this.writer = writer;
    }

}
