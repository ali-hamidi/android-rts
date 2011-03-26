package com.zeddic.common.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class GameGLSurfaceView extends GLSurfaceView {
  
  private AbstractGame mRenderer;
  
  public GameGLSurfaceView(Context context) {
      super(context);
      mRenderer = new AbstractGame();
      setRenderer(mRenderer);
  }

  public boolean onTouchEvent(final MotionEvent event) {
    queueEvent(new Runnable(){
        public void run() {
            //mRenderer.setColor(event.getX() / getWidth(),
            //        event.getY() / getHeight(), 1.0f);
        }});
        return true;
    }      
}
