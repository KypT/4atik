package server;

import javax.json.Json;
import javax.websocket.*;
import java.text.*;

public class ChatikMessageEncoder implements Encoder.Text<ChatikMessage> {
    @Override
    public void init(final EndpointConfig config) {}

    @Override
    public void destroy() {}

    @Override
    public String encode(final ChatikMessage chatikMessage) throws EncodeException
    {
        Format formatter = new SimpleDateFormat("HH:mm");
        return Json.createObjectBuilder()
                .add("message", chatikMessage.getMessage())
                .add("sender", chatikMessage.getSender())
                .add("command", chatikMessage.getCommand())
                .add("received", formatter.format(chatikMessage.getReceived())).build()
                .toString();
    }
}