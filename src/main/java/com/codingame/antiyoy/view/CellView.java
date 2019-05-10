package com.codingame.antiyoy.view;

import com.codingame.gameengine.module.entities.*;

// import org.graalvm.compiler.core.common.type.ArithmeticOpTable;

import com.codingame.antiyoy.Cell;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import static com.codingame.antiyoy.view.Constants.*;
import static com.codingame.antiyoy.Constants.*;

import java.util.Observable;

public class CellView extends AbstractView {

    private Group group;
    private Rectangle decors;
    private Text type;
    private Cell model;
    private Sprite sprite;
    private Sprite neutralMine;
    private String spriteName;
    public CellView(GraphicEntityModule entityModule, TooltipModule tooltipModule, Cell cell) {
        super(entityModule, tooltipModule);
        this.model = cell;

        createCellView();
    }

    private void createCellView() {
        this.neutralMine = this.entityModule.createSprite()
                .setImage("MINE_NEUTRAL.png")
                .setBaseHeight(CELL_SIZE)
                .setBaseWidth(CELL_SIZE)
                .setZIndex(5);

        this.spriteName = "CELL_VOID.png";
        this.sprite = this.entityModule.createSprite()
                .setImage(spriteName)
                .setBaseHeight(CELL_SIZE)
                .setBaseWidth(CELL_SIZE)
                .setZIndex(1);


        group = entityModule.createGroup()
                .setScale(1)
                .setX(model.getX() * CELL_SIZE)
                .setY(model.getY() * CELL_SIZE);

        this.updateSprite();

        group.add(this.sprite);

        if (model.getOwner() != VOID) {
            tooltipModule.setTooltipText(group, "x: " + model.getX() + "\ny: " + model.getY());
        }

        if (model.isMineSpot()) {
            tooltipModule.setTooltipText(group, "MINE SPOT\n" + "x: " + model.getX() + "\ny: " + model.getY());
            group.add(neutralMine);
        } else {
            this.neutralMine.setVisible(false);
        }
    }

    private void updateSprite() {
        String owner;
        switch (model.getOwner()) {
            case -2:
                owner = "VOID";
                break;
            case -1:
                owner = "NEUTRAL";
                break;
            case 0:
                owner = "RED";
                break;
            case 1:
                owner = "BLUE";
                break;
            default:
                owner = "VOID";
                break;
        }
        String active = model.isActive() ? "ACTIVE" : "INACTIVE";
        if (model.isProtected()) {
            active = "PROTECTED";
        }
        String newSpriteName = "CELL_" + owner;
        if (model.getOwner() >= 0) {
            newSpriteName += "_" + active;
        }
        newSpriteName += ".png";

        if (newSpriteName.equals(spriteName)) {
            return;
        }
        this.spriteName = newSpriteName;
        this.sprite.setImage(spriteName);
        this.group.setZIndex( active.equals("PROTECTED") ? 3 : 1);
    }

    public void updateView() {
        this.updateSprite();
    }

    public Entity getEntity() {
        return group;
    }
}