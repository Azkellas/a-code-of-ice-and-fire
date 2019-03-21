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
    private Sprite avatar;
    private Text gold;
    private Text pseudo;
    AtomicInteger goldModel;

    public PlayerView(GraphicEntityModule entityModule, TooltipModule tooltipModule, Player player, GameState gameState) {
        super(entityModule, tooltipModule);
        this.model = player;
        this.goldModel = gameState.getAtomicGold(player.getIndex());
        createPlayerView();
    }

    public void createPlayerView() {
        int playerIndex = this.model.getIndex();

        avatar = entityModule.createSprite()
                .setAnchor(0)
                .setBaseHeight(PLAYER_AVATAR_RADIUS)
                .setBaseWidth(PLAYER_AVATAR_RADIUS)
                .setImage(this.model.getAvatarToken())
                .setX(0)
                .setY(0);

        pseudo = entityModule.createText(this.model.getNicknameToken())
                .setAnchor(0.5)
                .setFillColor(0xffffff)
                .setFontSize(60)
                .setStrokeColor(0x000000)
                .setStrokeThickness(4.0)
                .setX(PLAYER_AVATAR_RADIUS / 2)
                .setY(PLAYER_AVATAR_RADIUS + 40);

        gold = this.entityModule.createText("")
                .setAnchor(0.5)
                .setFillColor(0xffffff)
                .setFontSize(40)
                .setStrokeColor(0x000000)
                .setStrokeThickness(4.0)
                .setX(PLAYER_AVATAR_RADIUS / 2)
                .setY(PLAYER_AVATAR_RADIUS + 40 + 50);

        group = entityModule.createGroup()
                .setScale(1)
                .setX(LEFT_PANEL_WIDTH / 2)
                .setY(playerIndex * (SCREEN_HEIGHT/2) + 100);
        group.add(avatar, pseudo, gold);
    }
    
    public void updateView() {
        this.gold.setText(goldModel.toString());
    }

    public Entity getEntity() {
        return group;
    }

}
