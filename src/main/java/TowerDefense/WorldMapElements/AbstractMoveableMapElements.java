package TowerDefense.WorldMapElements;

import TowerDefense.Vector2d;
import TowerDefense.WorldMapComponents.IPositionChangedObserver;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractMoveableMapElements extends AbstractMapElementsWithHealth {
    protected List<IPositionChangedObserver> positionObservers = new LinkedList<>();

    public void addPositionObserver(IPositionChangedObserver observer) {
        this.positionObservers.add(observer);
    }
    public void removePositionObserver(IPositionChangedObserver observer) {
        this.positionObservers.remove(observer);
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        for(IPositionChangedObserver observer : this.positionObservers) {
            observer.positionChanged(this, oldPosition, newPosition);
        }
        this.position = newPosition;
    }


}
