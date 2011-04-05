package com.zeddic.war;

import javax.microedition.khronos.opengles.GL10;

import com.zeddic.common.Entity;
import com.zeddic.common.opengl.Sprite;
import com.zeddic.common.transistions.Transition;
import com.zeddic.common.transistions.Transitions.TransitionType;
import com.zeddic.common.util.Components;
import com.zeddic.common.util.Vector2d;

public class Planet extends Entity {
  
  private Sprite sprite = new Sprite(128, 128, R.drawable.moon);
  private Transition transition = new Transition(0, 360, 600000, TransitionType.LINEAR);
  private Components components = new Components();
  
  
  public Planet() {
    this(0, 0);
  }
  
  public Planet(float x, float y) {
    super(x, y);  
    transition.setAutoReset(true);
    
    components.add(transition);
    
    //components = new Components();
    //components.add(path);
  }
  
  public void reset() {
    enable();

  }
  
  @Override
  public void update(long time) {
    super.update(time);
    components.update(time);
  }
  
  @Override
  public void draw(GL10 gl) {
    
    sprite.rz = transition.get();
    sprite.draw(gl);

    components.draw(gl);
  }

  @Override
  public void collide(Entity object, Vector2d avoidVector) { }
}
