package com.codingame.antiyoy;

import com.codingame.antiyoy.Constants.*;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Action {
    private ACTIONTYPE type;
    private String string;
    private int player;
    private int unitId;
    private int x;
    private int y;
    private int level;

    public Action(String string, ACTIONTYPE type, int player, int idlevel, int x, int y) {
        // Constructor for type == MOVE
        this.string = string;
        this.type = type;
        this.player = player;
        if (type == ACTIONTYPE.MOVE)
            this.unitId = idlevel;
        else if (type == ACTIONTYPE.TRAIN)
            this.level = idlevel;
        this.x = x;
        this.y = y;
    }

    public Action(String string, ACTIONTYPE type, int player, int x, int y) {
        // Constructor for type == TRAIN
        this.type = type;
        this.player = player;
        this.x = x;
        this.y = y;
    }

    public ACTIONTYPE getType() { return this.type; }
    public int getUnitId() { return this.unitId; }
    public int getLevel() { return this.level; }
    public int getPlayer() { return this.player; }
    public int getX() { return this.x; }
    public int getY() { return this.y; }

    public String toString() { return this.string; }
}