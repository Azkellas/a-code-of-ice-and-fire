package com.codingame.antiyoy.view;

import static com.codingame.antiyoy.Constants.*;

public final class Constants {
    static public final int SCREEN_WIDTH = 1920;
    static public final int SCREEN_HEIGHT = 1080;

    static public final int LEFT_PANEL_WIDTH = 300;
    static public final int RIGHT_PANEL_WIDTH = 300;
    static public final int HEIGHT_GAP = 40;
    static public final int WIDTH_GAP = 0;
    static public final int BOARD_HEIGHT = SCREEN_HEIGHT - 2*HEIGHT_GAP;
    static public final int BOARD_WIDTH = SCREEN_WIDTH - LEFT_PANEL_WIDTH - RIGHT_PANEL_WIDTH;
    // static public final int BOARD_WIDTH = BOARD_HEIGHT;

    static public final int CELL_SIZE = Math.min(BOARD_HEIGHT / MAP_HEIGHT, BOARD_WIDTH / MAP_WIDTH);

    static public final int GRID_X = LEFT_PANEL_WIDTH + WIDTH_GAP + (BOARD_WIDTH  - CELL_SIZE * MAP_WIDTH)  / 2;
    static public final int GRID_Y = HEIGHT_GAP + (BOARD_HEIGHT - CELL_SIZE * MAP_HEIGHT) / 2;

    static public final int GRID_HEIGHT = SCREEN_HEIGHT - 100;

    static private final int PLAYER_TEXT_COLOR[] = {0xcc0000, 0x0000cc};
    static private final int PLAYER_UNIT_COLOR[] = {0xff0305, 0x26b9e5};
    static private final int PLAYER_ACTIVE_CELL_COLOR[] = {0x880000, 0x000088};
    static private final int PLAYER_INACTIVE_CELL_COLOR[] = {0x440000, 0x000044};

    static public final int PROTECTED_COLOR = 0x999999;

    static public final int PLAYER_AVATAR_RADIUS = 200;

    static public int getPlayerUnitColor(int playerId) {
        return PLAYER_UNIT_COLOR[playerId];
    }

    static public int getTurnColor(int playerId) {
        return PLAYER_UNIT_COLOR[Math.max(0, playerId)];
    }

    static public int getPlayerCellColor(int playerId, boolean isActive) {
        if (playerId == NEUTRAL)
            return 0x666666;
        if (playerId == VOID)
            return 0x000000;

        // owned by a player
        if (isActive)
            return PLAYER_ACTIVE_CELL_COLOR[playerId];
        else
            return PLAYER_INACTIVE_CELL_COLOR[playerId];
    }

    private Constants() {
    }
}