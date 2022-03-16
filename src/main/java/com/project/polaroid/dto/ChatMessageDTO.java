package com.project.polaroid.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChatMessageDTO {
    private Long ChatRoomId;
    private String receiver;
    private String sender;
    private String message;
}
