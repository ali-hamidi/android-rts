package com.zeddic.war.collision;

import com.zeddic.common.Entity;

/**
 * A fake entity used simply for the purpose of querying the collision grid
 * to see if a position is available for an object to be placed.
 * 
 * @author scott@zeddic.com (Scott Bailey)
 */
public class CollisionQueryEntity extends Entity {

  public void setQuery(float x, float y, float radius) {
    this.x = x;
    this.y = y;
    this.radius = radius;
  }
}
