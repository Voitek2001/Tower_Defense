package TowerDefense.Simulation;

import TowerDefense.GUI.App;
import TowerDefense.GUI.IRenderGridObserver;
import TowerDefense.Vector2d;
import TowerDefense.WorldMapComponents.WorldMap;
import TowerDefense.WorldMapElements.Castle;
import TowerDefense.WorldMapElements.ElementStatus;
import TowerDefense.WorldMapElements.Tower;
import TowerDefense.WorldMapElements.Wall;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SimulationEngine implements IEngine, Runnable{

    private final int moveDelay;
    private final WorldMap map;
    private final EnemySpawner enemySpawner;
    private final Castle castle;
    private final List<IRenderGridObserver> renderGridObservers = new ArrayList<>();



    public SimulationEngine(JSONObject simulationConfig, WorldMap map, int moveDelay, App app) {


        this.map = map;
        this.moveDelay = moveDelay;
        this.enemySpawner = new EnemySpawner(map, simulationConfig.getInt("enemiesDamage"), simulationConfig.getInt("enemiesHP"), app);
        //init castle here for more comfortable access
        this.castle = new Castle(new Vector2d(simulationConfig.getInt("numberOfElements") / 2,
                simulationConfig.getInt("numberOfElements") / 2), this.map, simulationConfig.getInt("castleHP"));
        this.map.placeAt(this.castle);
    }

    @Override
    public void run() {
        while (true) {
            // spawn new enemy
            this.enemySpawner.spawnEnemy();
            // move every moveable object
            simulateMoveOrAttackOfEnemy();
            shootToEnemyFromTowers();
            removeDeadEnemies();
            
            renderNewGrid();

            if (this.castle.getStatus().equals(ElementStatus.DEAD)) {
                break;

            }
            try {
                //noinspection BusyWait
                Thread.sleep(this.moveDelay);
            }
            catch (InterruptedException e) {
                return;
            }
        }
    }

    private void removeDeadEnemies() {
        this.map.getEnemyList().removeIf((enemy -> enemy.getStatus().equals(ElementStatus.DEAD)));
    }

    private void shootToEnemyFromTowers() {
        for (Tower tower: this.map.getTowerList()) {
            this.map.getEnemyList().forEach((enemy -> {
                if (tower.checkIfPositionInRange(enemy.getPosition())) {
                    enemy.changeHealth(tower.getAttackPower());
                }
            }));
        }
    }


    private void simulateMoveOrAttackOfEnemy() {
        this.map.getEnemyList().forEach((enemy) -> {
            Vector2d nextPos = enemy.prepNextMove();
            switch (enemy.getEnemyActionStatus()) {
                case DESTROYINGCASTLE -> this.castle.changeHealth(enemy.getAttackPower());
                case DESTROYINGWALL -> {
                    if (!enemy.tryToFindNewPathUsingIntuition()) {
                        Wall wall = (Wall) this.map.objectAt(nextPos).get();
                        wall.changeHealth(enemy.getAttackPower());
                    }
                }
                case WALKINGTOCASTLE -> enemy.positionChanged(enemy.getPosition(), nextPos);
            }

        });
    }

    void renderNewGrid() {
        for (IRenderGridObserver observer : this.renderGridObservers) {
            observer.renderNewGrid();
        }
    }

    public void addObserver(IRenderGridObserver observer) {
        this.renderGridObservers.add(observer);
    }

}
