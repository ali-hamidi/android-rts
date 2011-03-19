package com.zeddic.war;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zeddic.common.GameSurface;
import com.zeddic.common.Updater;

public class MainActivity extends Activity {
	private GameSurface surface;
  private WarGame game;
  private Updater updater;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.main);
    
    GameState.setup(this);
    
    // Create the drawing Surface;
    surface = new GameSurface(this);
    surface.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    
    // Create the game.
    game = new WarGame();
    
    // Create the updater to coordinate the background update/render thread.
    updater = new Updater(surface);
    updater.setGame(game);
    updater.showFps(true);
    
    // Set the updater to listen to various game events and trigger
    // appropriate dialog boxes. The game can't do this itself from a non-UI
    // thread.
    updater.addEventHandler(WarGame.GAME_EVENT_DEAD, new DeadHandler());
    updater.addEventHandler(WarGame.GAME_EVENT_LOADED, new LoadedHandler());
    
    // Add the drawing surface to the screen.
    LinearLayout container = (LinearLayout)findViewById(R.id.game);
    container.addView(surface);
    
    showMessage("Loading");
    // Start the background thread which will do our setup.
    updater.start();
  }
  
  @Override
  public void onDestroy() {
    Log.d(MainActivity.class.getName(), "Destroying Game Activity");
    updater.stop();
    GameState.cleanup();
    super.onDestroy();
  }
    
  private void showMessage(String message) {    
    TextView messageText = (TextView)findViewById(R.id.gameMessageText);
    messageText.setText(message);
    
    LinearLayout messageContainer = (LinearLayout)findViewById(R.id.gameMessageContainer);
    messageContainer.setVisibility(View.VISIBLE);
  }
  
  private void hideMessage() {
    LinearLayout message = (LinearLayout)findViewById(R.id.gameMessageContainer);
    message.setVisibility(View.INVISIBLE);
  }
  
  private class LoadedHandler extends Handler {
    
    @Override
    public void handleMessage(Message msg) {
      hideMessage();
    }
  }
  
  
  private class DeadHandler extends Handler {
    
    @Override
    public void handleMessage(Message msg) {
      AlertDialog dialog = new AlertDialog.Builder(GameState.context)
          .setTitle("Game Over")
          .create();
      
      dialog.setButton(
          AlertDialog.BUTTON1, "Play Again",
          new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
              game.restart();
            }
          });
      
      dialog.setButton(
          AlertDialog.BUTTON2, "Quit",
          new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
              GameState.activity.finish();
            }
          });

      dialog.show();
    }
  }
}