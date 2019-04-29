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
    private Rectangle protect;
    private Text type;
    private Cell model;

    public CellView(GraphicEntityModule entityModule, TooltipModule tooltipModule, Cell cell) {
        super(entityModule, tooltipModule);
        this.model = cell;

        createCellView();
    }

    private void createCellView() {
        decors = entityModule.createRectangle()
                .setHeight(CELL_SIZE -2)
                .setWidth(CELL_SIZE -2)
                .setFillColor(getPlayerCellColor(this.model.getOwner(), this.model.isActive()))
                .setLineColor(255)
                .setZIndex(1);
        protect = this.entityModule.createRectangle()
                .setHeight( (CELL_SIZE -2) / 5)
                .setWidth( (CELL_SIZE -2) / 5)
                .setFillColor(PROTECTED_COLOR)
                .setX((int)((CELL_SIZE-2)/2.5))
                .setY((int)((CELL_SIZE-2)/2.5))
                .setZIndex(2)
                .setAlpha(0);

        type = this.entityModule.createText("X")
                .setAnchor(0.5)
                .setFillColor(0xffffff)
                .setFontSize(20)
                .setStrokeColor(0xffffff)
                .setStrokeThickness(4.0)
                .setX((int)((CELL_SIZE-2)/2.5))
                .setY((int)((CELL_SIZE-2)/2.5))
                .setZIndex(3);


        group = entityModule.createGroup()
                .setScale(1)
                .setX(model.getX() * CELL_SIZE)
                .setY(model.getY() * CELL_SIZE);



        group.add(decors, protect);

        if (model.getOwner() != VOID) {
            tooltipModule.setTooltipText(group, "x: " + model.getX() + "\ny: " + model.getY());
        }

        if(model.isMineSpot()) {
            tooltipModule.setTooltipText(group, "MINE SPOT\n" + "x: " + model.getX() + "\ny: " + model.getY());
            group.add(type);
        }
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
        int cellColor = getPlayerCellColor(this.model.getOwner(), this.model.isActive());
        if (decors.getFillColor() != cellColor)
            decors.setFillColor(cellColor);
        boolean isProtected = this.model.isProtected();
        double alpha = isProtected ? 0.8 : 0.0;
        if (protect.getAlpha() != alpha)
            protect.setAlpha(alpha);
    }

    public Entity getEntity() {
        return group;
    }
}