package com.zeddic.war.collision;

import android.util.FloatMath;

import com.zeddic.common.Entity;
import com.zeddic.common.util.SimpleList;
import com.zeddic.common.util.Vector2d;
import com.zeddic.war.collision.TileBounds.EdgeType;

public class CollisionCell {

  private static int INITIAL_CAPACITY = 40;
  private TileBounds bounds = TileBounds.EMPTY;
  public SimpleList<Entity> items;
  
  private boolean active;
  private float top;
  private float left;
  
  private CollisionGrid grid;
  private EdgeType topEdge;
  private EdgeType bottomEdge;
  private EdgeType rightEdge;
  private EdgeType leftEdge;
  
  int row;
  int col;

  public CollisionCell(CollisionGrid grid, int row, int col) {
    this.grid = grid;
    this.row = row;
    this.col = col;
    top = row * CollisionSystem.SIZE;
    left = col * CollisionSystem.SIZE;
    active = false;
    items = new SimpleList<Entity>(Entity.class, INITIAL_CAPACITY);
  }
  
  public void setBounds(TileBounds bounds) {
    this.bounds = bounds;
    this.active = bounds != TileBounds.EMPTY;
  }
  
  public void calculateEdges() {
    
    CollisionCell above = grid.above(this);
    CollisionCell below = grid.below(this);
    CollisionCell left = grid.left(this);
    CollisionCell right = grid.right(this);
    
    topEdge = 
        (above == null || bounds.topEdge == EdgeType.SOLID && grid.above(this).bounds.bottomEdge == EdgeType.SOLID)
        ? EdgeType.EMPTY
        : bounds.topEdge;

    bottomEdge =
        (below == null || bounds.bottomEdge == EdgeType.SOLID && grid.below(this).bounds.topEdge == EdgeType.SOLID)
        ? EdgeType.EMPTY
        : bounds.bottomEdge;

    leftEdge =
        (left == null || bounds.leftEdge == EdgeType.SOLID && grid.left(this).bounds.rightEdge == EdgeType.SOLID)
        ? EdgeType.EMPTY
        : bounds.leftEdge;
    
    rightEdge =
        (right == null || bounds.rightEdge == EdgeType.SOLID && grid.right(this).bounds.leftEdge == EdgeType.SOLID)
        ? EdgeType.EMPTY
        : bounds.rightEdge;
  }

  Vector2d projection = new Vector2d();
 
  public void collide(Entity entity) {
    
    if (!collideWithTile(entity)) {
      collideWithItems(entity);
    }
  }
  
  private boolean collideWithTile(Entity entity) {
    if (bounds.isEmpty()) {
      return false;
    }
    
    if (left() >= entity.right() || right() <= entity.left()) {
      return false;
    }
    
    if (top() >= entity.bottom() || bottom() <= entity.top()) {
      return false;
    }
        
    projection.x = Float.MAX_VALUE;
    projection.y = Float.MAX_VALUE;
    boolean hit = false;
    
    if (bottomEdge == EdgeType.SOLID && entity.top() < bottom() && entity.top() > top()) {
      determineShorterProjection(projection, 0, bottom() - entity.top());
      hit = true;
    }
    
    if (topEdge == EdgeType.SOLID && entity.bottom() > top() && entity.bottom() < bottom()) {
      determineShorterProjection(projection, 0, top() - entity.bottom());
      hit = true;
    }
    
    if (leftEdge == EdgeType.SOLID && entity.right() > left() && entity.right() < right()) {
      determineShorterProjection(projection, left() - entity.right(), 0);
      hit = true;
    }
    
    if (rightEdge == EdgeType.SOLID && entity.left() < right() && entity.left() > left()) {
      determineShorterProjection(projection, right() - entity.left(), 0);
      hit = true;
    }
    
    if (hit) {
      entity.x += projection.x;
      entity.y += projection.y;
      //entity.collide(projection, null);
    }
    
    return hit;
  }
  
  private void collideWithItems(Entity entity) {
    
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
      }
    }
  }
  
  private void determineShorterProjection(Vector2d projection, float dX, float dY) {
    if (Math.abs(dX + dY) < Math.abs(projection.x + projection.y)) {
      projection.x = dX;
      projection.y = dY;
    }
  }
    
  public float top() {
    return top;
  }
  
  public float bottom() {
    return top + CollisionSystem.SIZE;
  }
  
  public float left() {
    return left;
  }
  
  public float right() {
    return left + CollisionSystem.SIZE;
  }
  
  /**
   * Adds a new object to this grid position.
   */
  public void add(Entity object) {
    items.add(object);
  }
  
  
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
