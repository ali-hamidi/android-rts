package com.zeddic.common.opengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.zeddic.war.GameState;

public abstract class AbstractGame implements GLSurfaceView.Renderer {

  private static final int MILLIS_PER_SECOND = 1000;
  private static final boolean ENABLE_FPS = true;
  
  private long lastUpdate;
  private long lastFpsDisplay;
  private long fps = 0;
  
  private static final int FPS_WIDTH = 64;
  private static final int FPS_HEIGHT = 64;
  private TextSprite fpsSprite = new TextSprite(FPS_WIDTH, FPS_HEIGHT);
  
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
    
    if (lastFpsDisplay < 500) {
      lastFpsDisplay += delta;
    } else {
      lastFpsDisplay = 0;
      fps = MILLIS_PER_SECOND / delta;
    }
    
    if (ENABLE_FPS) {
      displayFps(gl);
    }
  }

  private void displayFps(GL10 gl) {
    fpsSprite.x = FPS_WIDTH / 2;
    fpsSprite.y = GameState.screenHeight - (FPS_HEIGHT / 2);
    fpsSprite.setText(String.valueOf(fps));
    fpsSprite.draw(gl);
  }

  public void onTouchEvent(final MotionEvent event) {}

  public abstract void draw(GL10 gl);
  public abstract void update(long time);
}
