package net.crash.gameofthree.ws;

import net.crash.gameofthree.domain.Player;
import net.crash.gameofthree.domain.Room;
import net.crash.gameofthree.util.GameCache;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import static net.crash.gameofthree.Constant.GET_START;

/**
 * Created by Pi on 8/23/2017.
 */

@ServerEndpoint(value = "/join/{playerName}")
public class GameEndpoint {
    private static final GameCache CACHE = GameCache.getInstance();

    @OnOpen
    public void onOpen(Session session, @PathParam("playerName") String playerName) throws IOException {
        Player player = new Player(playerName, session);
        CACHE.addPlayer(player);
        session.getBasicRemote().sendText(GET_START);
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        Room room = CACHE.getRoom(session.getId());
        try {
            int action = Integer.valueOf(message);
            room.calculate(action, session.getId());
        } catch (Exception ex) {
            room.getPlayer(session.getId()).sendMsg(message);
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        String sessionId = session.getId();
        Room   room      = CACHE.getRoom(sessionId);
        Player player    = room.getPlayer(sessionId);
        player.close();
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }
}
