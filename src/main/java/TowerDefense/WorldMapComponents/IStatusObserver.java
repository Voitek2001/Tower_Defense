package TowerDefense.WorldMapComponents;

import TowerDefense.WorldMapElements.AbstractMapElements;
import TowerDefense.WorldMapElements.AbstractMapElementsWithHealth;
import TowerDefense.WorldMapElements.ElementStatus;
import TowerDefense.WorldMapElements.IMapElement;

public interface IStatusObserver {
    void die(AbstractMapElementsWithHealth element);
}
