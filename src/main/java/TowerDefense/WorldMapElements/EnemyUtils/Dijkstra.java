package TowerDefense.WorldMapElements.EnemyUtils;

import TowerDefense.Vector2d;
import TowerDefense.WorldMapComponents.Bound;
import TowerDefense.WorldMapElements.AbstractMapElements;

import java.util.*;

import static TowerDefense.WorldMapElements.EnemyUtils.CommonUtils.isOccupied;
import static TowerDefense.WorldMapElements.EnemyUtils.CommonUtils.isSafe;


public class Dijkstra {
    public static Optional<List<Vector2d>> findShortestPath(HashMap<Vector2d, AbstractMapElements> objectsPosition, Bound bounds,
                                                  Vector2d startPos, Vector2d endPos) {
        List<Vector2d> path = new LinkedList<>();
        MoveDirection[] movesDirections = MoveDirection.values();

        int size = bounds.upperRight().x();
        Vector2d[][] parent = new Vector2d[size][size];
        for(Vector2d[] row: parent) {
            Arrays.fill(row, new Vector2d(-1, -1));
        }
        int[][] dist = new int[size][size];
        for (int[] row: dist) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        boolean[][] visited = new boolean[size][size];

        dist[startPos.x()][startPos.y()] = 0;

        int currMin;
        Vector2d uPos = new Vector2d(-1, -1), neighPos;
        for (int a = 0; a < size * size; a++) {
            currMin = Integer.MAX_VALUE;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (!visited[i][j] && dist[i][j] < currMin) {
                        uPos = new Vector2d(i, j);
                        currMin = dist[i][j];
                    }
                }
            }

            for (MoveDirection move : movesDirections) {
                neighPos = uPos.add(move.getDirection());
                if (isSafe(neighPos, bounds) &&
                        !isOccupied(neighPos, objectsPosition) &&
                        !visited[neighPos.x()][neighPos.y()] &&
                        dist[neighPos.x()][neighPos.y()] > dist[uPos.x()][uPos.y()] + 1
                    ) {
                    dist[neighPos.x()][neighPos.y()] = dist[uPos.x()][uPos.y()] + 1;
                    parent[neighPos.x()][neighPos.y()] = uPos;
                }
            }
            visited[uPos.x()][uPos.y()] = true;
        }
        if(!visited[endPos.x()][endPos.y()]) {
            return Optional.empty();
        }
        endPos = parent[endPos.x()][endPos.y()];
        while (!endPos.equals(new Vector2d(-1, -1))) {
            path.add(endPos);
            endPos = parent[endPos.x()][endPos.y()];
        }
        Collections.reverse(path);
        return Optional.of(path);
    }

}

