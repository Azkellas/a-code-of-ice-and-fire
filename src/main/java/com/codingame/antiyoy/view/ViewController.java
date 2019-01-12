package com.codingame.antiyoy.view;

import com.codingame.game.Player;
import com.codingame.game.Referee;
import static com.codingame.antiyoy.Constants.*;
import static com.codingame.antiyoy.view.Constants.*;

import com.codingame.gameengine.module.entities.GraphicEntityModule;

import java.util.ArrayList;
import java.util.List;

import com.codingame.antiyoy.Cell;
import com.codingame.antiyoy.Unit;
import com.codingame.antiyoy.GameState;


public class ViewController {
    private List<AbstractView> views = new ArrayList<>();
    private GraphicEntityModule entityModule;

    private GameStateView gameStateView;

    private List<PlayerView> playerViews;

    public ViewController(GraphicEntityModule entityModule, List<Player> players, GameState gameState) {
        this.entityModule = entityModule;

        this.gameStateView = new GameStateView(entityModule);

        this.playerViews= new ArrayList<>();
        for (Player player : players)
            this.playerViews.add(new PlayerView(this.entityModule, player, gameState));

        initView();
    }

    private void initView() {
        for (PlayerView playerView : playerViews)
            this.views.add(playerView);
    }

    public void update() {
        gameStateView.updateView();

        for(AbstractView view : views) {
            view.updateView();
        }
        disposeViews();
    }

    private void disposeViews() {
        views.removeIf(view -> view.isDisposable());
    }


    public void createCellView(Cell cell) {
        this.views.add(this.gameStateView.createCellView(cell));
    }
    public void createUnitView(Unit unit) { this.views.add(this.gameStateView.createUnitView(unit)); }
//
//    public void createCardDeckView(Player player) {
//        CardDeckView deckView = new CardDeckView(entityModule, player.getPlayer());
//        views.add(deckView);
//    }
//
//    public void createTileView(TileModel tile) {
//        views.add(board.createTileView(tile));
//    }
//
//    public void createPlayerView(Player player) {
//        //create view with player name and avatar
//        views.add(new PlayerTextView(entityModule, player) );
//        views.add(board.createPlayerView(player));
//    }
//
//    public void createTextView() {
//        TurnTextView textView = new TurnTextView(entityModule);
//        views.add(textView);
//    }
}