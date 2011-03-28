package com.zeddic.common.util;

import javax.microedition.khronos.opengles.GL10;

import com.zeddic.common.AbstractGameObject;
import com.zeddic.common.GameObject;

public class Components extends AbstractGameObject {

  SimpleList<GameObject> children;
  
  public Components() {
    this.children = SimpleList.create(GameObject.class);
  }
  
  public void reset() {
    int size = children.size;
    for ( int i = 0 ; i < size ; i++) {
      children.items[i].reset();
    }
  }
  
  public void draw(GL10 gl) {
    int size = children.size;
    for ( int i = 0 ; i < size ; i++) {
      children.items[i].draw(gl);
    }
  }
  
  public void update(long time) {
    int size = children.size;
    for ( int i = 0 ; i < size ; i++) {
      children.items[i].update(time);
    }
  }
  
  public void add(GameObject child) {
    children.add(child);
  }
}
