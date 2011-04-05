package com.zeddic.war;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;
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

//public static 

  
  public enum OnPressType { 
    UNKNOWN(0,null), 
    ONE(1, new MovePoint()), 
    TWO(2, new MovePinch());
    
    private final int index;   
    private final Point2d[] points;
    private final CamerasOnMove onMove;
    
    OnPressType(int index, CamerasOnMove onMove) {
      this.index = index;
      this.points = new Point2d[index];
      for (int i = 0 ; i < index; ++i){
        this.points[i]= new Point2d(0,0);
      }
      this.onMove = onMove;
    }
  
    public int getIndex()   { return index; }

    public Point2d[] getLastPoints() {return points;}
    
    public CamerasOnMove getCamerasOnMove() {return onMove;}
  };
  
  
  
  // The panning and zoom state of the camera.
  public Point3d cameraPoint = new Point3d(0,0,0);
  
  // The last recorded touch input x/y
  private OnPressType lastPress = OnPressType.UNKNOWN;
  
  // Is the user dragging the camera now?
  private boolean dragging = false;

  /**
   * Simple dump 
   */
  private void dumpEvent(MotionEvent event) {
    String names[] = { "DOWN" , "UP" , "MOVE" , "CANCEL" , "OUTSIDE" ,
       "POINTER_DOWN" , "POINTER_UP" , "7?" , "8?" , "9?" };
    StringBuilder sb = new StringBuilder();
    int action = event.getAction();
    int actionCode = action & MotionEvent.ACTION_MASK;
    sb.append("event ACTION_" ).append(names[actionCode]);
    if (actionCode == MotionEvent.ACTION_POINTER_DOWN
          || actionCode == MotionEvent.ACTION_POINTER_UP) {
       sb.append("(pid " ).append(
       action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
       sb.append(")" );
    }
    sb.append("[" );
    for (int i = 0; i < event.getPointerCount(); i++) {
       sb.append("#" ).append(i);
       sb.append("(pid " ).append(event.getPointerId(i));
       sb.append(")=" ).append((int) event.getX(i));
       sb.append("," ).append((int) event.getY(i));
       if (i + 1 < event.getPointerCount())
          sb.append(";" );
    }
    sb.append("]" );
    Log.d("Martin", sb.toString());
 }

  /**
   * Translates user input to pan and zoom commands.
   */
  public void onTouchEvent(MotionEvent e) {
    Log.d("Martin",">>>>>Entering on Touch Event ");
    int action = e.getAction();
    int actionCode = action & MotionEvent.ACTION_MASK;
    switch (actionCode) {
      case MotionEvent.ACTION_DOWN:{
        //Log.d("Martin","---> Single Action press down");
        onPress(e);
        //dumpEvent(e);
        break;
      }
      case MotionEvent.ACTION_POINTER_DOWN:{
        Log.d("Martin","--->Multi Action pinch press down");
        onPinch(e);
        dumpEvent(e);
        break;
      }
      case MotionEvent.ACTION_UP:{
        //Log.d("Martin", "--->Action press up");
        onRelease(e);
        //dumpEvent(e);
        break;
      }
      
      //The following case doesn't seem to occur
      //it appears that multipress or onMultPress release is not implemented.
      //I think we should leave this on th offchance that future implementation will behave correctly.
      //case MotionEvent.ACTION_POINTER_UP: Log.d("Martin","--->pinch press up"); break;
      
      case MotionEvent.ACTION_MOVE: 
        Log.d("Martin", "--->Action Move");
        dumpEvent(e);
        onMove(e); 
        break;
      default: onCancel(e); break;
    }
    Log.d("Martin","<<<<<Exiting on Touch Event ");
    Log.d("Martin","***************************");
  }

  private void onPress(MotionEvent e) {
    lastPress = OnPressType.ONE;
    Point2d lastPoint = lastPress.getLastPoints()[0];
    if ((lastPoint.x == e.getX())&&
        (lastPoint.y == e.getY())) return;
    dragging = true;
    lastPoint.x = e.getX();
    lastPoint.y = GameState.screenHeight-e.getY();
  }

  private void onPinch(MotionEvent e){
    lastPress = OnPressType.TWO;
    Point2d lastPoint = null;
    //We will do a check to verify that the points have changed
    String junk = "junk"+e.getPointerCount();
    Log.d("Martin",junk);
    if (lastPress.getIndex() != e.getPointerCount()) return;
    boolean equalPoints = true;
    for (int i = 0; ((i < lastPress.getIndex())||(equalPoints==true));i++){
      lastPoint = lastPress.getLastPoints()[i];
      if ((lastPoint.x != e.getX(i))||
          (lastPoint.y != e.getY(i))){
        equalPoints = false;
      }
    }
    // if the points different capture the points
    if (equalPoints == false){
      dragging = true;
      for (int i = 0; i < lastPress.getIndex();i++){
        lastPoint = lastPress.getLastPoints()[i];
        lastPoint.x = e.getX(i);
        lastPoint.y = GameState.screenHeight-e.getY(i);
      }
    }
  }
  
  private void onRelease(MotionEvent e) {
    lastPress = null;
    onMove(e);
    dragging = false;
  }
  
  private void onMove(MotionEvent e) {
    // In the off chance that the press was not registered previously
    if ((lastPress == null)||
        (lastPress == OnPressType.UNKNOWN )){
      return;
    }
    
    
    //In the situation that dragging
    if (!dragging) {
      return;
    }
    
    if (lastPress.getIndex()==e.getPointerCount()){
      lastPress.getCamerasOnMove().onMove(e, lastPress, cameraPoint);
    }
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
    gl.glTranslatef(cameraPoint.x, cameraPoint.y, cameraPoint.z);
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
    cameraPoint.x = 0;
    cameraPoint.y = 0;
    cameraPoint.z = 0;
  }
  
  //Public Static inner classes 
  public static class Point2d{
    public float x = 0;
    public float y = 0;
    
    public Point2d(float x, float y){
      this.x = x;
      this.y = y;
    }
    
  }
  
  public static class Point3d extends Point2d {
    public float z = 0;
    
    public Point3d(float x, float y, int z){
      super(x,y);
      this.z = z;
    }
  }

  static interface CamerasOnMove{
    public void onMove(MotionEvent e, OnPressType lastPress, Point3d cameraPoint);
  }
  
  public static class MovePoint implements CamerasOnMove{

    @Override
    public void onMove(MotionEvent e, OnPressType lastPress, Point3d cameraPoint) {
      Point2d lastPoint = lastPress.getLastPoints()[0];
      float newX = e.getX();
      float newY = GameState.screenHeight - e.getY();
      
      cameraPoint.x += newX - lastPoint.x;// lastX;
      cameraPoint.y += newY - lastPoint.y;//lastY;
      
      lastPoint.x = newX;//lastX = newX;
      lastPoint.y = newY;//lastY = newY;
    }
    
  }

  
  public static double distance (float x1, float y1, float x2, float y2){
    return Math.sqrt(Math.pow((x2-x1), 2)-Math.pow((y2-y1),2));
  }
  
  public static class MovePinch implements CamerasOnMove{

    @Override
    public void onMove(MotionEvent e, OnPressType lastPress, Point3d cameraPoint) {
      Point2d point1 = lastPress.getLastPoints()[0];
      Point2d point2 = lastPress.getLastPoints()[1];
      
      double dist1 = Camera.distance(point1.x, point1.y, point2.x, point2.y);
      double dist2 = Camera.distance(e.getX(0), e.getY(0), e.getX(1), e.getY(1));
      
      if (dist1 < dist2)
        cameraPoint.z +=1;
      else
        cameraPoint.z -=1;
      /*
      Point2d lastPoint = lastPress.getLastPoints()[0];
      float newX = e.getX();
      float newY = GameState.screenHeight - e.getY();
      
      cameraPoint.x += newX - lastPoint.x;// lastX;
      cameraPoint.y += newY - lastPoint.y;//lastY;
      
      lastPoint.x = newX;//lastX = newX;
      lastPoint.y = newY;//lastY = newY;
      */
    }
    
  }
}
