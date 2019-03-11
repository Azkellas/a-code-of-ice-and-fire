package com.codingame.antiyoy.view;

import com.codingame.game.Player;
import com.codingame.game.Referee;
import static com.codingame.antiyoy.Constants.*;
import static com.codingame.antiyoy.view.Constants.*;

import com.codingame.gameengine.module.entities.GraphicEntityModule;

import java.util.ArrayList;
import java.util.List;

import com.codingame.antiyoy.Cell;
import com.codingame.antiyoy.Building;
import com.codingame.antiyoy.Unit;
import com.codingame.antiyoy.GameState;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import javax.tools.Tool;


public class ViewController {
    private List<AbstractView> views = new ArrayList<>();
    private GraphicEntityModule entityModule;
    private TooltipModule tooltipModule;

    private GameStateView gameStateView;

    private List<PlayerView> playerViews;

    public ViewController(GraphicEntityModule entityModule, TooltipModule tooltipModule, List<Player> players, GameState gameState) {
        this.entityModule = entityModule;
        this.tooltipModule = tooltipModule;

        this.gameStateView = new GameStateView(entityModule, tooltipModule, gameState);

        this.playerViews= new ArrayList<>();
        for (Player player : players)
            this.playerViews.add(new PlayerView(this.entityModule, this.tooltipModule, player, gameState));

        initView();
    }

    private void initView() {
        this.views.addAll(playerViews);
    }

    public void update() {
        gameStateView.updateView();

        for (AbstractView view : views) {
            view.updateView();
        }
        // System.err.println("Views: " + views.size());
        if (views.size() > 100)
            this.gameStateView.getModel().debugViews();
        disposeViews();
    }

    private void disposeViews() {
        int currSize = views.size();
        views.removeIf(view -> view.isDisposable());
        // System.err.println("Disposing " + currSize + " -> " + views.size());
    }


    public void createCellView(Cell cell) {
        this.views.add(this.gameStateView.createCellView(cell));
    }

    public void createUnitView(Unit unit) {
        UnitView view = this.gameStateView.createUnitView(unit);
        unit.setViewer(view);
        this.views.add(view);
    }

    public void createBuildingView(Building building) {
        BuildingView view = this.gameStateView.createBuildingView(building);
        building.setViewer(view);
        this.views.add(view);
    }
}