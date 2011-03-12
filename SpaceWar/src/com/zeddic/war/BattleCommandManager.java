package com.zeddic.war;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.zeddic.game.common.GameObject;
import com.zeddic.game.common.util.SimpleList;
import com.zeddic.war.collision.ProximityUtil;
import com.zeddic.war.ships.FighterShip;
import com.zeddic.war.ships.LocationTarget;
import com.zeddic.war.ships.Target;
import com.zeddic.war.util.HasPosition;

public class BattleCommandManager extends GameObject {

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
      this.selection = new Selection(ship);
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
    
    LocationTarget target = new LocationTarget(e.getX(), e.getY());
    targets.add(target);
    
    if (selection.isShip()) {
      selection.ship.setTarget(target);
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
  public void draw(Canvas c) {
    if (!hasSelection()) {
      return;
    }

    if (selection.isShip()) {
      c.drawLine(selection.getX(), selection.getY(), lastX, lastY, PAINT);
    }
  }
  
  @Override
  public void update(long time) {
    
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
  
  private static class Selection implements HasPosition {
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

    @Override
    public float getX() {
      return ship != null ? ship.x : target.getX();
    }

    @Override
    public float getY() {
      return ship != null ? ship.y : target.getY();
    }
  }
}
