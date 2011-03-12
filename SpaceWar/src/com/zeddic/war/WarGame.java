package com.zeddic.war;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import com.zeddic.game.common.Game;
import com.zeddic.game.common.util.Countdown;
import com.zeddic.war.collision.CollisionSystem;
import com.zeddic.war.collision.ProximityUtil;
import com.zeddic.war.level.MockLevelLoader;
import com.zeddic.war.ships.FighterShip;
import com.zeddic.war.ships.LocationTarget;


public class WarGame extends Game {

  public static final int GAME_EVENT_WIN = 0;
  public static final int GAME_EVENT_DEAD = 1;
  public static final int GAME_EVENT_LOADED = 2;
  public static final int GAME_EVENT_PAUSED = 3;
  public static final int GAME_EVENT_STARTED = 4;
  
  private static final int GAME_STATE_SETUP = 0;
  private static final int GAME_STATE_PLAYING = 1;
  private static final int GAME_STATE_PROMPT = 2;
  private static final int GAME_STATE_PAUSED = 3;
  
  private static final long FIRST_SPAWN_DELAY = 0;
  
  private int gameState;
  private static final int MAX_WORLD_WIDTH = 2000;
  private static final int MAX_WORLD_HEIGHT = 2000;

  private Countdown pauseButtonCooldown = new Countdown(100);
  
  
  private BattleCommandManager commandManager;
  //private FighterShip ship;
  
  public WarGame() {

  }
  
  @Override
  public void init(int screenWidth, int screenHeight) {
    if (this.initialized)
      return;
    
    Log.d(WarGame.class.getName(), "Size: " + screenWidth + "x" + screenHeight);
    
    GameState.setScreen(screenWidth, screenHeight);
    
    // Setup the collision system.
    //CollisionManager.setup(MAX_WORLD_WIDTH, MAX_WORLD_HEIGHT);

    // Populate the GameState
    //GameState.player = new Player();

    
    // Should be scaling world sizes to the window instead of just making
    // the world as big as the screen... :(
    // That or making the world scrollable as needed.
    
    GameState.level = new MockLevelLoader(screenWidth, screenHeight).load("blah");
    CollisionSystem.get().initializeForLevel(GameState.level);

    commandManager = new BattleCommandManager();
    
    //GameState.map = new Map();
    //GameState.map.setSize(screenWidth, screenHeight);
    
    // Create the enemies and reusable game objects. 
    GameState.stockpiles = new Stockpiles();
    GameState.stockpiles.populate();
    
    
    FighterShip ship = GameState.stockpiles.ships.take(FighterShip.class);
    ship.x = screenWidth / 2;
    ship.y = screenHeight / 2;
    ship.enable();

    ship = GameState.stockpiles.ships.take(FighterShip.class);
    ship.x = screenWidth / 3;
    ship.y = screenHeight / 3;
    ship.enable();
    

    initialized = true;
    triggerEvent(GAME_EVENT_LOADED);
    
    gameState = GAME_STATE_PLAYING;
    
  }
  
  private void endGame() {
    gameState = GAME_STATE_PROMPT;
    updater.triggerEventHandler(GAME_EVENT_DEAD);
  }
  
  private void winGame() {
    gameState = GAME_STATE_PROMPT;
    updater.triggerEventHandler(GAME_EVENT_WIN);
  }
  
  private void resetGameObjects() {
    //GameState.stockpiles.reset();
   // GameState.player.reset();
    GameState.effects.reset();
  }

  
  public void restart() {
    
  }
  
  public void pause() {
    super.pause();
    gameState = GAME_STATE_PAUSED;
    triggerEvent(GAME_EVENT_PAUSED);
  }
  
  public void resume() {
    super.resume();
    gameState = GAME_STATE_PLAYING;
  }
  
  @Override
  public void draw(Canvas c) {
    //ship.draw(c);

    CollisionSystem.get().draw(c);
    commandManager.draw(c);
    GameState.level.draw(c);
    GameState.stockpiles.draw(c);
    
  }
  
  @Override
  public void update(long time) {
    if (gameState != GAME_STATE_PLAYING) {
       return;
    }
    //ship.update(time);

    GameState.stockpiles.update(time);
  }

  //// USER INPUT

  public boolean onTouchEvent(MotionEvent e) {

    commandManager.onTouch(e);
    
    return true;
  }
}
