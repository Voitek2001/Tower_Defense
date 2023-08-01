package TowerDefense.GUI;

import TowerDefense.Simulation.SimulationEngine;
import TowerDefense.Vector2d;
import TowerDefense.WorldMapComponents.IStatusObserver;
import TowerDefense.WorldMapComponents.WorldMap;
import TowerDefense.WorldMapElements.AbstractMapElementsWithHealth;
import TowerDefense.WorldMapElements.Tower;
import TowerDefense.WorldMapElements.Wall;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.json.JSONObject;
import java.io.FileNotFoundException;


public class App extends Application implements IRenderGridObserver, IElementToPlaceObserver, IStatusObserver {

    private JSONObject appConfig;
    private JSONObject simulationConfig;
    private ObjectsToPlace selectedElementToPlace;
    private WorldMap map;
    private GridPane grid;
    private Stage stage;
    private SimulationEngine engine;
    private int gold; // consider moving gold var to simulationEngine


    private MenuBar menuBar;
    @Override
    public void init() throws Exception {
        super.init();
        // load configuration settings from Json
        String pathToConfig = "src/main/java/TowerDefense/config.json";
        JsonReader config = new JsonReader(pathToConfig);

        // load simulation settings/cons from Json
        String objectToLoad = "SimulationConfig";
        this.simulationConfig = config.getConfig(objectToLoad);

        // load app setting/cons
        objectToLoad = "AppConfig";
        this.appConfig = config.getConfig(objectToLoad);

        this.map = new WorldMap(this.appConfig.getInt("numberOfElements"));


        this.engine = new SimulationEngine(this.simulationConfig, this.map, 3000, this);
        this.engine.addObserver(this);

        this.gold = this.simulationConfig.getInt("startGold");

    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        primaryStage.setTitle("Tower Defense");
        primaryStage.setOnCloseRequest(exit -> {
            Platform.exit();
            System.exit(0);
        });

        this.menuBar = new MenuBar(this.engine, this.gold,
                this.simulationConfig.getInt("towerCost"), this.simulationConfig.getInt("wallCost"));
        this.menuBar.addSelectedElementObserver(this);

        SplitPane splitPane = createSplitPane(createLeftGridPane(), this.menuBar.getRightPane());
        Scene scene = new Scene(splitPane, this.appConfig.getInt("widthOfScene"), this.appConfig.getInt("heightOfScene"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private SplitPane createSplitPane(GridPane leftPane, VBox rightPane) {
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(leftPane, rightPane);
        splitPane.setOrientation(Orientation.HORIZONTAL);
        splitPane.setDividerPositions(0.8);
        return splitPane;
    }


    private GridPane createLeftGridPane() throws FileNotFoundException {
        GridPane leftPane = new GridPane();
        Image image = ImageLoader.loadImage(this.appConfig.getString("resourcesPath") + "/" + "grassBackground.jpg");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(this.appConfig.getInt("widthOfScene"));
        imageView.setFitHeight(2*this.appConfig.getInt("heightOfScene"));
        leftPane.getChildren().add(imageView);
        this.grid = leftPane;
        renderGrid();
        leftPane.setOnMouseClicked(event -> {
            System.out.println("Kliknięto na współrzędne: x=" + event.getX() + ", y=" + event.getY());
            try {
                onClickEventHandler(event.getX(), event.getY());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        return leftPane;
    }

    private void onClickEventHandler(double x, double y) throws FileNotFoundException {
        int gridX, gridY;
        gridX = (int) (x / ((this.appConfig.getInt("widthOfScene") * (0.8)) /
                (this.appConfig.getInt("numberOfElements"))));
        gridY = (int) (this.appConfig.getInt("heightOfScene") - y) /
                ((this.appConfig.getInt("heightOfScene")) / (this.appConfig.getInt("numberOfElements")));

        try {
            switch (this.selectedElementToPlace) {
                case WALL -> {
                    if (this.gold >= this.simulationConfig.getInt("wallCost")) {
                        this.map.placeAt(new Wall(new Vector2d(gridX, gridY), this.map, this.simulationConfig.getInt("wallHP")));
                        this.gold -= this.simulationConfig.getInt("wallCost");
                    }
                }
                case TOWER -> {
                    if (this.gold >= this.simulationConfig.getInt("towerCost")) {
                        this.map.placeTowerAt(new Tower(new Vector2d(gridX, gridY), this.map, this.simulationConfig.getInt("damageFromTowers"), this.simulationConfig.getInt("towerRange")));
                        this.gold -= this.simulationConfig.getInt("towerCost");
                    }
                }
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Obecne pole jest zajęte nie można na nim postawić kolejnego obiektu");
        }
        clearAndGenerateNewGrid();

    }

    private void clearAndGenerateNewGrid() throws FileNotFoundException {
        this.grid.getChildren().retainAll(grid.getChildren().get(0));
        this.menuBar.createRightPane(this.gold);
        SplitPane splitPane = createSplitPane(createLeftGridPane(), this.menuBar.getRightPane());
        Scene scene = new Scene(splitPane, this.appConfig.getInt("widthOfScene"), this.appConfig.getInt("heightOfScene"));
        this.stage.setScene(scene);
    }

    public void renderNewGrid() {
        Platform.runLater(() -> {
            this.grid.getChildren().retainAll(grid.getChildren().get(0));
            SplitPane splitPane;
            try {
                this.menuBar.createRightPane(this.gold);
                splitPane = createSplitPane(createLeftGridPane(), this.menuBar.getRightPane());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            Scene scene = new Scene(splitPane, this.appConfig.getInt("widthOfScene"), this.appConfig.getInt("heightOfScene"));
            this.stage.setScene(scene);
        });
    }
    public void renderGrid() {

        Vector2d lowerLeft = new Vector2d(0, 0);
        Vector2d upperRight = new Vector2d(this.appConfig.getInt("numberOfElements"),
                                           this.appConfig.getInt("numberOfElements"));

        int horizontalSize, verticalSize;
        verticalSize = (this.appConfig.getInt("heightOfScene")) / (this.appConfig.getInt("numberOfElements"));
        horizontalSize = (int) ((this.appConfig.getInt("widthOfScene") * (0.8)) / (this.appConfig.getInt("numberOfElements")));
        int lowerLeftX = lowerLeft.x();
        int lowerLeftY = lowerLeft.y();
        int upperRightX = upperRight.x();
        int upperRightY = upperRight.y();
        for (int i = lowerLeftY; i < upperRightY; i++) {

            this.grid.getRowConstraints().add(new RowConstraints(verticalSize));
        }
        for (int i = lowerLeftX; i < upperRightX; i++) {

            this.grid.getColumnConstraints().add(new ColumnConstraints(horizontalSize));
        }

        for (int x = lowerLeftX; x < upperRightX; x++) {
            for (int y = lowerLeftY; y < upperRightY; y++) {
                Vector2d position = new Vector2d(x, y);
                int finalX = x;
                int finalY = y;
                this.map.objectAt(position).ifPresent(
                        (value) -> {
                            GuiElementBox guiElementBox = new GuiElementBox(value, this.appConfig.getString("resourcesPath"),
                                    60 , 60);
                            VBox guiElement = guiElementBox.getGUIElement();
                            GridPane.setHalignment(guiElement, HPos.CENTER);
                            this.grid.add(guiElement, finalX - lowerLeftX, upperRightY - finalY - 1, 1, 1);

                        }
                );

            }
        }

    }

    @Override
    public void selectedElementChanged(ObjectsToPlace objectsToPlace) {
        this.selectedElementToPlace = objectsToPlace;
    }


    @Override
    public void die(AbstractMapElementsWithHealth element) {
        this.gold += this.simulationConfig.getInt("enemyKillReward");
    }
}
