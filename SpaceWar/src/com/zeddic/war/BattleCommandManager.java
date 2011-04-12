package com.zeddic.war;

import javax.microedition.khronos.opengles.GL10;

import android.view.MotionEvent;

import com.zeddic.common.AbstractGameObject;
import com.zeddic.common.opengl.Color;
import com.zeddic.common.opengl.SimpleGeometry;
import com.zeddic.common.util.SimpleList;
import com.zeddic.common.util.Vector2d;
import com.zeddic.war.collision.ProximityUtil;
import com.zeddic.war.ships.FighterShip;
import com.zeddic.war.ships.LocationTarget;
import com.zeddic.war.ships.Target;

public class BattleCommandManager extends AbstractGameObject {

  private static final float SELECTION_RANGE = 100;
  private static final Color color = new Color(0, 255, 0, 255);
  private final SimpleList<Target> targets = SimpleList.create(Target.class);

  private Selection selection;
  private float lastX;
  private float lastY;
  
  public BattleCommandManager() {
    
  }

  public boolean onTouch(MotionEvent e) {
    switch (e.getAction()) {
      case MotionEvent.ACTION_DOWN: return onPress(e);
      case MotionEvent.ACTION_UP: return onRelease(e);
      case MotionEvent.ACTION_MOVE: return onMove(e);
      default: return onCancel(e);
    }
  }
  
  private boolean onPress(MotionEvent e) {
    this.selection = null;
    
    Vector2d world = GameState.camera.convertToWorld(new Vector2d(e.getX(), e.getY()));
    
    FighterShip ship = (FighterShip) ProximityUtil.getClosest(
        FighterShip.class,
        world.x, world.y, 
        SELECTION_RANGE);
    if (ship != null) {
      selection = new Selection(ship);
    } else {
      Target target = getTargetInRange(world.x, world.y);
      if (target != null) {
        selection = new Selection(target);
      }
    }

    lastX = world.x;
    lastY = world.y;
    
    return this.selection != null;
  }

  private boolean onRelease(MotionEvent e) {
    if (selection == null) {
      return false;
    }
    
    Vector2d world = GameState.camera.convertToWorld(new Vector2d(e.getX(), e.getY()));

    if (selection.isShip()) {
      if (selection.ship.getTarget() != null) {
        selection.ship.getTarget().set(world.x, world.y);
      } else {
        final LocationTarget target = new LocationTarget(world.x, world.y);
        target.addFollower(selection.ship);
        target.addReachedHandler(new Runnable() {
            @Override
            public void run() {
              targets.remove(target);
            }
          });
        
        targets.add(target);
        selection.ship.setTarget(target);
      }
    }
    
    selection = null;
    
    return true;
  }
  
  private boolean onMove(MotionEvent e) {
    if (selection == null) {
      return false;
    }

    Vector2d world = GameState.camera.convertToWorld(new Vector2d(e.getX(), e.getY()));
    
    lastX = world.x;
    lastY = world.y;
    
    if (!selection.isShip()) {
      selection.target.set(lastX, lastY);
    }
    
    return true;
  }
  
  private boolean onCancel(MotionEvent e) {
    selection = null;
    return false;
  }
  
  public boolean hasSelection() {
    return selection != null;
  }
  
  @Override
  public void draw(GL10 gl) {
    
    for (int i = 0; i < targets.size; i++) {
      targets.items[i].draw(gl);
    }
    
    if (!hasSelection()) {
      return;
    }

    if (selection.isShip()) {
            
      float dX = lastX - selection.ship.x;
      float dY = lastY - selection.ship.y;
      
      if (dX * dX + dY * dY > 25 * 25) {
        Vector2d temp = new Vector2d(dX, dY);
        temp.normalize();
        temp.x *= 25;
        temp.y *= 25;
        
        SimpleGeometry.drawLine(gl, selection.ship.x + temp.x, selection.ship.y + temp.y, lastX, lastY, color);
      }
    }
  }
  
  @Override
  public void update(long time) {
    for (int i = 0; i < targets.size; i++) {
      targets.items[i].update(time);
    }
  }

  private Target getTargetInRange(float x, float y) {
    Target toReturn = null;
    Target target;
    
    float range = SELECTION_RANGE;
    float minDistanceSquared = range * range;
    
    for (int i = 0; i < targets.size; i++) {
      target = targets.items[i];

      float dX = x - target.getX();
      float dY = y - target.getY();
      float distanceSquared = dX * dX + dY * dY;
      if (distanceSquared < minDistanceSquared) {
        toReturn = target;
        minDistanceSquared = distanceSquared;
      }
    }
    
    return toReturn;
  }

  private static class Selection {
    private FighterShip ship;
    private Target target;
    
    public Selection(FighterShip ship) {
      this.ship = ship;
      this.target = null;
    }
    
    public Selection(Target target) {
      this.ship = null;
      this.target = target;
    }

    public boolean isShip() {
      return ship != null;
    }
  }
}
