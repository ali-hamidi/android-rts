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

package com.zeddic.war.guns;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.zeddic.common.Entity;
import com.zeddic.common.effects.Effects;
import com.zeddic.common.util.ComponentManager;
import com.zeddic.common.util.Vector2d;

public class Bullet extends Entity {

  private static final long DEFAULT_MAX_LIFE = 4000;
  
  ////SETUP OBJECT SHAPE AND PAINT
  private static final Paint PAINT;
  private static float ANGLE_OFFSET = 0;
  static {
    PAINT = new Paint();
    PAINT.setColor(Color.YELLOW);
    PAINT.setStyle(Paint.Style.FILL);
    PAINT.setStrokeWidth(2);
  }

  public long life; 
  public long maxLife;
  public boolean firedByEnemy = false;
  
  private ComponentManager components;
  //private CollisionComponent collisionComponent;
  
  public Bullet() {
    this(0, 0);
    this.radius = 4;
  }
  
  public Bullet(float x, float y) {
    super(x, y);

    life = 0;
    maxLife = DEFAULT_MAX_LIFE;
    
    //collisionComponent = new CollisionComponent(this, CollisionManager.TYPE_HIT_ONLY);
    
    components = new ComponentManager(this);
    //components.add(collisionComponent);
    //components.add(new MapBoundsComponent(this, MapBoundsComponent.BEHAVIOR_COLLIDE));
  }
  
  @Override
  public void kill() {
    super.kill();
   // collisionComponent.unregisterObject();
  }
  
  public void reset() {
    life = 0;
    enabled = true;
    canRecycle = false;
  }
  
  @Override
  public void draw(Canvas canvas) {
    canvas.drawLine(x, y, x + -velocity.x / 6, y + -velocity.y / 6, PAINT);
  }
  
  @Override
  public void update(long time) {
    super.update(time);
    life += time;
    if (life > maxLife) {
      kill();
    }
    
    components.update(time);
  }
  
  public void offset(float distance) {
    x = x + distance * (float) Math.cos(Math.toRadians(angle));
    y = y + distance * (float) Math.sin(Math.toRadians(angle));
  }
  
  @Override
  protected float getAngleOffset() {
    return ANGLE_OFFSET;
  }
  
  @Override
  public void collide(Entity other, Vector2d avoidVector) {
    super.collide(other, avoidVector);
    Effects.get().hit(x, y, avoidVector);
    kill();
  }
}
