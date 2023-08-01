package TowerDefense.WorldMapElements.EnemyUtils;

import TowerDefense.Vector2d;

public enum MoveDirection {
    NORTH,
    EAST,
    WEST,
    SOUTH;

    public Vector2d getDirection() {
        return switch (this) {
            case NORTH -> new Vector2d(0, 1);
            case EAST -> new Vector2d(1, 0);
            case WEST -> new Vector2d(-1, 0);
            case SOUTH -> new Vector2d(0, -1);
        };
    }
}
