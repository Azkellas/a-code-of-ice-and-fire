package com.codingame.antiyoy.view;

import static com.codingame.antiyoy.Constants.*;

public final class Constants {
    static public final int SCREEN_WIDTH = 1920;
    static public final int SCREEN_HEIGHT = 1080;

    static public final int LEFT_PANEL_WIDTH = 300;
    static public final int BOARD_WIDTH = SCREEN_WIDTH - LEFT_PANEL_WIDTH;
    static public final int BOARD_HEIGHT = SCREEN_HEIGHT;

    static public final int CELL_SIZE = Math.min(BOARD_HEIGHT / MAP_HEIGHT, BOARD_WIDTH / MAP_WIDTH);
    static public final int UNIT_RADIUS = CELL_SIZE / 3;

    static public final int GRID_X = LEFT_PANEL_WIDTH + (BOARD_WIDTH  - CELL_SIZE * MAP_WIDTH)  / 2;
    static public final int GRID_Y = (BOARD_HEIGHT - CELL_SIZE * MAP_HEIGHT) / 2;

    static private final int PLAYER_UNIT_COLOR[] = {0xcc0000, 0x00cc00};
    static private final int PLAYER_CELL_COLOR[] = {0x880000, 0x008800};

    static public final int PLAYER_AVATAR_RADIUS = 200;

    static public int getPlayerUnitColor(int playerId) {
        return PLAYER_UNIT_COLOR[playerId];
    }

    static public int getPlayerCellColor(int playerId) {
        if (playerId == NEUTRAL)
            return 0x666666;
        if (playerId == VOID)
            return 0x000000;

        // owned by a player
        return PLAYER_CELL_COLOR[playerId];
    }

    private Constants() {
    }
}