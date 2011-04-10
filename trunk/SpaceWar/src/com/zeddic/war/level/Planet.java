package com.zeddic.war.level;

import javax.microedition.khronos.opengles.GL10;

import com.zeddic.common.Entity;
import com.zeddic.common.opengl.Color;
import com.zeddic.common.opengl.Sprite;
import com.zeddic.common.util.Components;
import com.zeddic.common.util.Vector2d;
import com.zeddic.war.R;
import com.zeddic.war.collision.CollideBehavior;

public class Planet extends Entity {
  
  private static final Sprite sprite = new Sprite(128, 128, R.drawable.planet);
  private Components components = new Components();
  
  private Color color;
  
  public Planet() {
    this(0, 0);
  }

  public Planet(float x, float y) {
    this(x, y, new Color(255, 255, 255, 255));
  }

  public Planet(float x, float y, Color color) {
    super(x, y);
    this.color = color;
    this.radius = 50;
    this.enabled = true;
    this.collide.setBehavior(CollideBehavior.STATIONARY);
  }

  public void reset() {
    enable();
  }
  
  public void setColor(Color color) {
    this.color = color;
  }
  
  @Override
  public void update(long time) {
    super.update(time);
    components.update(time);
  }
  
  @Override
  public void draw(GL10 gl) {
    sprite.x = x;
    sprite.y = y;
    sprite.setColor(color);
    sprite.draw(gl);
  }

  @Override
  public void collide(Entity object, Vector2d avoidVector) { }
}
