package TowerDefense.WorldMapElements;

import TowerDefense.Vector2d;

public interface IMapElement {

    Vector2d getPosition();
    String getPathToImage();
    int getHP();
    int getMaxHP();

}
