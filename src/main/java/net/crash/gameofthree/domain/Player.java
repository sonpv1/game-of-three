package net.crash.gameofthree.domain;

import javax.websocket.Session;
import java.io.IOException;

/**
 * Created by Pi on 8/23/2017.
 */
public class Player {
    private String name;
    private Session session;

    public Player(Session session) {
        this.session = session;
    }

    public Player(String name, Session session) {
        this.name = name;
        this.session = session;
    }

    /**
     * To close session
     */
    public void close() {
        try {session.close();} catch (IOException e) {}
    }

    /**
     * To send message
     * @param msg
     */
    public void sendMsg(String msg) {
        try {
            session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return session != null ? session.equals(player.session) : player.session == null;
    }

    @Override
    public int hashCode() {
        return session != null ? session.hashCode() : 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
