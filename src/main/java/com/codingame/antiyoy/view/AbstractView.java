package com.codingame.antiyoy.view;

import java.util.Observable;
import java.util.Observer;

import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.tooltip.TooltipModule;

public abstract class AbstractView implements Observer {
    protected GraphicEntityModule entityModule;
    protected TooltipModule tooltipModule;
    private boolean disposable = false;

    public AbstractView(GraphicEntityModule entityManager, TooltipModule tooltipModule) {
        this.entityModule = entityManager;
        this.tooltipModule = tooltipModule;
    }

    public abstract void updateView();

    public boolean isDisposable() {
        return disposable;
    }

    public void doDispose() {
        // System.err.println("Do dispose");
        disposable = true;
    }

    public void update(Observable observable, Object arg) {}
}
