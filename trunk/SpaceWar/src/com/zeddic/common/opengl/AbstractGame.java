package com.zeddic.common.opengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public abstract class AbstractGame implements GLSurfaceView.Renderer {

  private long lastUpdate;

  @Override
  public void onSurfaceCreated(GL10 gl, EGLConfig config) {}
  
  @Override
  public void onSurfaceChanged(GL10 gl, int width, int height) {}

  @Override
  public void onDrawFrame(GL10 gl) {
    long now = System.currentTimeMillis();
    long delta = now - lastUpdate;
    lastUpdate = now;
    
    update(delta);
    draw(gl);
  }

  public void onTouchEvent(final MotionEvent event) {}

  public abstract void draw(GL10 gl);
  public abstract void update(long time);
}
