package server;

import javax.websocket.*;
import javax.json.*;
import java.io.StringReader;
import java.util.Date;

public class ChatikMessageDecoder implements Decoder.Text<ChatikMessage>
{
    @Override
    public void init(final EndpointConfig config) {}

    @Override
    public void destroy() {}

    @Override
    public ChatikMessage decode(final String textMessage) throws DecodeException
    {
        ChatikMessage chatMessage = new ChatikMessage();
        JsonObject obj = Json.createReader(new StringReader(textMessage)).readObject();
        chatMessage.setMessage(obj.getString("message"));
        chatMessage.setSender(obj.getString("sender"));
        chatMessage.setCommand(obj.getString("command"));
        chatMessage.setReceived(new Date());
        return chatMessage;
    }

    @Override
    public boolean willDecode(final String s) {
        return true;
    }
}
