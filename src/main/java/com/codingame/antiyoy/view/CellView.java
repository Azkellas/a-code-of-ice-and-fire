package com.codingame.antiyoy.view;

import com.codingame.gameengine.module.entities.Entity;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Rectangle;
// import org.graalvm.compiler.core.common.type.ArithmeticOpTable;

import com.codingame.antiyoy.Cell;
import static com.codingame.antiyoy.view.Constants.*;

import java.util.Observable;

public class CellView extends AbstractView {

    private Group group;
    private Rectangle decors;

    private Cell model;

    public CellView(GraphicEntityModule entityModule, Cell cell) {
        super(entityModule);
        this.model = cell;

        createCellView();
    }

    private void createCellView() {
        decors = entityModule.createRectangle()
                .setHeight(CELL_SIZE -2)
                .setWidth(CELL_SIZE -2)
                .setFillColor(getPlayerCellColor(this.model.getOwner()))
                .setLineColor(255)
                .setZIndex(1);
        group = entityModule.createGroup()
                .setScale(1);
        group.add(decors);
    }
//
//    private void addItem() {
//        if (CellItem != null) {
//            itemGroup = entityModule.createGroup().setZIndex(2);
//            itemBackground = entityModule.createCircle()
//                    .setZIndex(0)
//                    .setScale(0.3)
//                    .setAlpha(0.7);
//            itemGroup.add(itemBackground);
//            String spritePath = String.format("item_%s_%d", CellItem.getName(), CellItem.getPlayerId());
//            itemGroup.add(entityModule.createSprite()
//                    .setImage(spritePath)
//                    .setAnchor(0.5)
//                    .setZIndex(1));
//            group.add(itemGroup);
//        }
//    }
//
//    private void removeItem() {
//        itemGroup.setScale(0);
//        entityModule.commitEntityState(1, itemGroup);
//        group.remove(this.itemGroup);
//    }

    public void updateView(){
        group.setX(model.getX() * CELL_SIZE).setY(model.getY() * CELL_SIZE);
        decors.setFillColor(getPlayerCellColor(this.model.getOwner()));
    }

    public Entity getEntity() {
        return group;
    }
}