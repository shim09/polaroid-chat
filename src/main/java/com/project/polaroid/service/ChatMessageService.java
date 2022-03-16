package com.project.polaroid.service;

import com.project.polaroid.dto.ChatMessageDTO;
import com.project.polaroid.entity.ChatMessageEntity;
import com.project.polaroid.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final MemberService memberService;
    private final ChatRoomService chatRoomService;

    @Transactional
    public void save(ChatMessageDTO message) {
        ChatMessageEntity chatMessageEntity = new ChatMessageEntity(message.getMessage(), LocalDateTime.now(),chatRoomService.findById(message.getChatRoomId()).get()
        ,memberService.findByNickname(message.getSender()));
        chatMessageRepository.save(chatMessageEntity);
    }
}
