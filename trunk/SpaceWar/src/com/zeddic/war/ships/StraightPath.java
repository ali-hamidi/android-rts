/*
 * Copyright (C) 2010 Geo Siege Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zeddic.war.ships;

import android.graphics.Canvas;
import android.util.FloatMath;

import com.zeddic.common.Entity;
import com.zeddic.common.GameObject;

public class StraightPath implements GameObject {

  private Entity parent;
  private Target target;
  private float waitDistance;
  private float speed;
  
  private boolean inRange;
  private boolean enabled;
  
  
  private float lastTargetX = 0;
  private float lastTargetY = 0;
  
  public StraightPath(
      Entity parent,
      float waitDistance,
      float speed) {
    
    this.parent = parent;
    this.waitDistance = waitDistance;
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
  
  public void draw(Canvas canvas) {

  }

  public void update(long time) {

    if (!enabled || target == null) {
      return;
    }

    float dX = target.getX() - parent.x;
    float dY = target.getY() - parent.y;
    float distance = FloatMath.sqrt(dX * dX + dY * dY); 
    if (distance < waitDistance) {
      parent.velocity.x = 0;
      parent.velocity.y = 0;
      inRange = true;
      target.removeFollower(parent);
      target = null;
      return;
    }
    
    inRange = false;
    
    float velocityScale = (float) Math.min(distance, speed);
    
    parent.velocity.x = dX;
    parent.velocity.y = dY;
    parent.velocity.normalize();
    parent.velocity.x *= velocityScale;
    parent.velocity.y *= velocityScale;
    
    if (target.getX() != lastTargetX || target.getY() != lastTargetY) {
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
