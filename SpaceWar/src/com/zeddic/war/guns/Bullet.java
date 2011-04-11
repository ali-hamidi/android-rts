package com.zeddic.war.guns;

import javax.microedition.khronos.opengles.GL10;

import com.zeddic.common.Entity;
import com.zeddic.common.opengl.Color;
import com.zeddic.common.opengl.SimpleGeometry;
import com.zeddic.common.util.Vector2d;
import com.zeddic.war.collision.CollideBehavior;
import com.zeddic.war.effects.Effects;
import com.zeddic.war.ships.EnemyShip;

public class Bullet extends Entity {

  private static final long DEFAULT_MAX_LIFE = 4000;
  private static final Color color = new Color(255, 255, 0, 255);
  public long life; 
  public long maxLife;

  public Bullet() {
    this(0, 0);
    this.radius = 4;
  }
  
  public Bullet(float x, float y) {
    super(x, y);
    this.collide.setBehavior(CollideBehavior.HIT_ONLY);
    this.life = 0;
    this.maxLife = DEFAULT_MAX_LIFE;
  }

  @Override
  public void kill() {
    super.kill();
  }
  
  public void reset() {
    life = 0;
    enabled = true;
    canRecycle = false;
  }

  @Override
  public void draw(GL10 gl) {
    SimpleGeometry.drawLine(
        gl,
        x + velocity.x / 40, y + velocity.y / 40 ,
        x - velocity.x / 40, y - velocity.y / 40,
        color);
  }
  
  @Override
  public void update(long time) {
    super.update(time);
    life += time;
    if (life > maxLife) {
      kill();
    }
  }

  public void offset(float distance) {
    x = x + distance * (float) Math.cos(Math.toRadians(angle));
    y = y + distance * (float) Math.sin(Math.toRadians(angle));
  }

  @Override
  public void collide(Entity other, Vector2d avoidVector, boolean recieving) {
    Effects.get().hit(x, y, avoidVector);
    
    if (other instanceof EnemyShip) {
      ((EnemyShip) other).hit(10);
    }
    kill();
  }
}
