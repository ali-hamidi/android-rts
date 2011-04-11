package com.zeddic.war.ships;

import javax.microedition.khronos.opengles.GL10;

import android.util.FloatMath;

import com.zeddic.common.Entity;
import com.zeddic.common.GameObject;

public class StraightPath implements GameObject {

  private Entity parent;
  private Target target;
  private float speed;
  private boolean inRange;
  private boolean enabled;
  
  public StraightPath(
      Entity parent,
      float speed) {
    
    this.parent = parent;
    this.speed = speed;
    this.inRange = false;
    this.enabled = true;
  }
  
  @Override
  public void reset() {
    this.inRange = false;
    this.enabled = true;
  }
  
  public void setTarget(Target target) {
    this.target = target;
  }

  public Target getTarget() {
    return target;
  }
  
  @Override
  public void draw(GL10 gl) {

  }

  @Override
  public void update(long time) {

    if (!enabled || target == null) {
      return;
    }

    // Calculate a vector towards the target
    float dX = target.getX() - parent.x;
    float dY = target.getY() - parent.y;
    
    if (dX == 0 && dY == 0) {
      return;
    }
    
    // Determine the amount that we could travel in this frame.
    float travelPotential = (float) time / 1000 * speed;
    float distance = FloatMath.sqrt(dX * dX + dY * dY); 

    if (distance < travelPotential) {
      inRange = true;
      parent.x = target.getX();
      parent.y = target.getY();
      parent.velocity.x = 0;
      parent.velocity.y = 0;
      target.removeFollower(parent);
    } else {
      inRange = false;
      parent.velocity.x = dX;
      parent.velocity.y = dY;
      parent.velocity.normalize();
      parent.velocity.scale(speed);
      parent.matchAngleWithVelocity();
    }
  }

  /**
   * Disables the pather. Once triggered the parent object will no longer
   * have itself directed by the pather.
   */
  public void disable() {
    enabled = false;
  }
  
  /**
   * Allows the pather to move the parent owner.
   */
  public void enable() {
    enabled = true;
  }
  
  public boolean inWaitRange() {
    return inRange;
  }
}
