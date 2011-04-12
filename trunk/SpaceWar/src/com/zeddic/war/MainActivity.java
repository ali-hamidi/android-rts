package com.zeddic.war;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.zeddic.common.opengl.GameGLSurfaceView;

public class MainActivity extends Activity {
  private WarGame game;
  private GameGLSurfaceView glView;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    GameState.setup(this);
    
    game = new WarGame();
    glView = new GameGLSurfaceView(this, game);
    

    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(glView);
  }

  @Override
  protected void onResume() {
    // TODO(baileys): Get pause event to the game.
    super.onResume();
    glView.onResume();
  }

  @Override
  protected void onPause() {
    // TODO(baileys): Get pause event to the game.
    super.onPause();
    glView.onPause();
  }
  
  @Override
  public void onDestroy() {
    Log.d(MainActivity.class.getName(), "Destroying Game Activity");
    GameState.cleanup();
    super.onDestroy();
  }
}