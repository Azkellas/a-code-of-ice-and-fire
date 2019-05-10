package com.codingame.antiyoy.view;

import com.codingame.gameengine.module.entities.Entity;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;

import com.codingame.antiyoy.Building;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import static com.codingame.antiyoy.view.Constants.*;
import static com.codingame.antiyoy.Constants.*;
import com.codingame.gameengine.module.entities.Sprite;

import java.util.Observable;

public class BuildingView extends AbstractView {

    private Group group;
    private Building model;
    private Sprite sprite;

    public BuildingView(GraphicEntityModule entityModule, TooltipModule tooltipModule, Building building) {
        super(entityModule, tooltipModule);
        this.model = building;

        createBuildingView();
    }

    private String getTypeString(BUILDING_TYPE type) {
        if (type == BUILDING_TYPE.HQ)
            return "HQ";
        if (type == BUILDING_TYPE.MINE)
            return "M";
        if (type == BUILDING_TYPE.TOWER)
            return "T";
        return "o";
    }

    private void createBuildingView() {
        String buildingString = TYPE_TO_STRING(model.getType());
        String owner = model.getOwner() == 1 ? "BLUE" : "RED";
        String spriteName = buildingString + "_" + owner + ".png";
        sprite = this.entityModule.createSprite()
                .setImage(spriteName)
                .setX( - CELL_SIZE/2)
                .setY( - CELL_SIZE/2)
                .setBaseHeight(CELL_SIZE)
                .setBaseWidth(CELL_SIZE);

        group = entityModule.createGroup()
                .setScale(1)
                .setX(model.getX() * CELL_SIZE + (CELL_SIZE-2)/2)
                .setY(model.getY() * CELL_SIZE + (CELL_SIZE-2)/2)
                .setZIndex(10);


        group.add(sprite);

        tooltipModule.setTooltipText(group,  TYPE_TO_STRING(model.getType()) + "\nx: " + model.getX() + "\ny: " + model.getY());

    }
//

    public void updateView() {
        if (this.isDisposable())
            group.setVisible(false);
    }

    public Entity getEntity() {
        return group;
    }
}