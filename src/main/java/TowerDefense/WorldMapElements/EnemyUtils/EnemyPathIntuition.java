package TowerDefense.WorldMapElements.EnemyUtils;

import TowerDefense.Simulation.EnemyActionStatus;
import TowerDefense.Vector2d;
import TowerDefense.WorldMapComponents.Bound;
import TowerDefense.WorldMapElements.AbstractMapElements;

import java.util.*;

import static TowerDefense.WorldMapElements.EnemyUtils.CommonUtils.isSafe;

public class EnemyPathIntuition {

    private List<Vector2d> pathToCastle;
    private Iterator<Vector2d> pathIterator;
    // map gonna inform enemy intuition that enemy reach a wall, then there are 2 options
    // 1 if castle can be reached
    // 2 else
    public EnemyPathIntuition(HashMap<Vector2d, AbstractMapElements> objects, Bound bounds,
                              Vector2d enemyPos, Vector2d castlePos) {
        findPath(objects, bounds, enemyPos, castlePos);
    }

    public boolean findPath(HashMap<Vector2d, AbstractMapElements> objects, Bound bounds,
                         Vector2d enemyPos, Vector2d castlePos) {
        Optional<List<Vector2d>> possPath = Dijkstra.findShortestPath(objects, bounds, enemyPos, castlePos);
        possPath.ifPresentOrElse(
                (val) -> this.pathToCastle = val,
                () -> this.pathToCastle = getGreedyPath(bounds, enemyPos, castlePos));

        this.pathIterator = this.pathToCastle.iterator();
        return possPath.isPresent();
    }

    public List<Vector2d> getPathToCastle() {
        return this.pathToCastle;
    }

    // if can't reach castle try to destroy walls
    // there is a bug, if castle is surround by towers enemies can't destroy the towers and can't walk through.
    // so if bug appear enemy starts searching for new path using greedy algorithm, assume that can walk through towers.
    private List<Vector2d> getGreedyPath(Bound bounds, Vector2d enemyPos, Vector2d castlePos) {
        List<Vector2d> path = new LinkedList<>();
        Vector2d currPos = new Vector2d(enemyPos.x(), enemyPos.y());
        while (!currPos.equals(castlePos)) {
            for (MoveDirection move: MoveDirection.values()) {
                Vector2d possNewPos = currPos.add(move.getDirection());
                if (isSafe(possNewPos, bounds) && currPos.getManhattanDist(castlePos) > possNewPos.getManhattanDist(castlePos)) {
                    path.add(possNewPos);
                    currPos = possNewPos;
                    break;
                }
            }
        }
        path.remove(path.size() - 1);
        return path;
    }

    public Optional<Vector2d> getNextStep() {
        if (this.pathIterator.hasNext()) {
            return Optional.of(this.pathIterator.next());
        }
        return Optional.empty();
    }
}
