package com.zeddic.common.opengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;

public class AbstractGame implements GLSurfaceView.Renderer {

  @Override
  public void onDrawFrame(GL10 gl) {
    
  }

  @Override
  public void onSurfaceChanged(GL10 gl, int width, int height) {
    gl.glViewport(0, 0, width, height);
  }

  @Override
  public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    
  }
}
