package TowerDefense.WorldMapElements.EnemyUtils;

import TowerDefense.Simulation.EnemyActionStatus;
import TowerDefense.Vector2d;
import TowerDefense.WorldMapComponents.AbstractWorldMap;
import TowerDefense.Simulation.IDGenerator;
import TowerDefense.WorldMapElements.AbstractMoveableMapElements;
import TowerDefense.WorldMapElements.ElementStatus;
import TowerDefense.WorldMapElements.IMapElement;

import java.util.Optional;

public class Enemy extends AbstractMoveableMapElements implements IMapElement {

    private final int enemyID = IDGenerator.currId.getAndIncrement();
    private final EnemyPathIntuition enemyIntuition;
    private final int attackPower;
    private EnemyActionStatus enemyActionStatus;


    public Enemy(Vector2d position, AbstractWorldMap map, int attackPower, int initHP) {
        this.position = position;
        this.map = map;
        this.attackPower = attackPower;
        this.enemyIntuition = new EnemyPathIntuition(this.map.getObjectsPosition(), this.map.getBounds(), this.position, this.map.getCastlePos());
        this.HP = this.maxHP = initHP;
        this.status = ElementStatus.ALIVE;
    }

    public Vector2d prepNextMove() {
        Optional<Vector2d> possNextPos = this.enemyIntuition.getNextStep();
        if (possNextPos.isEmpty()) { // enemy should hit castle
            this.enemyActionStatus = EnemyActionStatus.DESTROYINGCASTLE;
            return this.map.getCastlePos();
        }
        if (this.map.isOccupiedByWall(possNextPos.get())) {
            this.enemyActionStatus = EnemyActionStatus.DESTROYINGWALL;
            return possNextPos.get();
        }
        this.enemyActionStatus = EnemyActionStatus.WALKINGTOCASTLE;
        return possNextPos.get();
    }

    public boolean tryToFindNewPathUsingIntuition() {
        return this.enemyIntuition.findPath(this.map.getObjectsPosition(), this.map.getBounds(), this.position, this.map.getCastlePos());
    }

    @Override
    public String getPathToImage() {
        return "enemy.png";
    }

    public int getAttackPower() {
        return attackPower;
    }

    public EnemyActionStatus getEnemyActionStatus() {
        return enemyActionStatus;
    }


}
