package com.project.polaroid.service;

import com.project.polaroid.entity.ChatRoomEntity;
import com.project.polaroid.entity.ChatRoomJoinEntity;
import com.project.polaroid.entity.MemberEntity;
import com.project.polaroid.repository.ChatRoomJoinRepository;
import com.project.polaroid.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChatRoomJoinService {
    private final ChatRoomJoinRepository chatRoomJoinRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberService memberService;
    @Transactional(readOnly = true)
    public List<ChatRoomJoinEntity> findByMember(MemberEntity member){
        return chatRoomJoinRepository.findByMember(member);
    }

    @Transactional(readOnly = true)
    public Long check(String member1,String member2){
        MemberEntity memberFirst = memberService.findByNickname(member1);
        List<ChatRoomJoinEntity> listFirst = chatRoomJoinRepository.findByMember(memberFirst);
        Set<ChatRoomEntity> setFirst = new HashSet<>();
        for(ChatRoomJoinEntity chatRoomJoin : listFirst){
            setFirst.add(chatRoomJoin.getChatRoom());
        }
        MemberEntity memberSecond = memberService.findByNickname(member2);
        List<ChatRoomJoinEntity> listSecond = chatRoomJoinRepository.findByMember(memberSecond);
        for (ChatRoomJoinEntity chatRoomJoin : listSecond){
            if (setFirst.contains(chatRoomJoin.getChatRoom())){
                return chatRoomJoin.getChatRoom().getId();
            }
        }
        return 0L;
    }
    @Transactional
    public Long newRoom(String member1, String member2){
        Long ret = check(member1, member2);
        if (ret != 0){
            // 이미 존재하는 방일경우 방 번호를 리턴해준다.
            return ret;
        }
        ChatRoomEntity chatRoom = new ChatRoomEntity();
        ChatRoomEntity newChatRoom = chatRoomRepository.save(chatRoom);
        if (member1.equals(member2)){
            // 나와의 채팅은 한명만 존재한다.
            createRoom(member1,newChatRoom);
        }
        else {
            // 둘다입장.
            createRoom(member1,newChatRoom);
            createRoom(member2,newChatRoom);
        }
        return newChatRoom.getId();
    }
    @Transactional
    public void createRoom(String member, ChatRoomEntity chatRoom) {
        ChatRoomJoinEntity chatRoomJoin = new ChatRoomJoinEntity(memberService.findByNickname(member),chatRoom);
        chatRoomJoinRepository.save(chatRoomJoin);
    }
    @Transactional(readOnly = true)
    public List<ChatRoomJoinEntity> findByChatRoom(ChatRoomEntity chatRoom){
        return chatRoomJoinRepository.findByChatRoom(chatRoom);
    }
    @Transactional
    public void delete(ChatRoomJoinEntity chatRoomJoin){
        chatRoomJoinRepository.delete(chatRoomJoin);
    }

    public String findAnotherUser(ChatRoomEntity chatRoom, String name){
        List<ChatRoomJoinEntity> chatRoomJoins = findByChatRoom(chatRoom);
        for (ChatRoomJoinEntity chatRoomJoin : chatRoomJoins){
            if (name.equals(chatRoomJoin.getMember().getMemberNickname()) == false){
                return chatRoomJoin.getMember().getMemberNickname();
            }
        }
        return name;
    }
}
