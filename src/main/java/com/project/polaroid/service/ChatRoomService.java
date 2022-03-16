package com.project.polaroid.service;

import com.project.polaroid.dto.ChatRoomDTO;
import com.project.polaroid.entity.ChatMessageEntity;
import com.project.polaroid.entity.ChatRoomEntity;
import com.project.polaroid.entity.ChatRoomJoinEntity;
import com.project.polaroid.entity.MemberEntity;
import com.project.polaroid.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomJoinService chatRoomJoinService;
    @Transactional(readOnly = true)
    public Optional<ChatRoomEntity> findById(Long id){
        return chatRoomRepository.findById(id);
    }

    public List<ChatRoomDTO> setting(List<ChatRoomJoinEntity> chatRoomJoins, MemberEntity member){
        List<ChatRoomDTO> chatRooms = new ArrayList<>();
        for (ChatRoomJoinEntity tmp : chatRoomJoins){
            ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
            ChatRoomEntity chatRoom = tmp.getChatRoom();
            chatRoomDTO.setId(chatRoom.getId());
            if (chatRoom.getMessageEntityList().size() !=0){
                Collections.sort(chatRoom.getMessageEntityList(), new Comparator<ChatMessageEntity>(){
                @Override
                        public int compare(ChatMessageEntity c1, ChatMessageEntity c2){
                    if (c1.getTime().isAfter(c2.getTime())) {
                        return -1;
                    }
                        else {
                            return 1;
                        }
                    }
                });
            ChatMessageEntity lastMessage = chatRoom.getMessageEntityList().get(0);
            chatRoomDTO.makeChatRoomDTO(lastMessage.getMessage(),chatRoomJoinService.findAnotherUser(chatRoom,member.getMemberNickname()),lastMessage.getTime());
            chatRooms.add(chatRoomDTO);
            }
            else {
                chatRoomJoinService.delete(tmp);
            }
        }
        Collections.sort(chatRooms,new Comparator<ChatRoomDTO>(){
            @Override
            public int compare(ChatRoomDTO c1, ChatRoomDTO c2){
                if (c1.getTime().isAfter(c2.getTime())){
                    return -1;
                }
                else {
                    return 1;
                }
            }
        });
        return chatRooms;
    }


}
