package com.zeddic.common.util;

import android.graphics.Canvas;

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
  
  public void draw(Canvas canvas) {
    int size = children.size;
    for ( int i = 0 ; i < size ; i++) {
      children.items[i].draw(canvas);
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
