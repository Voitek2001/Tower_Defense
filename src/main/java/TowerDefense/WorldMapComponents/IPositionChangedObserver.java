package TowerDefense.WorldMapComponents;

import TowerDefense.Vector2d;
import TowerDefense.WorldMapElements.AbstractMoveableMapElements;

public interface IPositionChangedObserver {
    void positionChanged(AbstractMoveableMapElements element, Vector2d oldPosition, Vector2d newPosition);
}
