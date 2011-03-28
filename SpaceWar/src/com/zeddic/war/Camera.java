package com.zeddic.war;

import javax.microedition.khronos.opengles.GL10;

import android.view.MotionEvent;

import com.zeddic.common.GameObject;

/**
 * A camera simulates zooming and panning within the game world.
 * The camera should be applied to the gl matrix stack before
 * drawing any world objects.
 * 
 * @author Scott Bailey
 */
public class Camera implements GameObject {

  // The panning and zoom state of the camera.
  public float x = 0;
  public float y = 0;
  public float z = 0;
  
  // The last recorded touch input x/y.
  private float lastX;
  private float lastY;
  
  // Is the user dragging the camera now?
  private boolean dragging = false;

  /**
   * Translates user input to pan and zoom commands.
   */
  public void onTouchEvent(MotionEvent e) {
    switch (e.getAction()) {
      case MotionEvent.ACTION_DOWN: onPress(e); break;
      case MotionEvent.ACTION_UP: onRelease(e); break;
      case MotionEvent.ACTION_MOVE: onMove(e); break;
      default: onCancel(e); break;
    }
  }

  private void onPress(MotionEvent e) {
    dragging = true;
    lastX = e.getX();
    lastY = GameState.screenHeight - e.getY();
  }

  private void onRelease(MotionEvent e) {
    onMove(e);
    dragging = false;
  }
  
  private void onMove(MotionEvent e) {
    if (!dragging) {
      return;
    }
    
    float newX = e.getX();
    float newY = GameState.screenHeight - e.getY();
    
    x += newX - lastX;
    y += newY - lastY;
    
    lastX = newX;
    lastY = newY;
  }
  
  private void onCancel(MotionEvent e) {
    dragging = false;
  }
  
  /**
   * Applys the camera open gl transformation. Functionally this works by 
   * translating the entire world in the inverse direction of the cameras
   * movement. {@link #end} should be called after all entities affected
   * by the camera have been drawn.
   */
  public void apply(GL10 gl) {
    gl.glPushMatrix();
    gl.glTranslatef(x, y, z);
  }
  
  /**
   * Ends the transformations for the camera. These should be ended before
   * drawing anything in the global 'screen' space, such as menus, that are
   * unaffected by camera movement.
   */
  public void end(GL10 gl) {
    gl.glPopMatrix();
  }

  @Override
  public void draw(GL10 gl) { }

  @Override
  public void update(long time) { }

  @Override
  public void reset() {
    x = 0;
    y = 0;
    z = 0;
  }
}
