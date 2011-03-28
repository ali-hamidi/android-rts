package com.zeddic.common.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class GameGLSurfaceView extends GLSurfaceView {
  
  private AbstractGame game;
  
  public GameGLSurfaceView(Context context, AbstractGame game) {
      super(context);
      this.game = game;
      setRenderer(game);
  }

  public boolean onTouchEvent(final MotionEvent event) {
    queueEvent(new Runnable(){
      public void run() {
        game.onTouchEvent(event);
      }});
      return true;
    }      
}
