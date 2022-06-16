package com.project.pingme.mapper;

import com.project.pingme.dto.ChatMessage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MessageMapper {

    @Select("SELECT * FROM MESSAGES")
    List<ChatMessage> getAllMessages();

    @Select("SELECT * FROM MESSAGES WHERE username = #{username}")
    ChatMessage getMessage(String username);

    @Insert("INSERT INTO MESSAGES (username, messagetext, messagetime) " +
            "VALUES(#{username}, #{messageText}, #{messageTime})")
    @Options(useGeneratedKeys = true, keyProperty = "messageId")
    int insert(ChatMessage message);
}
