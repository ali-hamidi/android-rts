package com.zeddic.war;

import javax.microedition.khronos.opengles.GL10;

import android.accounts.NetworkErrorException;
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
public /*class*/enum Camera implements GameObject {
  INSTANCE;

  
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
  
  //private static final int INITIAL_ACTION_CODE=-1;
  //private static final boolean  USE_ON_ACTION_POINTER_DOWN_PATCH = false;
  
  // The panning and zoom state of the camera.
  private Point3d cameraPoint = new Point3d(0,0,0);
  
  // The panning and zoom state of the camera.
  private Point3d cameraScale = new Point3d(1,1,1);
  private boolean cameraScaleSet = false;

  // The last recorded touch input x/y
  private OnPressType lastPress = OnPressType.UNKNOWN;
  
  // Is the user dragging the camera now?
  private boolean dragging = false;

  //private int previousActionCode = INITIAL_ACTION_CODE;
  /**
   * Converts an X coordinate in screen space to world space based
   * on the current camera orientation.
   */
  public float convertToWorldX(float screenX) {
    return screenX - cameraPoint.x;
  }
  
  /**
   * Converts a Y coordinate in screen space to world space based
   * on the current camera orientation.
   */
  public float convertToWorldY(float screenY) {
    return screenY - cameraPoint.y;
  }

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
    /*
    if (USE_ON_ACTION_POINTER_DOWN_PATCH){
      if (event.getPointerCount()>1){
        if (actionCode != MotionEvent.ACTION_POINTER_UP){
          actionCode = MotionEvent.ACTION_POINTER_DOWN;
        }
      }
    }*/
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
    //Log.d("Martin",">>>>>Entering on Touch Event ");
    int action = e.getAction();
    int actionCode = action & MotionEvent.ACTION_MASK;

    // Special Case where Pinch is not being passed
    // Correctly.  this will do an onPinchCall as well as
    // and onMove
    /*
    if (USE_ON_ACTION_POINTER_DOWN_PATCH){
      if (((previousActionCode == INITIAL_ACTION_CODE)&&
          (e.getPointerCount()>1))||(false)){
        Log.d("Martin","--->Multi Action pinch Patch press down");
        onPinch(e);
        //dumpEvent(e);
        previousActionCode = MotionEvent.ACTION_POINTER_DOWN;
    }
    */
    switch (actionCode) {
      case MotionEvent.ACTION_DOWN:{
        //Log.d("Martin","---> Single Action press down");
        onPress(e);
        //dumpEvent(e);
        break;
      }
      case MotionEvent.ACTION_POINTER_DOWN:{
        //Log.d("Martin","--->Multi Action pinch press downStart");
        onPinch(e);
        //Log.d("Martin","--->Multi Action pinch press downEnd");
        //dumpEvent(e);
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
      //I think we should leave this on the off chance that future implementation will behave correctly.
      /*
      case MotionEvent.ACTION_POINTER_UP:{ 
        Log.d("Martin","--->Multi Action pinch press up"); 
        onRelease(e);
        break;
      }*/
      case MotionEvent.ACTION_MOVE: 
        //Log.d("Martin", "--->Action Move");
        //dumpEvent(e);
        onMove(e); 
        break;
      default: 
        Log.d("Martin", "--->UNKNOWN ACTION "+actionCode);
        onCancel(e); 
        break;
    }
    //Log.d("Martin","<<<<<Exiting on Touch Event ");
    //Log.d("Martin","***************************");
  }

  private void onPress(MotionEvent e) {
    lastPress = OnPressType.ONE;
    Point2d lastPoint = lastPress.getLastPoints()[0];
    if ((lastPoint.x == e.getX())&&
        (lastPoint.y == e.getY())) return;
    dragging = true;
    lastPoint.x = e.getX();
    lastPoint.y = e.getY();
  }

  private void onPinch(MotionEvent e){
    Log.d("Martin", "--->DOING onPinch Start ");
    lastPress = OnPressType.TWO;
    Point2d lastPoint = null;
    //We will do a check to verify that the points have changed
    Log.d("Martin", "--->DOING onPinch Start ");
    Log.d("Martin", "--->DOING onPinch index count "+lastPress.getIndex());
    Log.d("Martin", "--->DOING onPinch  pointer count "+ e.getPointerCount());
    if (lastPress.getIndex() != e.getPointerCount()) return;
    Log.d("Martin", "--->DOING onPinch after test");
    boolean equalPoints = true;
    for (int i = 0; ((i < lastPress.getIndex())&&(equalPoints==true));i++){
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
        lastPoint.y = e.getY(i);
      }
    }
    Log.d("Martin", "--->DOING onPinch End ");
  }
  
  private void onRelease(MotionEvent e) {
    lastPress = null;
    onMove(e);
    dragging = false;
    //previousActionCode = INITIAL_ACTION_CODE;
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
      lastPress.getCamerasOnMove().onMove(e, lastPress, cameraPoint, cameraScale);
    }
  }
  
  private void onCancel(MotionEvent e) {
    dragging = false;
    //previousActionCode = INITIAL_ACTION_CODE;
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
    gl.glScalef(cameraScale.x,cameraScale.y,cameraScale.z);
    //gl.glScalex((int)cameraScale.x,(int)cameraScale.y,(int)cameraScale.z);
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
    cameraScale.x = 1;
    cameraScale.y = 1;
    cameraScale.z = 1;
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
    public void onMove(MotionEvent e, OnPressType lastPress, Point3d cameraPoint,Point3d cameraScale);
  }
  
  public static class MovePoint implements CamerasOnMove{

    @Override
    public void onMove(MotionEvent e, OnPressType lastPress, Point3d cameraPoint, Point3d cameraScale) {
      //Log.d("Martin", "OnMove on Point");
      Point2d lastPoint = lastPress.getLastPoints()[0];
      float newX = e.getX();
      float newY = e.getY();
      
      cameraPoint.x += newX - lastPoint.x;
      cameraPoint.y += newY - lastPoint.y;
      
      lastPoint.x = newX;
      lastPoint.y = newY;
    }
    
  }

  
  public static double distance (float x1, float y1, float x2, float y2){
    return Math.sqrt(Math.pow((x2-x1), 2)-Math.pow((y2-y1),2));
  }
  
  public static class MovePinch implements CamerasOnMove{
    private static final float SCALE_FACTOR = (float) 0.01;
    @Override
    public void onMove(MotionEvent e, OnPressType lastPress, Point3d cameraPoint, Point3d cameraScale) {
      Log.d("Martin", "OnMove on Pinch");
      Point2d point1 = lastPress.getLastPoints()[0];
      Point2d point2 = lastPress.getLastPoints()[1];
      
      double dist1 = Camera.distance(point1.x, point1.y, point2.x, point2.y);
      float newX1 = e.getX(0);
      float newY1 = e.getY(0);
      float newX2 = e.getX(1);
      float newY2 = e.getY(1);
      
      double dist2 = Camera.distance(newX1, newY1, newX2, newY2);
      
      if (dist1 < dist2){
        if (cameraScale.x<1.5) cameraScale.x +=SCALE_FACTOR;
        if (cameraScale.y<1.5) cameraScale.y +=SCALE_FACTOR;
//      if (cameraScale.Z<1.5) cameraScale.z +=SCALE_FACTOR;
      }else{
        if (cameraScale.x >.5) cameraScale.x -=SCALE_FACTOR;
        if (cameraScale.y >.5) cameraScale.y -=SCALE_FACTOR;
//        if (cameraScale.z > .5) cameraScale.z -=SCALE_FACTOR;
      }
      
      String zVal = "cp.x="+cameraPoint.x+"cp.y="+cameraPoint.y+"cp.z="+cameraPoint.z;
      Log.d("Martin", zVal);
      zVal = "cs.x="+cameraScale.x+"cs.y="+cameraScale.y+"cs.z="+cameraScale.z;
      Log.d("Martin", zVal);
      point1.x = newX1;
      point1.y = newY1;
      point2.x = newX2;
      point2.y = newY2;
   
    }
    
  }
}
