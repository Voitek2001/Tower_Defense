package TowerDefense.WorldMapElements;

import TowerDefense.Vector2d;

import TowerDefense.WorldMapComponents.WorldMap;


public class Tower extends AbstractMapElements implements IMapElement {


    private final int attackPower;
    private final int attackRange;

    public Tower(Vector2d position, WorldMap map, int attackPower, int attackRange) {
        this.position = position;
        this.map = map;
        this.attackPower = attackPower;
        this.attackRange = attackRange;
    }

    @Override
    public String toString() {
        return "T";
    }

    @Override
    public String getPathToImage() {
        return "tower.png";
    }

    @Override
    public int getHP() {
        return 0;
    }

    @Override
    public int getMaxHP() {
        return 0;
    }

    public boolean checkIfPositionInRange(Vector2d position) {
        return position.getManhattanDist(this.position) < this.attackRange;
    }
    public int getAttackPower() {
        return attackPower;
    }

}