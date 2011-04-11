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

import com.zeddic.common.Entity;
import com.zeddic.common.opengl.Color;
import com.zeddic.common.opengl.Sprite;
import com.zeddic.common.util.Vector2d;
import com.zeddic.war.R;
import com.zeddic.war.collision.CollideBehavior;
import com.zeddic.war.effects.Effects;
import com.zeddic.war.level.InvadePath;

public class Square extends Entity implements EnemyShip {
  
  public float maxHealth = 60;
  public float health;
  private float speed;
  private static final Color color = new Color(255, 0, 251, 255);
  
  private InvadePathFollower pather;
  
  private Sprite sprite = new Sprite(32, 32, R.drawable.square);

  public Square() {
    this(0, 0);
  }

  public Square(float x, float y) {
    super(x, y);
    this.collide.setBehavior(CollideBehavior.RECEIVE_ONLY);
    this.radius = 10;
    this.speed = 50;
    this.pather = new InvadePathFollower(this, speed);
  }
  
  public void reset() {
    enable();
    health = maxHealth;
  }
  
  public void spawn(InvadePath path) {
    reset();
    this.x = path.points.items[0].x;
    this.y = path.points.items[0].y;
    this.pather.setPath(path);
  }
  
  @Override
  public void update(long time) {
    if (isDead())
      return;

    pather.update(time);
    super.update(time);
    
    if (pather.reached) {
      die();
    }
  }
  
  @Override
  public void draw(GL10 gl) {
    sprite.x = x;
    sprite.y = y;
    sprite.setColor(color);
    sprite.draw(gl);
  }

  public boolean isDead() {
    return !enabled;
  }

  public float getPercentHealth() {
    return health / maxHealth;
  }

  public void die() {
    if (isDead()) {
      return;
    }
    kill();
    this.collide.unregisterObject();
    Effects.get().explode(x, y);
  }

  public void collide(Entity object, Vector2d avoidVector) {

  }

  @Override
  public void hit(float damage) {
    if (isDead())
      return;
    health -= damage;
    health = Math.max(0, health);
    if (health == 0) {
      die();
    }
  }
}
