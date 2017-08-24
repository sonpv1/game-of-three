package net.crash.gameofthree.util;

import net.crash.gameofthree.domain.Player;
import net.crash.gameofthree.domain.Room;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static net.crash.gameofthree.Constant.YOUR_NUM;

/**
 * Created by Pi on 8/23/2017.
 */
public class GameCache {
    private static final Map<String, Room>   ROOM        = new ConcurrentHashMap<>();
    private static final Map<String, String> PLAYER_ROOM = new ConcurrentHashMap<>();
    private static final Random              RANDOM      = new Random();
    private static final int                 MAX         = 10000;
    private static final GameCache           INSTANCE    = new GameCache();

    private GameCache() {}

    public static GameCache getInstance() {
        return INSTANCE;
    }

    /**
     * To get room by session Id
     * @param sessionId
     * @return
     */
    public Room getRoom(String sessionId) {
        String roomId = PLAYER_ROOM.get(sessionId);
        return ROOM.get(roomId);
    }

    /**
     * Add new player to available room
     *
     * @param player
     */
    public Room addPlayer(Player player) {
        long count = ROOM.values().stream().filter(r -> !r.isFull()).count();
        if (count == 0) {
            Room room = new Room();
            ROOM.put(room.getRoomId(), room);
        }
        Room room = ROOM.values().stream().filter(r -> !r.isFull()).findFirst().get();
        if (null == room.getFirstPlayer()) {
            room.setFirstPlayer(player);
        } else {
            room.setSecondPlayer(player);
            int n = RANDOM.nextInt(MAX) + 1;
            room.setCurrent(n);
            room.sendToSecondPlayer(YOUR_NUM + String.valueOf(n));
            room.switchTurn();
        }
        PLAYER_ROOM.put(player.getSession().getId(), room.getRoomId());
        System.out.println("PLAYER: " + player.getName() + " SESSION: " + player.getSession().getId() + " HAS JOINED TO ROOM: " + room.getRoomId());
        return room;
    }
}
