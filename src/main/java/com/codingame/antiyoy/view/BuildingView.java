package com.codingame.antiyoy.view;

import com.codingame.gameengine.module.entities.Entity;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Rectangle;
import com.codingame.gameengine.module.entities.Text;

import com.codingame.antiyoy.Building;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import static com.codingame.antiyoy.view.Constants.*;
import static com.codingame.antiyoy.Constants.*;

import java.util.Observable;

public class BuildingView extends AbstractView {

    private Group group;
    private Rectangle decors;
    private Text type;
    private Building model;

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
        decors = entityModule.createRectangle()
                .setWidth((int)(CELL_SIZE / 1.5))
                .setHeight((int)(CELL_SIZE / 1.5))
                .setX( - (CELL_SIZE-2)/3)
                .setY( - (CELL_SIZE-2)/3)
                .setFillColor(getPlayerUnitColor(this.model.getOwner()))
                .setZIndex(2);
        type = this.entityModule.createText(getTypeString(model.getType()))
                .setAnchor(0.5)
                .setFillColor(0xffffff)
                .setFontSize(20)
                .setStrokeColor(0xffffff)
                .setStrokeThickness(4.0)
                .setX(0)
                .setY(0)
                .setZIndex(3);

        group = entityModule.createGroup()
                .setScale(1)
                .setX(model.getX() * CELL_SIZE + (CELL_SIZE-2)/2)
                .setY(model.getY() * CELL_SIZE + (CELL_SIZE-2)/2);
        group.add(decors, type);
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