package org.springframework.social.youtube.api;

import org.springframework.social.youtube.api.impl.Message;

import java.util.List;

/**
 * User: lancea10
 * Date: 7/24/12
 * Time: 7:35 PM
 */
public interface InboxOperations {


    public List<Message> getMessages();

    public boolean sendMessage(Message message);

    public boolean deleteMessage(Message message);
}
