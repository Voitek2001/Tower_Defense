package TowerDefense.Simulation;


import TowerDefense.GUI.App;
import TowerDefense.Vector2d;
import TowerDefense.WorldMapComponents.AbstractWorldMap;
import TowerDefense.WorldMapElements.EnemyUtils.Enemy;

import java.util.Random;


public class EnemySpawner {

    private final AbstractWorldMap map;
    private final int attackPowerForEnemy;
    private final int enemyHP;
    private final App app;

    public EnemySpawner(AbstractWorldMap map, int attackPowerForEnemy, int enemyHP, App app) {
        this.map = map;
        this.attackPowerForEnemy = attackPowerForEnemy;
        this.enemyHP = enemyHP;
        this.app = app;

    }

    public void spawnEnemy() {

        Vector2d newPosition = genNewPositionOnEdge();
        Enemy newEnemy = new Enemy(newPosition, this.map, this.attackPowerForEnemy, this.enemyHP);
        this.map.placeEnemy(newEnemy);
        newEnemy.addStatusObserver(this.app);
    }

    private Vector2d genNewPositionOnEdge() {
        int size = this.map.getBounds().upperRight().x();
        int whichEdge = new Random().nextInt(0, 4);

        return switch (whichEdge) {
            case 0 -> new Vector2d(0, new Random().nextInt(size - 1)); //left edge
            case 1 -> new Vector2d(new Random().nextInt(size - 1), size - 1); //upper edge
            case 2 -> new Vector2d(size - 1, new Random().nextInt(size - 1)); //right edge
            default -> new Vector2d(new Random().nextInt(size - 1), 0); //down edge
        };
    }
}
