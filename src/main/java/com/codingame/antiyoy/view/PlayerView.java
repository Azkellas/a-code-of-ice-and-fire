package com.codingame.antiyoy.view;

import com.codingame.game.Player;


import com.codingame.antiyoy.GameState;

import com.codingame.gameengine.module.entities.Entity;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.entities.Text;

import java.util.concurrent.atomic.AtomicInteger;

import static com.codingame.antiyoy.view.Constants.*;

public class PlayerView extends AbstractView {
    private Group group;

    public Player model;
    private Sprite avatar;
    private Text gold;
    private Text pseudo;
    AtomicInteger goldModel;

    public PlayerView(GraphicEntityModule entityModule, Player player, GameState gameState) {
        super(entityModule);
        this.model = player;
        this.goldModel = gameState.getAtomicGold(player.getIndex());
        createPlayerView();
    }

    public void createPlayerView() {
        int playerIndex = this.model.getIndex();

        avatar = entityModule.createSprite()
                .setAnchor(0.5)
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
                .setX(0)
                .setY(100);

        gold = this.entityModule.createText("")
                .setAnchor(0.5)
                .setFillColor(0xffffff)
                .setFontSize(40)
                .setStrokeColor(0x000000)
                .setStrokeThickness(4.0)
                .setX(0)
                .setY(200);

        group = entityModule.createGroup()
                .setScale(1)
                .setX(LEFT_PANEL_WIDTH / 2)
                .setY(playerIndex * 500 + 200);
        group.add(avatar, pseudo, gold);
    }
    
    public void updateView() {
        this.gold.setText(goldModel.toString());
    }

    public Entity getEntity() {
        return group;
    }

}
