package net.crash.gameofthree.domain;

import java.util.UUID;

import static net.crash.gameofthree.Constant.*;

/**
 * Created by Pi on 8/23/2017.
 */
public class Room {
    private Player firstPlayer;
    private Player secondPlayer;
    private String roomId;
    private int    current;
    private int    index;

    public Room() {
        index = 0;
        roomId = UUID.randomUUID().toString();
    }

    /**
     * To switch turn
     */
    public void switchTurn() {
        index = index == 0 ? 1 : 0;
    }

    /**
     * To validate turn
     * @param sessionId
     * @return
     */
    public boolean isValidTurn(String sessionId) {
        return  (index == 0 && sessionId.equals(firstPlayer.getSession().getId())) ||
                (index == 1 && sessionId.equals(secondPlayer.getSession().getId()));
    }

    /**
     * To calculate next number
     * @param action
     * @param sessionId
     */
    public void calculate(int action, String sessionId) {
        if (!isValidTurn(sessionId)) {
            getPlayer(sessionId).sendMsg(NOT_TURN);
            return;
        }
        current += action;
        current = (int) Math.round(((double) current) / 3);
        if (current == 1) {
            Player winner = getOtherPlayer(sessionId);
            Player looser = getPlayer(sessionId);
            winner.sendMsg(WINNER);
            if (null != winner.getName()) {
                looser.sendMsg(GREETING.replace("{1}", winner.getName()));
            }
            looser.sendMsg(GAME_OVER);
            close();
            return;
        } else if (current <= 0) {
            getPlayer(sessionId).sendMsg(ERROR);
        }
        sendMsg(sessionId, CURRENT + current);
    }

    public boolean isFull() {
        return null != firstPlayer && null != secondPlayer;
    }

    /**
     * To close the room
     */
    public void close() {
        if (null != firstPlayer) {
            firstPlayer.close();
        }
        if (null != secondPlayer) {
            secondPlayer.close();
        }
    }

    /**
     * To get player
     *
     * @param sessionId
     * @return
     */
    public Player getOtherPlayer(String sessionId) {
        if (sessionId.equals(firstPlayer.getSession().getId()))
            return secondPlayer;

        return firstPlayer;
    }

    /**
     * To get player
     *
     * @param sessionId
     * @return
     */
    public Player getPlayer(String sessionId) {
        if (sessionId.equals(firstPlayer.getSession().getId()))
            return firstPlayer;

        return secondPlayer;
    }

    /**
     * To send msg
     *
     * @param sessionId
     * @param msg
     */
    public void sendMsg(String sessionId, String msg) {
        if (sessionId.equals(firstPlayer.getSession().getId())) {
            if (index == 0) {
                sendToSecondPlayer(msg);
                sendToFirstPlayer(msg);
                switchTurn();
            } else {
                sendToFirstPlayer(NOT_TURN);
            }
        } else {
            if (index == 1) {
                sendToFirstPlayer(msg);
                sendToSecondPlayer(msg);
                switchTurn();
            } else {
                sendToSecondPlayer(NOT_TURN);
            }
        }
    }

    /**
     * To send to first player
     *
     * @param msg
     */
    public void sendToFirstPlayer(String msg) {
        if (null != firstPlayer) {
            firstPlayer.sendMsg(msg);
        }
    }

    /**
     * To send to second player
     *
     * @param msg
     */
    public void sendToSecondPlayer(String msg) {
        if (null != secondPlayer) {
            secondPlayer.sendMsg(msg);
        }
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(Player firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public Player getSecondPlayer() {
        return secondPlayer;
    }

    public void setSecondPlayer(Player secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public String getRoomId() {
        return roomId;
    }

}

