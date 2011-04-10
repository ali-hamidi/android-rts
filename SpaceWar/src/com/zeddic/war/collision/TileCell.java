package com.zeddic.war.collision;

import com.zeddic.common.Entity;
import com.zeddic.common.util.Vector2d;
import com.zeddic.war.collision.TileBounds.EdgeType;

/**
 * A class that represents a single collidable tile in a tile map.
 * 
 * @author scott@zeddic.com (Scott Bailey)
 */
public class TileCell {

  private TileBounds bounds = TileBounds.EMPTY;
  
  private boolean active;
  private float top;
  private float left;
  
  private TileGrid grid;
  private EdgeType topEdge;
  private EdgeType bottomEdge;
  private EdgeType rightEdge;
  private EdgeType leftEdge;
  private float size;
  
  int row;
  int col;

  public TileCell(TileGrid grid, int row, int col, float size) {
    this.grid = grid;
    this.row = row;
    this.col = col;
    this.size = size;
    top = row * size;
    left = col * size;
    active = false;
  }
  
  public void setBounds(TileBounds bounds) {
    this.bounds = bounds;
    this.active = bounds != TileBounds.EMPTY;
  }
  
  public void calculateEdges() {
    
    TileCell above = grid.above(this);
    TileCell below = grid.below(this);
    TileCell left = grid.left(this);
    TileCell right = grid.right(this);
    
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
  public boolean collide(Entity entity) {
    
    if (!active) {
      return false;
    }
    
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
    return top + size;
  }
  
  public float left() {
    return left;
  }
  
  public float right() {
    return left + size;
  }
}
