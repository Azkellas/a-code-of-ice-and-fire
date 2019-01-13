package com.codingame.game;
import com.codingame.gameengine.core.AbstractMultiplayerPlayer;

// Uncomment the line below and comment the line under it to create a Solo Game
public class Player extends AbstractMultiplayerPlayer {
    protected int expectedOutputLines = 1;

    @Override
    public int getExpectedOutputLines() {
        // Returns the number of expected lines of outputs for a player
        return expectedOutputLines;
    }

    public void setExpectedOutputLines(int lines) {
        this.expectedOutputLines = lines;
    }
}
