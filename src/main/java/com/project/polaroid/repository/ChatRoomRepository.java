package com.project.polaroid.repository;

import com.project.polaroid.entity.ChatRoomEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ChatRoomRepository extends CrudRepository<ChatRoomEntity,Long> {

    public Optional<ChatRoomEntity> findById(Long id);
}
