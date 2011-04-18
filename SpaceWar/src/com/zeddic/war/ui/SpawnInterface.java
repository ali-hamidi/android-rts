package com.zeddic.war.ui;

import javax.microedition.khronos.opengles.GL10;

import android.view.MotionEvent;

import com.zeddic.common.AbstractGameObject;
import com.zeddic.common.opengl.Color;
import com.zeddic.common.opengl.Screen;
import com.zeddic.common.opengl.Sprite;
import com.zeddic.common.util.Vector2d;
import com.zeddic.war.GameState;
import com.zeddic.war.R;
import com.zeddic.war.collision.CollisionSystem;
import com.zeddic.war.ships.FighterShip;

/**
 * A class that displays a user interface for selecting ships to
 * build and renders ships as the user places them into the world.
 * 
 * <p>A series of buttons are rendered at the bottom of the screen
 * that show the different ships available. The user can click and
 * drag ships off of these buttons and place them into the world.
 * The ships is placed upon releasing.
 * 
 * TODO(baileys): Add more ships to spawn.
 * TODO(baileys): Don't let ships spawn where there isn't room.
 * TODO(baileys): Hook into money.
 * TODO(baileys): Allow a way to 'cancel' a spawn by dropping the ship
 *     back on any button.
 * 
 * @author scott@zeddic.com (Scott Bailey)
 */
public class SpawnInterface extends AbstractGameObject {

  /** The number of pixels the spawning ship sprite should appear above the finger. */
  private static final float SPAWN_OFFSET = 80;
  

  private static final Color BAD_COLOR = new Color(255, 0, 0, 255);
  private static final Color GOOD_COLOR = new Color(0, 255, 0, 255);
  
  /** The sprite of the currently spawning ship. */
  private Sprite spawn;
  
  /** The buttons to choose from. */
  private ShipButton button1 = new ShipButton(R.drawable.ship, new Color(255, 0, 0, 255), 10);
  
  // Location of the last mouse event.
  private Vector2d last = new Vector2d();
  
  // Represent the screen and world location where the user is attempting to spawn.
  private Vector2d screen = new Vector2d();
  private Vector2d world = new Vector2d();
  
  // True if the user is currently in a spawn action.
  private boolean spawning = false;
  private static final Sprite spawnCircle = new Sprite(1, 1, R.drawable.spawn);
  
  public SpawnInterface() {
    positionButtons();    
  }

  private void positionButtons() {
    button1.y = Screen.height - 64;
    button1.x = Screen.width / 2;
  }
  
  /** Makes sure UI is centered on screen change. */
  public void reposition() {
    positionButtons();
  }

  public boolean onTouch(MotionEvent e) {
    switch (e.getAction()) {
      case MotionEvent.ACTION_DOWN: return onPress(e);
      case MotionEvent.ACTION_UP: return onRelease(e);
      case MotionEvent.ACTION_MOVE: return onMove(e);
      default: return onCancel(e);
    }
  }
  
  private boolean onPress(MotionEvent e) {
    
    // If it's within any button, start the spawn.
    if (button1.within(e)) {
      spawning = true;
      spawn = button1.sprite;
      record(e);
    }
    
    return spawning;
  }
  
  private boolean onRelease(MotionEvent e) {
    boolean handled = spawning;
    
    // If in a spawn action, this is the time to 
    // actually create the object.
    if (spawning) {
      
      // Convert the screen coordinates into a world coordinates.
      Vector2d screen = new Vector2d(e.getX(), e.getY() - SPAWN_OFFSET);
      Vector2d spawnAt = GameState.camera.convertToWorld(screen);
      spawn(spawnAt);
    }

    spawning = false;
    spawn = null;
    return handled;
  }
  
  private boolean onMove(MotionEvent e) {
    if (!spawning) {
      return false;
    }

    record(e);

    return true;
  }
  
  private boolean onCancel(MotionEvent e) {
    return onRelease(e);
  }
  
  /** 
   * Spawns a ship into the world as long as any are available in the stockpile. 
   */
  private void spawn(Vector2d worldPosition) {

    if (!isValidSpawnLocation(worldPosition)) {
      return;
    }

    FighterShip ship = GameState.stockpiles.ships.take(FighterShip.class);
    if (ship == null) {
      return;
    }
    
    ship.x = worldPosition.x;
    ship.y = worldPosition.y;
    ship.enable();
  }
  
  /** Records the last observed finger location on the screen. */
  private void record(MotionEvent e) {
    last.x = e.getX();
    last.y = e.getY();
  }

  @Override
  public void reset() {
    spawning = false;
  }
  
  @Override
  public void draw(GL10 gl) {
    button1.draw(gl);
    
    if (!spawning) {
      return;
    }

    Vector2d screen = getScreenSpawnLocation();
    boolean valid = isValidSpawnLocation(getSpawnLocation());
    
    Color color = valid ? GOOD_COLOR : BAD_COLOR;

    spawnCircle.x = screen.x;
    spawnCircle.y = screen.y;
    spawnCircle.setColor(color);
    
    // TODO(baileys): Replace this with the gun range of the active ship
    // the user is trying to spawn.
    spawnCircle.scale  = GameState.camera.convertToScreen(200 * 2);
    spawnCircle.draw(gl);
    
    spawn.x = screen.x;
    spawn.y = screen.y;
    spawn.draw(gl);
  }

  private Vector2d getScreenSpawnLocation() {
    screen.x = last.x;
    screen.y = Math.max(0, last.y - SPAWN_OFFSET);
    return screen;
  }

  private Vector2d getSpawnLocation() {
    screen = getScreenSpawnLocation();
    world = GameState.camera.convertToWorld(screen);
    return world;
  }

  private boolean isValidSpawnLocation(Vector2d world) {
    // TODO(baileys): Replace this with the radius of the active type of
    // ship.
    return !CollisionSystem.get().intersects(world.x, world.y, 8)
        && GameState.level.map.inMapArea(world.x, world.y);
  }

  @Override
  public void update(long time) {
    button1.update(time);
  }
}
