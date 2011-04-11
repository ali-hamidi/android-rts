package com.zeddic.war.ships;

/**
 * Common interface for all enemies in wave ships.
 * 
 * @author scott@zeddic.com (Scott Bailey)
 */
public interface EnemyShip {
  
  /**
   * Damages the ship by the given amount.
   */
  void hit(float damage);
}
