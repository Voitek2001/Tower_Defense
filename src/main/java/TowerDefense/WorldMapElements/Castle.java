package TowerDefense.WorldMapElements;


import TowerDefense.Vector2d;
import TowerDefense.WorldMapComponents.WorldMap;

public class Castle extends AbstractMapElementsWithHealth implements IMapElement {

    public Castle(Vector2d position, WorldMap map, int initHP) {
        this.position = position;
        this.map = map;
        this.status = ElementStatus.ALIVE;
        this.HP = this.maxHP = initHP;
    }


    @Override
    public String getPathToImage() {
        return "castle.png";
    }
}
