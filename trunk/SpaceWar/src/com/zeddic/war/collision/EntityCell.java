package com.zeddic.war.collision;

import android.util.FloatMath;

import com.zeddic.common.Entity;
import com.zeddic.common.util.SimpleList;
import com.zeddic.common.util.Vector2d;

public class EntityCell {

  private static int INITIAL_CAPACITY = 40;
  private Vector2d projection = new Vector2d();
  protected SimpleList<Entity> items;
  protected int row;
  protected int col;

  public EntityCell(int row, int col) {
    this.row = row;
    this.col = col;
    items = new SimpleList<Entity>(Entity.class, INITIAL_CAPACITY);
  }

  public boolean collide(Entity entity) {
    
    Entity other;
    for (int i = 0; i < items.size; i++) {
      other = items.items[i];

      if (other == entity) {
        continue;
      }

      // Are they colliding now?
      float dX = entity.x - other.x;
      float dY = entity.y - other.y;
      float minDistance = entity.radius + other.radius;
      boolean colliding = dX * dX + dY * dY < minDistance * minDistance;
      
      if (colliding) {
        float seperationNeeded = minDistance - FloatMath.sqrt(dX * dX + dY * dY);
        projection.x = dX;
        projection.y = dY;
        projection.normalize();
        projection.x *= seperationNeeded;
        projection.y *= seperationNeeded;
        
        entity.x += projection.x;
        entity.y += projection.y;
        
        // TODO(scott): Tell entity what they have collided with.

        return true;
      }
      
    }
    
    return false;
  }
  
  /**
   * Adds a new object to this grid position.
   */
  public void add(Entity object) {
    items.add(object);
  }
  
  /**
   * Returns true if an entity is currently in the cell.
   */
  public boolean contains(Entity object) {
    return items.contains(object);
  }
  
  /**
   * Removes an object from the grid spot.
   */
  public void remove(Entity object) {
    items.remove(object);
  }
}
