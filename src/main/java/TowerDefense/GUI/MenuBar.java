package TowerDefense.GUI;

import TowerDefense.Simulation.SimulationEngine;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.LinkedList;
import java.util.List;

public class MenuBar {

    private final int towerCost;
    private final int wallCost;
    private Thread engineThread;
    private final SimulationEngine engine;
    private final List<IElementToPlaceObserver> elementToPlaceObservers = new LinkedList<>();
    private VBox rightPane;


    public MenuBar(SimulationEngine engine,
                   int initGold, int towerCost, int wallCost) {
        this.engine = engine;
        this.towerCost = towerCost;
        this.wallCost = wallCost;
        createRightPane(initGold);

    }

    public void createRightPane(int currentGold) {

        VBox rightPane = new VBox();

        Text text = new Text();
        text.setText("Current gold: " + currentGold);

        Button button = new Button("Run");
        button.setOnAction(event -> {

            this.engineThread = new Thread(this.engine);
            this.engineThread.start();

        });
        button.setMinWidth(20);
        button.setMinHeight(20);

        Button towerButton = new Button("Tower\n Cost: " + this.towerCost);
        towerButton.setOnAction(event -> selectedElementChanged(ObjectsToPlace.TOWER));
        Button wallButton = new Button("Wall\n Cost: " + this.wallCost);
        wallButton.setOnAction(event -> selectedElementChanged(ObjectsToPlace.WALL));
        rightPane.getChildren().addAll(text, button, towerButton, wallButton);
        rightPane.setAlignment(Pos.CENTER);
        rightPane.setSpacing(15);
        this.rightPane = rightPane;
    }

    public void addSelectedElementObserver(IElementToPlaceObserver observer) {
        this.elementToPlaceObservers.add(observer);
    }

    private void selectedElementChanged(ObjectsToPlace toPlace) {
        for (IElementToPlaceObserver observer: this.elementToPlaceObservers) {
            observer.selectedElementChanged(toPlace);
        }
    }

    public VBox getRightPane() {
        return this.rightPane;
    }


}
