package com.codingame.game;
import com.codingame.gameengine.core.AbstractMultiplayerPlayer;

// Uncomment the line below and comment the line under it to create a Solo Game
public class Player extends AbstractMultiplayerPlayer {
    protected int expectedOutputLines = 1;
    private String message = "";

    public void setMessage(String msg) {
        this.message = msg;
    }

    public void resetMessage() {
        this.message = "";
    }

    public String getMessage() {
        if (this.message.length() > 20) {
            return message.substring(0, 17) + "...";
        }
        return message;

    }
    @Override
    public int getExpectedOutputLines() {
        // Returns the number of expected lines of outputs for a player
        return expectedOutputLines;
    }

    public String getTrimedPseudo() {
        String nickname = this.getNicknameToken();
        if (nickname.length() > 15) {
            nickname = nickname.substring(0, 12) + "...";
        }
        return nickname;
    }

    public void setExpectedOutputLines(int lines) {
        this.expectedOutputLines = lines;
    }
}
