package com.zeddic.war.collision;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.zeddic.common.Entity;
import com.zeddic.common.util.SimpleList;
import com.zeddic.common.util.Vector2d;
import com.zeddic.war.collision.TileBounds.EdgeType;

public class CollisionCell {
  

  private static int INITIAL_CAPACITY = 40;
  
  private static final Paint PAINT;
  private static final Paint FILL_PAINT;
  static {
    PAINT = new Paint();
    PAINT.setColor(Color.WHITE);
    PAINT.setStyle(Paint.Style.STROKE);
    PAINT.setStrokeWidth(1);
    
    FILL_PAINT = new Paint();
    FILL_PAINT.setColor(Color.rgb(219, 255, 207));
    FILL_PAINT.setStyle(Paint.Style.FILL);
  }

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

  public void draw(Canvas c) {
    
    //PAINT.setColor(Color.GREEN);
    //c.drawRect(left, top, left + CollisionSystem.SIZE, top + CollisionSystem.SIZE, PAINT);
    
    //if (items.size > 0) {
    //  c.drawRect(left, top, left + CollisionSystem.SIZE, top + CollisionSystem.SIZE, FILL_PAINT);
    //}
    
    PAINT.setColor(Color.WHITE);
    if (bounds != TileBounds.SOLID || !active) {
      return;
    }
    
    c.drawRect(left, top, left + CollisionSystem.SIZE, top + CollisionSystem.SIZE, PAINT);

    PAINT.setColor(Color.RED);
  
    if (topEdge == EdgeType.SOLID) {
      c.drawLine(left, top, left + CollisionSystem.SIZE, top, PAINT);
    }
    
    if (leftEdge == EdgeType.SOLID) {
      c.drawLine(left, top, left, top + CollisionSystem.SIZE, PAINT);
    }
    
    if (rightEdge == EdgeType.SOLID) {
      c.drawLine(left + CollisionSystem.SIZE, top, left + CollisionSystem.SIZE, top + CollisionSystem.SIZE, PAINT);
    }
    
    if (bottomEdge == EdgeType.SOLID) {
      c.drawLine(left, top + CollisionSystem.SIZE, left + CollisionSystem.SIZE, top + CollisionSystem.SIZE, PAINT);
    }
  }
  
  Vector2d projection = new Vector2d();
 
  public void collide(Entity entity) {
    
    if (bounds.isEmpty()) {
      return;
    }
    
    if (left() >= entity.right() || right() <= entity.left()) {
      return;
    }
    
    if (top() >= entity.bottom() || bottom() <= entity.top()) {
      return;
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
