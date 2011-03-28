package com.zeddic.war.collision;

import javax.microedition.khronos.opengles.GL10;

import com.zeddic.common.GameObject;
import com.zeddic.common.util.SimpleList;
import com.zeddic.war.level.Level;

public class CollisionSystem implements GameObject {

  private static CollisionSystem singleton;
  public static final int SIZE = 32;
  private CollisionGrid grid;
  private boolean ready;

  SimpleList<CollisionCell> nearbyCells = SimpleList.create(CollisionCell.class);

  private CollisionSystem() {
    ready = false;
  }

  public void initializeForLevel(Level level) {
    grid = new CollisionGrid(level);
    ready = true;
  }
  
  public void register(CollideComponent component) {
    if (!ready) {
      return;
    }
    
    grid.add(component);
  }
  
  public void unregister(CollideComponent component) {
    if (!ready) {
      return;
    }

    grid.remove(component);
  }
  
  public void update(CollideComponent component) {
    grid.update(component);
  }
  
  public void move(CollideComponent component, float dX, float dY) {
    // TODO: perform a scan of objects here cases where dX, dY are
    // more than 32 in length.
    
    component.entity.x += dX;
    component.entity.y += dY;
    grid.collide(component);
    
    if (dX != 0 || dY != 0) {
      grid.update(component);
    }
  }

  public CollisionGrid getGrid() {
    return grid;
  }
  
  @Override
  public void update(long time) {
    // Nothing to do.
  }
  
  /**
   * Draw the collision grid for debug purposes only.
   */
  @Override
  public void draw(GL10 gl) {
    grid.draw(gl);
  }
  
  @Override
  public void reset() {
    ready = false;
    grid = null;
  }
  
  public static CollisionSystem get() {
    if (singleton == null) {
      singleton = new CollisionSystem();
    }
    return singleton;
  }
}
