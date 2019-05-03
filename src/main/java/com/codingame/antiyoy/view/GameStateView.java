package com.codingame.antiyoy.view;

import com.codingame.game.Player;
import com.codingame.antiyoy.GameState;
import com.codingame.antiyoy.Cell;
import com.codingame.antiyoy.Unit;
import com.codingame.antiyoy.Building;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import java.util.*;

import static com.codingame.antiyoy.view.Constants.*;


public class GameStateView extends AbstractView {
    private Group group;

    List<CellView> cells;
    List<UnitView> units;
    List<BuildingView> buildings;

    GameState model;
    public GameStateView(GraphicEntityModule entityModule, TooltipModule tooltipModule, GameState gameState){
        super(entityModule, tooltipModule);

        this.model = gameState;
        cells = new ArrayList<>();
        units = new ArrayList<>();
        buildings = new ArrayList<>();

        this.group = this.entityModule.createGroup()
                .setX(GRID_X)
                .setY(GRID_Y)
                .setVisible(true)
                .setZIndex(0)
                .setScale(1);

    }

    public CellView createCellView(Cell cell) {
        CellView cellView = new CellView (entityModule, tooltipModule, cell);
        group.add(cellView.getEntity().setZIndex(1));
        cells.add(cellView);
        return cellView;
    }

    public UnitView createUnitView(Unit unit) {
        UnitView unitView= new UnitView(entityModule, tooltipModule, unit);
        group.add(unitView.getEntity().setZIndex(2));
        units.add(unitView);
        return unitView;
    }

    public BuildingView createBuildingView(Building building) {
        BuildingView buildingView = new BuildingView(entityModule, tooltipModule, building);
        group.add(buildingView.getEntity().setZIndex(2));
        buildings.add(buildingView);
        return buildingView;
    }

//    public PlayerView createPlayerView(Player player) {
//        PlayerView playerView = new PlayerView(entityModule, player, player.getPlayer());
//        group.add(playerView.getEntity().setZIndex(2));
//        players.add(playerView);
//        return playerView;
//    }


    public void updateView() {
    }

    public GameState getModel() { return this.model; }


    public void update(AbstractMap.SimpleEntry<String, Integer> action) {
    }


}
