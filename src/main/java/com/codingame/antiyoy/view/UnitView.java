package com.codingame.antiyoy.view;

import com.codingame.gameengine.module.entities.Entity;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Circle;
import com.codingame.gameengine.module.entities.Text;

import com.codingame.antiyoy.Unit;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import static com.codingame.antiyoy.view.Constants.*;

import java.util.Observable;

public class UnitView extends AbstractView {

    private Group group;
    private Circle decors;
    private Text level;
    private Unit model;

    public UnitView(GraphicEntityModule entityModule, TooltipModule tooltipModule, Unit unit) {
        super(entityModule, tooltipModule);
        this.model = unit;

        createUnitView();
    }

    private void createUnitView() {
        decors = entityModule.createCircle()
                .setRadius(UNIT_RADIUS)
                .setFillColor(getPlayerUnitColor(this.model.getOwner()))
                .setZIndex(2);
        level = this.entityModule.createText(String.valueOf(model.getLevel()))
                .setAnchor(0.5)
                .setFillColor(0xffffff)
                .setFontSize(30)
                .setStrokeColor(0xffffff)
                .setStrokeThickness(2.0)
                .setX(0)
                .setY(0)
                .setZIndex(3);

        group = entityModule.createGroup()
                .setScale(1);
        group.add(decors, level);

        tooltipModule.setTooltipText(group,  "id: " + model.getId() + "\nlevel: " + model.getLevel() + "\nx: " + model.getX() + "\ny: " + model.getY());

    }
//

    public void updateView() {
        if (this.isDisposable())
            group.setVisible(false);
        int x = model.getX() * CELL_SIZE + (CELL_SIZE-2)/2;
        int y = model.getY() * CELL_SIZE + (CELL_SIZE-2)/2;
        if (x != group.getX() || y != group.getY())
            group.setX(x).setY(y);
    }

    public Entity getEntity() {
        return group;
    }
}