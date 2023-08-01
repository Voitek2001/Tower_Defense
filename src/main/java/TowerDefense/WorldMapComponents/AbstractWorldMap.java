package TowerDefense.WorldMapComponents;

import TowerDefense.Vector2d;
import TowerDefense.WorldMapElements.*;
import TowerDefense.WorldMapElements.EnemyUtils.Enemy;

import java.util.*;

public class AbstractWorldMap implements IHealthObserver, IPositionChangedObserver, IStatusObserver, IWorldMap {

    private final List<Enemy> enemyList = new ArrayList<>();
    private final HashMap<Vector2d, AbstractMapElements> objectsPosition = new HashMap<>();
    private final List<Tower> towerList = new LinkedList<>();
    private final Bound bounds;

    public AbstractWorldMap(int size) {
        this.bounds = new Bound(new Vector2d(0, 0), new Vector2d(size, size));
    }

    @Override
    public void placeAt(AbstractMapElements element) throws IllegalArgumentException {
        Vector2d elementPos = element.getPosition();
        if (this.objectsPosition.containsKey(elementPos)) {
            throw new IllegalArgumentException("Obecne pole jest zajęte nie można na nim postawić kolejnego obiektu\"");
        }
        // if tower is placed on enemies they are immediately killed
        for (AbstractMoveableMapElements enemy: this.enemyList) {
            if (enemy.getPosition().equals(elementPos)) {
                enemy.setStatus(ElementStatus.DEAD);
            }
        }
//        this.enemyList.removeIf(enemy -> enemy.getStatus().equals(ElementStatus.DEAD));
        this.objectsPosition.put(elementPos, element);
        if (element instanceof AbstractMapElementsWithHealth) {
            ((AbstractMapElementsWithHealth) element).addHealthObserver(this);
            ((AbstractMapElementsWithHealth) element).addStatusObserver(this);
        }
    }
    @Override
    public Optional<IMapElement> objectAt(Vector2d position) {
        if (this.objectsPosition.containsKey(position)) {
            return Optional.of(this.objectsPosition.get(position));
        }
        for(Enemy enemy: this.enemyList) {
            if (enemy.getPosition().equals(position)){
                return Optional.of(enemy);
            }
        }
        // TODO fix problem with types
        //return this.enemyList.stream().filter(enemy -> enemy.getPosition().equals(position)).findFirst();
        return Optional.empty();
    }

    @Override
    public void changeHealth(AbstractMapElementsWithHealth element, int oldHealth, int newHealth) {

    }

    @Override
    public void positionChanged(AbstractMoveableMapElements element, Vector2d oldPosition, Vector2d newPosition) {

    }
    public void placeTowerAt(Tower tower) {
        placeAt(tower);
        towerList.add(tower);
    }

    public void placeEnemy(Enemy enemy) {
        this.enemyList.add(enemy);
        enemy.addHealthObserver(this);
        enemy.addStatusObserver(this);
        enemy.addPositionObserver(this);
    }


    public Bound getBounds() {
        return bounds;
    }

    public Vector2d getCastlePos() {
        return new Vector2d(this.bounds.upperRight().x() / 2, this.bounds.upperRight().y() / 2);
    }

    public List<Enemy> getEnemyList() {
        return this.enemyList;
    }

    public boolean isOccupiedByWall(Vector2d position) {
        return this.objectsPosition.get(position) instanceof Wall;
    }

    @Override
    public void die(AbstractMapElementsWithHealth element) {
        this.objectsPosition.remove(element.getPosition());
        if (element instanceof Enemy) {
            element.setStatus(ElementStatus.DEAD);
        }
    }

    public List<Tower> getTowerList() {
        return this.towerList;
    }

    public HashMap<Vector2d, AbstractMapElements> getObjectsPosition() {
        return objectsPosition;
    }

}
