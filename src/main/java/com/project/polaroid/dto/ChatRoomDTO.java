package com.project.polaroid.dto;

import com.project.polaroid.entity.MemberEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
public class ChatRoomDTO {
    private Long id;
    private String writer;
    private String lastMessage;
    private LocalDateTime time;

    private MemberEntity member;



    public void makeChatRoomDTO(String message, String anotherUser, LocalDateTime time) {
        this.lastMessage = message;
        this.writer = anotherUser;
        this.time = time;
    }
}
