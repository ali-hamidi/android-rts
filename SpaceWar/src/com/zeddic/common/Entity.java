package com.zeddic.common;

import javax.microedition.khronos.opengles.GL10;

import com.zeddic.common.util.Vector2d;
import com.zeddic.war.collision.CollideBehavior;
import com.zeddic.war.collision.CollideComponent;

public abstract class Entity extends AbstractGameObject {

  public static float TIME_SCALER = 1000;

  public float x;
  public float y;
  public float angle;
  public float scale;
  public float rotation;
  public float radius;
  public Vector2d velocity;
  public CollideComponent collide;

  public Entity() {
    this(0, 0);
  }

  public Entity(float x, float y) {
    this.x = x;
    this.y = y;

    this.velocity = new Vector2d(0, 0);
    this.scale = 1;
    this.angle = 0;
    this.rotation = 0;
    this.collide = new CollideComponent(this, CollideBehavior.NONE);
  }
  
  public void setScale(float scale) {
    if (scale == this.scale) {
      return;
    }
    this.scale = scale;
  }

  public void setVelocity(float x, float y) {
    velocity.x = x;
    velocity.y = y;
  }
  
  public void setVelocityBySpeed(float angle, float speed) {
    double radians = Math.toRadians(angle);
    velocity.x = speed * (float) Math.cos(radians);
    velocity.y = speed * (float) Math.sin(radians);
  }
  
  public void setVelocityBySpeed(float speed) {
    double radians = Math.toRadians(angle);
    velocity.x = speed * (float) Math.cos(radians);
    velocity.y = speed * (float) Math.sin(radians);
  }
  
  public float getSpeed() {
    return (float) Math.sqrt(velocity.x * velocity.x + velocity.y * velocity.y);
  }
  
  public void matchAngleWithVelocity() {
    this.angle = velocity.getAngle();
  }

  public void update(long time) {
    float timeFraction = (float) time / TIME_SCALER;
    float dX = velocity.x * timeFraction;
    float dY = velocity.y * timeFraction;
    angle = angle + rotation * timeFraction;
    
    collide.move(dX, dY);
    collide.update(time);
  }

  @Override
  public void draw(GL10 gl) {
    // Leave drawing to implementing classes.
  }

  protected float getAngleOffset() {
    return 0;
  }
  
  public void collide(Entity object, Vector2d avoidVector) {
    if (avoidVector != null) {
      x += avoidVector.x;
      y += avoidVector.y;
    }
  }

  public float top() {
    return y - radius;
  }
  
  public float bottom() {
    return y + radius;
  }
  
  public float left() {
    return x - radius;
  }
  
  public float right() {
    return x + radius;
  }
  
  public void setTop(float top) {
    y = top + radius;
  }
  
  public void setLeft(float left) {
    x = left + radius;
  }
  
  public void setBottom(float bottom) {
    y = bottom - radius;
  }
  
  public void setRight(float right) {
    x = right - radius;
  }
}
