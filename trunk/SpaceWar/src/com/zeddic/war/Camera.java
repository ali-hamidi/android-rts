package com.zeddic.war;

import javax.microedition.khronos.opengles.GL10;

import android.util.FloatMath;
import android.view.MotionEvent;

import com.zeddic.common.GameObject;
import com.zeddic.common.util.Vector2d;

/**
 * A camera simulates zooming and panning within the game world.
 * The camera should be applied to the gl matrix stack before
 * drawing any world objects.
 * 
 * @author Scott Bailey
 */
public /*class*/enum Camera implements GameObject {
  INSTANCE;

  /** 
   * The translations of the camera in _world_ coordinates. For example,
   * -50, -50 would mean the camera is currently showing world position
   * 50, 50 in the very top-left most point on the screen.
   */
  public float x = 0;
  public float y = 0;
  
  /** Any zoom scale applied to the camera. For example: 2 would be 2x zoom. */
  public float scale = 1;
  
  /** The last known _screen_ coordinates for finger1. */
  private Vector2d lastPoint1 = new Vector2d(0, 0);
  
  /** The last known _screen_ coordinates for finger2. */
  private Vector2d lastPoint2 = new Vector2d(0, 0);
  
  /** A point, in world coordinates, that any zoom actions should focus on. */
  private Vector2d zoomAnchor;

  /** If true, system must collect at least data point before allowing a pan. */
  private boolean needPanData = true;

  /**
   * Translates user input to pan and zoom commands.
   */
  public void onTouchEvent(MotionEvent e) {
    int action = e.getAction();
    int actionCode = action & MotionEvent.ACTION_MASK;
    switch (actionCode) {
      case MotionEvent.ACTION_DOWN: onPress(e); break;
      case MotionEvent.ACTION_POINTER_DOWN: onPinchStart(e); break;
      case MotionEvent.ACTION_UP: onRelease(e); break;
      case MotionEvent.ACTION_MOVE: onMove(e); break;
      default: onCancel(e); break;
    }
  }

  private void onPress(MotionEvent e) {
    recordInput(e, 0, lastPoint1);
  }
  
  private void onPinchStart(MotionEvent e){
    recordInput(e, 0, lastPoint1);
    recordInput(e, 1, lastPoint2);
    
    // Whenever a zoom gesture starts, center the anchor on the
    // point exactly between both fingers.
    anchorZoom();
  }

  private void onMove(MotionEvent e) {
    if (e.getPointerCount() > 1) {
      onZoomMove(e);
    } else {
      onPanMove(e);
    }
  }
  
  private void onZoomMove(MotionEvent e) {
    
    // Determine how much the fingers have moved part.
    float initialDistance = getDistance(lastPoint1, lastPoint2);
    recordInput(e, 0, lastPoint1);
    recordInput(e, 1, lastPoint2);
    
    // Whenever a zoom gesture starts, center the anchor on the
    // point exactly between both fingers.
    if (!hasZoomAnchor()) {
      anchorZoom();
    }

    float newDistance = getDistance(lastPoint1, lastPoint2);
    float delta = (newDistance - initialDistance);
    
    // Update the scale based on this change. Multiply the delta by 
    // scale so the zoom rate decreases the more you zoom out.
    scale +=  delta * scale / 400;
    
    // After zooming, make sure the camera still shows
    // our zoom anchor exactly between both fingers.
    placeWorldPositionAtScreenPosition(
        zoomAnchor,
        getMidpoint(lastPoint1, lastPoint2));
  }

  private void onPanMove(MotionEvent e) {
    // Don't allow panning until a zoom operation has finished.
    if (hasZoomAnchor()) {
      return;
    }

    // Panning is based on deltas from the last known finger
    // position. Ensure we have at least two good data points.
    if (needPanData) {
      recordInput(e, 0, lastPoint1);
      needPanData = false;
    }

    // x and y always represent world coordinates. Convert the
    // screen translation into world space before updating them.
    x += (e.getX() - lastPoint1.x) / scale;
    y += (e.getY() - lastPoint1.y) / scale;
    recordInput(e, 0, lastPoint1);
  }
  
  private void onRelease(MotionEvent e) {
    zoomAnchor = null;
    needPanData = true;
  }

  private void onCancel(MotionEvent e) {
    zoomAnchor = null;
    needPanData = true;
  }
  
  /**
   * Re-centers the camera so that the given world coordinates are displayed at
   * the given pixel point on the screen. For example, the camera could be set
   * to place a game object at world position 1000x1000 at the top left of the screen
   * at pixel 10x10.
   */
  public void placeWorldPositionAtScreenPosition(Vector2d world, Vector2d screen) {    
    float desiredWorldX = screen.x / scale;
    float desiredWorldY = screen.y / scale;
    
    x = (-world.x + desiredWorldX);
    y = (-world.y + desiredWorldY);
  }

  /**
   * Applys the camera open gl transformation. Functionally this works by 
   * translating the entire world in the inverse direction of the cameras
   * movement. {@link #end} should be called after all entities affected
   * by the camera have been drawn.
   */
  public void apply(GL10 gl) {
    gl.glPushMatrix();
    gl.glScalef(scale, scale, 0);
    gl.glTranslatef(x, y, 0);
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
    scale = 1;
    zoomAnchor = null;
    needPanData = true;
  }
  
  /**
   * Converts screen coordinates to world coordinates.
   */
  public Vector2d convertToWorld(Vector2d screen) {
    return new Vector2d(
        (screen.x  / scale - x),
        (screen.y / scale) - y);
  }
  
  public Vector2d convertToWorld(MotionEvent e) {
    return convertToWorld(new Vector2d(e.getX(), e.getY()));
  }
  
  /**
   * Anchors the zoom camera to the now current midpoint of the user's
   * finger.
   */
  private void anchorZoom() {
    zoomAnchor = convertToWorld(getMidpoint(lastPoint1, lastPoint2));
  }
  
  /**
   * Returns true if there is some anchor that zooming is being done
   * around.
   */
  private boolean hasZoomAnchor() {
    return zoomAnchor != null;
  }
  
  // Utility methods.
  
  private static Vector2d getMidpoint(Vector2d point1, Vector2d point2) {
    return new Vector2d(
        point1.x + (point2.x - point1.x) / 2,
        point1.y + (point2.y - point1.y) / 2);
  }
  
  private static final float getDistance(Vector2d point1, Vector2d point2) {
    float dX = (point1.x - point2.x);
    float dY = (point1.y - point2.y);
    return FloatMath.sqrt(dX * dX + dY * dY);
  }
  
  /** Stores a motion event into a vector. */
  private static final void recordInput(MotionEvent e, int motionIndex, Vector2d saveIn) {
    saveIn.x = e.getX(motionIndex);
    saveIn.y = e.getY(motionIndex);
  }
}
