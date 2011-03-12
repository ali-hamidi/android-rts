package com.zeddic.war.collision;

import android.graphics.Canvas;

import com.zeddic.game.common.util.SimpleList;
import com.zeddic.war.level.Level;

public class CollisionSystem {

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
  
  public void collide(CollideComponent component) {
    grid.collide(component);
  }
  
  public CollisionGrid getGrid() {
    return grid;
  }
  
  /**
   * Draw the collision grid for debug purposes only.
   */
  public void draw(Canvas c) {
    grid.draw(c);
  }
  
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
