package TowerDefense.WorldMapElements;
import TowerDefense.Vector2d;
import TowerDefense.WorldMapComponents.AbstractWorldMap;

public abstract class AbstractMapElements implements IMapElement{
    protected Vector2d position;
    protected AbstractWorldMap map;


    @Override
    public Vector2d getPosition() {
        return this.position;
    }
    public abstract String getPathToImage();





}
