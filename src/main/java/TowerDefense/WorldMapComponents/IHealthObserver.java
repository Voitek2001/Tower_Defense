package TowerDefense.WorldMapComponents;

import TowerDefense.WorldMapElements.AbstractMapElementsWithHealth;

public interface IHealthObserver {
    void changeHealth(AbstractMapElementsWithHealth element, int oldHealth, int newHealth);
}
