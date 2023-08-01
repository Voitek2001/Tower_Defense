package TowerDefense.WorldMapElements.EnemyUtils;

import TowerDefense.Vector2d;
import TowerDefense.WorldMapComponents.Bound;
import TowerDefense.WorldMapElements.AbstractMapElements;
import TowerDefense.WorldMapElements.Castle;

import java.util.HashMap;

public class CommonUtils {
    public static boolean isSafe(Vector2d pos, Bound bound) {
        return (pos.precedes(bound.upperRight()) && pos.follows(bound.lowerLeft()));
    }

    public static boolean isOccupied(Vector2d pos, HashMap<Vector2d, AbstractMapElements> objectsPosition) {
        if (objectsPosition.containsKey(pos) && objectsPosition.get(pos) instanceof Castle) {
            return false;
        }
        return objectsPosition.containsKey(pos);
    }
}
