package com.zeddic.war;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.zeddic.common.AbstractGameObject;
import com.zeddic.common.util.SimpleList;
import com.zeddic.common.util.Vector2d;
import com.zeddic.war.collision.ProximityUtil;
import com.zeddic.war.ships.FighterShip;
import com.zeddic.war.ships.LocationTarget;
import com.zeddic.war.ships.Target;

public class BattleCommandManager extends AbstractGameObject {

  private static final Paint PAINT;
  static {
    PAINT = new Paint();
    PAINT.setColor(Color.rgb(64, 255, 0));
    PAINT.setStrokeWidth(1);
    PAINT.setStyle(Paint.Style.STROKE);
  }

  private final SimpleList<Target> targets = SimpleList.create(Target.class);

  private Selection selection;
  private float lastX;
  private float lastY;
  
  public BattleCommandManager() {
    
  }
  
  public void onTouch(MotionEvent e) {
    switch (e.getAction()) {
      case MotionEvent.ACTION_DOWN: onPress(e); break;
      case MotionEvent.ACTION_UP: onRelease(e); break;
      case MotionEvent.ACTION_MOVE: onMove(e); break;
      default: onCancel(e); break;
    }
  }
  
  private void onPress(MotionEvent e) {
    
    this.selection = null;

    FighterShip ship = (FighterShip) ProximityUtil.getClosest(FighterShip.class, e.getX(), e.getY(), 50);
    if (ship != null) {
      //if (ship.getTarget() != null) {
      //  this.selection = new Selection(ship.getTarget());
      //} else {
        this.selection = new Selection(ship);
      //}
    } else {
      Target target = getTargetInRange(e.getX(), e.getY());
      if (target != null) {
        this.selection = new Selection(target);
      }
    }

    lastX = e.getX();
    lastY = e.getY();
  }

  private void onRelease(MotionEvent e) {
    if (selection == null) {
      return;
    }
    
    if (selection.isShip()) {
      
      if (selection.ship.getTarget() != null) {
        selection.ship.getTarget().set(e.getX(), e.getY());
      } else {
        final LocationTarget target = new LocationTarget(e.getX(), e.getY());
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
  }
  
  private void onMove(MotionEvent e) {
    if (selection == null) {
      return;
    }
    
    lastX = e.getX();
    lastY = e.getY();
    
    if (!selection.isShip()) {
      selection.target.set(lastX, lastY);
    }
  }
  
  private void onCancel(MotionEvent e) {
    selection = null;
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
      
      
      /*float dX = lastX - selection.ship.x;
      float dY = lastY - selection.ship.y;
      
      if (dX * dX + dY * dY > 25 * 25) {
        Vector2d temp = new Vector2d(dX, dY);
        temp.normalize();
        temp.x *= 25;
        temp.y *= 25;
        c.drawLine(selection.ship.x + temp.x, selection.ship.y + temp.y, lastX, lastY, PAINT);
      } */
      
      
      //c.drawLine(selection.getX(), selection.getY(), lastX, lastY, PAINT);
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
    float minDistanceSquared = 50 * 50;
    
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

    public float getX() {
      return ship != null ? ship.x : target.getX();
    }

    public float getY() {
      return ship != null ? ship.y : target.getY();
    }
  }
}
