package com.project.polaroid.repository;

import com.project.polaroid.entity.ChatMessageEntity;
import org.springframework.data.repository.CrudRepository;


public interface ChatMessageRepository extends CrudRepository<ChatMessageEntity,Long> {
}
