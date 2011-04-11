package com.zeddic.war.collision;

import javax.microedition.khronos.opengles.GL10;

import com.zeddic.common.GameObject;
import com.zeddic.common.util.SimpleList;
import com.zeddic.war.level.Level;

/**
 * Handles collision detection for the game.
 * 
 * <p>The collision system is made up of two grids. One grid keeps
 * track of the various tiles from the tile map, while a second
 * grid keeps track of all the entities that reside in the world.
 * 
 * <p>When checking an entity for collisions, a quick query can be
 * be done against the grids to find any potential tiles or
 * world objects that the object intersects with. The source
 * object may then have it's x/y projected to avoid the collision.
 * 
 * <p>To register an entity with the collision system, call
 * an entities setBehavior.
 * 
 * @author scott@zeddic.com (Scott Bailey)
 */
public class CollisionSystem implements GameObject {

  private static CollisionSystem singleton;
  public static final int SIZE = 128;
  private EntityGrid entityGrid;
  private TileGrid tileGrid;
  private boolean ready;

  SimpleList<TileCell> nearbyCells = SimpleList.create(TileCell.class);

  private CollisionSystem() {
    ready = false;
  }

  public void initializeForLevel(Level level) {
    entityGrid = new EntityGrid(level, SIZE);
    tileGrid = new TileGrid(level);
    ready = true;
  }
  
  public void register(CollideComponent component) {
    if (!ready ||
        component.getBehavior() == CollideBehavior.HIT_ONLY ||
        component.getBehavior() == CollideBehavior.NONE) {
      return;
    }
    
    entityGrid.add(component);
  }
  
  public void unregister(CollideComponent component) {
    if (!ready ||
        component.getBehavior() == CollideBehavior.HIT_ONLY ||
        component.getBehavior() == CollideBehavior.NONE) {
      return;
    }

    entityGrid.remove(component);
  }
  
  public void update(CollideComponent component) {
    if (!ready ||
        component.getBehavior() == CollideBehavior.HIT_ONLY ||
        component.getBehavior() == CollideBehavior.NONE) {
      return;
    }

    entityGrid.update(component);
  }
  
  public void move(CollideComponent component, float dX, float dY) {
    // TODO: perform a scan of objects here cases where dX, dY are
    // more than 32 in length.
    
    component.entity.x += dX;
    component.entity.y += dY;
    
    CollideBehavior behavior = component.getBehavior();

    if (behavior == CollideBehavior.HIT_ONLY ||
        behavior == CollideBehavior.HIT_RECEIVE) {
      tileGrid.collide(component);
      entityGrid.collide(component);
    }

    if (behavior != CollideBehavior.HIT_ONLY &&
       (dX != 0 || dY != 0)) {
      entityGrid.update(component);
    }
  }

  public EntityGrid getEntityGrid() {
    return entityGrid;
  }
  
  public TileGrid getTileGrid() {
    return tileGrid;
  }

  @Override
  public void update(long time) {}

  @Override
  public void draw(GL10 gl) { }
  
  @Override
  public void reset() {
    ready = false;
    entityGrid = null;
    tileGrid = null;
  }
  
  public static CollisionSystem get() {
    if (singleton == null) {
      singleton = new CollisionSystem();
    }
    return singleton;
  }
}
