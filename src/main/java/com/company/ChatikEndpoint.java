package com.company;

import java.io.IOException;

import javax.websocket.*;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/")
public class ChatikEndpoint {

    @OnOpen
    public void onOpen(Session session)
    {
        System.out.print("New user connected");
    }

    @OnMessage
    public String onMessage(String message, Session session)
    {
        System.out.println(message);
        return message;
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason)
    {
        System.out.println(String.format("Session %s closed because of %s", session.getId(), closeReason));
    }
}