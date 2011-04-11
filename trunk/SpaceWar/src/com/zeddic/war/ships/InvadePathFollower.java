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

import javax.microedition.khronos.opengles.GL10;

import android.util.FloatMath;

import com.zeddic.common.Entity;
import com.zeddic.common.GameObject;
import com.zeddic.common.util.Vector2d;
import com.zeddic.war.level.InvadePath;

public class InvadePathFollower implements GameObject {

  private Entity parent;
  private InvadePath path;
  private float speed;
  public boolean reached;
  private int pathIndex;

  public InvadePathFollower(
      Entity parent,
      float speed) {
    
    this.parent = parent;
    this.speed = speed;
    this.reached = false;
  }
  
  @Override
  public void reset() {
    this.reached = false;
    
  }
  
  public void setPath(InvadePath path) {
    this.path = path;
    this.pathIndex = 1;
    this.reached = false;
  }

  @Override
  public void draw(GL10 gl) { }

  @Override
  public void update(long time) {

    if (reached || path == null) {
      return;
    }

    // Calculate a vector towards the next point on the path.
    Vector2d target = path.points.items[pathIndex];
    float dX = target.x - parent.x;
    float dY = target.y - parent.y;
    
    // Determine the amount that we could travel in this frame.
    float travelPotential = (float) time / 1000 * speed;
    float distance = FloatMath.sqrt(dX * dX + dY * dY); 

    if (distance < travelPotential) {
      parent.x = target.x;
      parent.y = target.y;
      parent.velocity.x = 0;
      parent.velocity.y = 0;
      
      pathIndex++;
      if (pathIndex >= path.points.size) {
        reached = true;
      }
    } else {
      parent.velocity.x = dX;
      parent.velocity.y = dY;
      parent.velocity.normalize();
      parent.velocity.scale(speed);
      parent.matchAngleWithVelocity();
    }
  }
}
