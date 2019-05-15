package com.codingame.antiyoy.view;

import com.codingame.game.Player;


import com.codingame.antiyoy.GameState;

import com.codingame.gameengine.module.entities.Entity;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.entities.Text;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import java.util.concurrent.atomic.AtomicInteger;

import static com.codingame.antiyoy.view.Constants.*;

public class PlayerView extends AbstractView {
    private Group group;

    public Player model;
    private Sprite background;
    private Text gold;
    private Text income;
    private Text pseudo;
    private Sprite avatar;
    AtomicInteger goldModel;
    AtomicInteger incomeModel;

    public PlayerView(GraphicEntityModule entityModule, TooltipModule tooltipModule, Player player, GameState gameState) {
        super(entityModule, tooltipModule);
        this.model = player;
        this.goldModel = gameState.getAtomicGold(player.getIndex());
        this.incomeModel = gameState.getAtomicIncome(player.getIndex());
        createPlayerView();
    }

    public void createPlayerView() {
        int playerIndex = this.model.getIndex();
        String color = playerIndex == 0 ? "RED" : "BLUE";

        int baseY = playerIndex == 0 ? 112 : 644;

        avatar = entityModule.createSprite()
                .setAnchor(0)
                .setBaseHeight(PLAYER_AVATAR_RADIUS-12)
                .setBaseWidth(PLAYER_AVATAR_RADIUS-12)
                .setImage(this.model.getAvatarToken())
                .setX(50)
                .setY(baseY + 350/2 - PLAYER_AVATAR_RADIUS/2)
                .setZIndex(20);

        background = entityModule.createSprite()
                .setAnchor(0)
                .setImage("HUD_" + color + ".png")
                .setX(238 - 50 - PLAYER_AVATAR_RADIUS/2)
                .setY(baseY);

        int textGap = 110;
        pseudo = entityModule.createText(this.model.getNicknameToken())
                .setAnchor(0.5)
                .setFillColor(0xffffff)
                .setFontSize(60)
                .setStrokeColor(0x000000)
                .setStrokeThickness(4.0)
                .setX(855/2)
                .setY(baseY + textGap)
                .setFillColor(getTurnColor(playerIndex));

        gold = this.entityModule.createText("")
                .setAnchor(0.5)
                .setFillColor(0xffffff)
                .setFontSize(40)
                .setStrokeColor(0x000000)
                .setStrokeThickness(4.0)
                .setX(855/2)
                .setY(baseY + textGap + 70);

        income = this.entityModule.createText("")
                .setAnchor(0.5)
                .setFillColor(0xffffff)
                .setFontSize(40)
                .setStrokeColor(0x000000)
                .setStrokeThickness(4.0)
                .setX(855/2)
                .setY(baseY + textGap + 70 + 50);

        group = entityModule.createGroup()
                .setScale(1)
                .setX(0)
                .setY(0);
        group.add(background, avatar, pseudo, gold, income);
    }
    
    public void updateView() {
        this.gold.setText("Gold: " + goldModel.toString());
        if (incomeModel.intValue() > 0) {
            this.income.setText("Income: +" + incomeModel.toString());
        } else {
            this.income.setText("Income: " + incomeModel.toString());
        }
    }

    public Entity getEntity() {
        return group;
    }

}
