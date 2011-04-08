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

import android.graphics.Color;
import android.graphics.Paint;

import com.zeddic.common.Entity;
import com.zeddic.common.opengl.Sprite;
import com.zeddic.common.util.Components;
import com.zeddic.common.util.Countdown;
import com.zeddic.common.util.Polygon;
import com.zeddic.common.util.Polygon.PolygonBuilder;
import com.zeddic.common.util.Vector2d;
import com.zeddic.war.R;
import com.zeddic.war.collision.CollideBehavior;
import com.zeddic.war.guns.Gun;

public class FighterShip extends Ship {
  
  private static final Paint PAINT;
  public static final Polygon SHAPE;
  private static float ANGLE_OFFSET = -90;
  static {
    PAINT = new Paint();
    PAINT.setColor(Color.BLUE);
    PAINT.setStyle(Paint.Style.STROKE);
    PAINT.setStrokeWidth(3);
    SHAPE = new PolygonBuilder()
        .add(0, 17)
        .add(12, -8)
        .add(0, -16)
        .add(-12, -8)
        .build();
  }

  public float maxHealth = 200;
  public float health = 200;
  private Countdown soundCountdown = new Countdown(400);
  private Gun gun;
  private Components components;
  private float speed;
  private Target target;
  private StraightPath path;

  private Sprite sprite = new Sprite(40, 40, R.drawable.ship);
  
  
  public FighterShip() {
    this(0, 0);
  }
  
  public FighterShip(float x, float y) {
    super(x, y);

    this.radius = 8;
    this.speed = 20;
    this.collide.setBehavior(CollideBehavior.HIT_RECEIVE);
    
    path = new StraightPath(this, 5, speed);
    
    components = new Components();
    components.add(path);
  }
  
  public void reset() {
    enable();
    gun.reset();
    
    health = maxHealth;
    
    soundCountdown.restart();
  }
  
  public void spawn(float x, float y) {
    reset();
    this.x = x;
    this.y = y;
  }
  
  /*public void centerOnMap() {
    this.x = GameState.level.map.left + GameState.level.map.width / 2;
    this.y = GameState.level.map.top + GameState.level.map.height / 2;
  }*/
  
  public void updateSpeed(float scaleX, float scaleY) {
    this.setVelocity(scaleX * speed, scaleY * speed);
  }
  
  @Override
  public void update(long time) {
    
    if (isDead())
      return;
    
    super.update(time);
    
    soundCountdown.update(time);
    components.update(time);
  }
  
  @Override
  public void draw(GL10 gl) {
    
    // TODO(baileys): Draw using opengl.
    
    /*canvas.save();
    canvas.translate(x, y);
    canvas.rotate(getAngleOffset() + angle);
    canvas.scale(scale, scale);
    canvas.drawPath(SHAPE.path, PAINT);
    canvas.restore(); */
    
    sprite.x = x;
    sprite.y = y;
    sprite.rz = angle;
    //.scale = 2;

    sprite.draw(gl);
    
    
    components.draw(gl);
  }
  
  public void setTarget(Target target) {
    this.target = target;
    this.path.setTarget(target);
    //this.setAngle(calculateAngle(target));
  }
  
  public Target getTarget() {
    return this.path.getTarget();
  }

  public void fire() {
    gun.fire();
  }

  public void fire(float angle) {
    gun.setAimAngle(angle);    
  }
  
  public boolean isDead() {
    return !enabled;
  }
  
  @Override
  protected float getAngleOffset() {
    return ANGLE_OFFSET;
  }
  
  @Override
  public void damage(float damage) {
    if (isDead())
      return;
    health -= damage;
    health = Math.max(0, health);
    if (health == 0) {
      die();
    }
  }
  
  public float getPercentHealth() {
    return health / maxHealth;
  }
  
  public void die() {
    if (isDead()) {
      return;
    }
    kill();
    //GameState.geoEffects.shockwave(x, y);
    //GameState.stockpiles.killAllEnemies();    
  }

  public void collide(Entity object, Vector2d avoidVector) {
    /*if (object instanceof Bullet) {
      damage(5);
    } else if (object instanceof EnemyShip) {
      damage(5);
      ((EnemyShip) object).damage(5);
    } else {
      super.collide(object, avoidVector);
    } */
  }
}
