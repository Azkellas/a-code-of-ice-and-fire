package com.codingame.antiyoy.view;

import com.codingame.gameengine.module.entities.Entity;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Circle;
import com.codingame.gameengine.module.entities.Text;
import com.codingame.gameengine.module.entities.Sprite;

import com.codingame.antiyoy.Unit;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import static com.codingame.antiyoy.view.Constants.*;

import java.util.Observable;

public class UnitView extends AbstractView {

    private Group group;
    private Unit model;
    private Sprite sprite;
    public UnitView(GraphicEntityModule entityModule, TooltipModule tooltipModule, Unit unit) {
        super(entityModule, tooltipModule);
        this.model = unit;

        createUnitView();
    }

    private void createUnitView() {
        String owner = model.getOwner() == 1 ? "BLUE" : "RED";
        String level = String.valueOf(model.getLevel());
        String spriteName = "UNIT_" + level + "_" + owner + ".png";
        sprite = this.entityModule.createSprite()
                .setImage(spriteName)
                .setX( - CELL_SIZE/2)
                .setY( - CELL_SIZE/2)
                .setBaseHeight(CELL_SIZE)
                .setBaseWidth(CELL_SIZE);

        group = entityModule.createGroup()
                .setScale(1)
                .setZIndex(10);
        group.add(sprite);

        tooltipModule.setTooltipText(group,  "id: " + model.getId() + "\nlevel: " + model.getLevel() + "\nx: " + model.getX() + "\ny: " + model.getY());

    }
//

    public void replace(Unit unit) {
        this.doReuse();
        this.group.setVisible(true);
        this.model = unit;
        tooltipModule.setTooltipText(group,  "id: " + model.getId() + "\nlevel: " + model.getLevel() + "\nx: " + model.getX() + "\ny: " + model.getY());
    }

    public void updateView() {
        if (this.isDisposable())
            group.setVisible(false);
        int x = model.getX() * CELL_SIZE + (CELL_SIZE-2)/2;
        int y = model.getY() * CELL_SIZE + (CELL_SIZE-2)/2;
        if (x != group.getX() || y != group.getY()) {
            tooltipModule.setTooltipText(group,  "id: " + model.getId() + "\nlevel: " + model.getLevel() + "\nx: " + model.getX() + "\ny: " + model.getY());
            group.setX(x).setY(y);
        }

    }

    public Entity getEntity() {
        return group;
    }

    public Unit getModel() { return model; }
}