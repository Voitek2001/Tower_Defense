package TowerDefense.WorldMapComponents;

import TowerDefense.Vector2d;
import TowerDefense.WorldMapElements.AbstractMapElements;
import TowerDefense.WorldMapElements.IMapElement;

import java.util.Optional;

public interface IWorldMap {
    void placeAt(AbstractMapElements element);

    Optional<IMapElement> objectAt(Vector2d position);
}
