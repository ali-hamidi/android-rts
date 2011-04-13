package com.zeddic.war.ui;

import android.view.MotionEvent;

import com.zeddic.common.AbstractGameObject;

/**
 * Abstract class for any user interface objects that the user may work with,
 * such as a button.
 * 
 * @author scott@zeddic.com (Scott Bailey)
 */
public abstract class AbstractUiObject extends AbstractGameObject {

  public float x = 0;
  public float y = 0;
  public float width = 0;
  public float height = 0;
  
  public float getLeft() {
    return x- width / 2;
  }
  
  public float getRight() {
    return x + width / 2;
  }
  
  public float getTop() {
    return y - height / 2;
  }
  
  public float getBottom() {
    return y + height / 2;
  }
  
  /**
   * Returns true if the given motion event takes place within this
   * UI element.
   */
  public boolean within(MotionEvent e) {
    if (e.getPointerCount() == 0) {
      return false;
    }

    float eX = e.getX();
    float eY = e.getY();

    return eY > getTop()
        && eY < getBottom()
        && eX > getLeft()
        && eX < getRight();
  }
}
