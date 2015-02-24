package server;

import java.io.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.websocket.*;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/", encoders = ChatikMessageEncoder.class, decoders = ChatikMessageDecoder.class)
public class ChatikEndpoint
{
    @OnClose
    public void onClose(final Session session)
    {
        String user = (String) session.getUserProperties().get("username");

        ChatikMessage response = new ChatikMessage("userDisconnected", user);
        replicate(session, response);
    }

    @OnMessage
    public void onMessage(final Session session, final ChatikMessage chatikMessage)
    {
        String command = chatikMessage.getCommand();

        if (command.equals("connect"))
        {
            String user = chatikMessage.getSender();
            connectNewUser(session, user);
            ChatikMessage msg = new ChatikMessage("userConnected", user);
            replicate(session, msg);
        }

        if (command.equals("send"))
        {
            replicate(session, chatikMessage);
        }

        if (command.equals("getUserlist"))
        {
            sendUserlist(session);
        }
    }

    private void replicate(Session session, ChatikMessage chatikMessage)
    {
        for (Session s : session.getOpenSessions())
        {
            if (!s.isOpen()) continue;
            try {
                s.getBasicRemote().sendObject(chatikMessage);
            } catch (Exception ex) {
                System.out.printf("Caught %s when sending messages\n", ex.getClass().toString());
            }
        }
    }

    private void connectNewUser(Session session, String username)
    {
        session.getUserProperties().put("username", username);
        ChatikMessage response = new ChatikMessage("connect", "You have successfully logged in to the 4atik! Have fun)");
        try {
            session.getBasicRemote().sendObject(response);
            System.out.println(username + " connected");
        } catch ( Exception e)
        {
            System.out.println("Error occurred while establishing connection to new user \n" + e);
        }
    }

    private void sendUserlist(Session session)
    {
        ChatikMessage response = new ChatikMessage("getUserlist", "");
        List<String> users = getConnectedUsers(session);
        if (users.isEmpty()) return;

        for(String user : users)
        {
            response.setMessage(user);
            try {
                session.getBasicRemote().sendObject(response);
            } catch (Exception ex)
            {
                System.out.println("Error occurred while sending userlist \n" + ex);
            }
        }
    }

    private List<String> getConnectedUsers(Session session)
    {
        List<String> users = new LinkedList<String>();
        for (Session s : session.getOpenSessions())
        {
            if (s.getUserProperties().containsKey("username"))
                users.add((String)s.getUserProperties().get("username"));
        }
        return users;
    }
}