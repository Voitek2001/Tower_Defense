package TowerDefense.WorldMapElements;

import TowerDefense.WorldMapComponents.IHealthObserver;
import TowerDefense.WorldMapComponents.IStatusObserver;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractMapElementsWithHealth extends AbstractMapElements{

    protected List<IHealthObserver> healthObservers = new LinkedList<>();
    protected List<IStatusObserver> statusObservers = new LinkedList<>();
    protected int HP;
    protected int maxHP;

    protected ElementStatus status;

    public void addHealthObserver(IHealthObserver observer) {
        this.healthObservers.add(observer);
    }
    public void removeHealthObserver(IHealthObserver observer) {
        this.healthObservers.remove(observer);
    }

    public void addStatusObserver(IStatusObserver observer) {
        this.statusObservers.add(observer);
    }

    public void removeStatusObserver(IStatusObserver observer) {
        this.statusObservers.remove(observer);
    }

    public void changeHealth(int decreaseBy) {
        for(IHealthObserver observer : this.healthObservers) {
            observer.changeHealth(this, this.HP, this.HP - decreaseBy);
        }
        this.HP -= decreaseBy;
        checkIfDie();
    }

    public int getHP() {
        return this.HP;
    }

    @Override
    public int getMaxHP() {
        return maxHP;
    }

    private void checkIfDie() {
        if (this.HP <= 0) {
            this.status = ElementStatus.DEAD;
            for (IStatusObserver observer: this.statusObservers) {
                observer.die(this);
            }

        }
    }

    public void setStatus(ElementStatus newStatus) {
        this.status = newStatus;
    }

    public ElementStatus getStatus() {
        return this.status;
    }
}
