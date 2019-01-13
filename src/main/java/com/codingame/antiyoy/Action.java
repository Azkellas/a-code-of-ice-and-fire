package com.codingame.antiyoy;

import com.codingame.antiyoy.Constants.*;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Action {
    private ACTIONTYPE type;
    private String string;
    private int player;
    private int unitId;
    private Cell cell;
    private int level;

    public Action(String string, ACTIONTYPE type, int player, int idlevel, Cell cell) {
        // Constructor for type == MOVE or TRAIN
        this.string = string;
        this.type = type;
        this.player = player;
        if (type == ACTIONTYPE.MOVE)
            this.unitId = idlevel;
        else if (type == ACTIONTYPE.TRAIN)
            this.level = idlevel;
        this.cell = cell;
    }

    public Action(String string, ACTIONTYPE type, int player, Cell cell) {
        // Constructor for type == BUILD
        this.type = type;
        this.player = player;
        this.cell = cell;
    }

    public ACTIONTYPE getType() { return this.type; }
    public int getUnitId() { return this.unitId; }
    public int getLevel() { return this.level; }
    public int getPlayer() { return this.player; }
    public Cell getCell() { return this.cell; }

    public String toString() { return this.string; }
}