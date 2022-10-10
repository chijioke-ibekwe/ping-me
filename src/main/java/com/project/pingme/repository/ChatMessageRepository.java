package com.project.pingme.repository;

import com.project.pingme.entity.ChatMessage;
import com.project.pingme.entity.UserContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByUserContact(UserContact userContact);
}
