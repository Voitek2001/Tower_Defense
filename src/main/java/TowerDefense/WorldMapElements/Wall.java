package TowerDefense.WorldMapElements;

import TowerDefense.Vector2d;
import TowerDefense.WorldMapComponents.WorldMap;

public class Wall extends AbstractMapElementsWithHealth implements IMapElement {


    public Wall(Vector2d position, WorldMap map, int initHP) {
        this.position = position;
        this.map = map;
        this.status = ElementStatus.ALIVE;
        this.HP = this.maxHP = initHP;
    }
    @Override
    public String getPathToImage() {
        return "wall2.png";
    }
}
