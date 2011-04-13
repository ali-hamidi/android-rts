package com.zeddic.war.ui;

import javax.microedition.khronos.opengles.GL10;

import android.view.MotionEvent;

import com.zeddic.common.AbstractGameObject;

/**
 * Renders the game 'heads up display'. This renders elements such as the
 * spawn buttons, the current amount of money, the current wave, menu buttons,
 * etc.
 * 
 * TODO(baileys): Add the money and wave indicators.
 * 
 * @author scott@zeddic.com (Scott Bailey)
 */
public class GameHud extends AbstractGameObject {

  // The button bar for spawning new ships.
  private SpawnInterface spawnInterface;
   
  public GameHud() {
    spawnInterface = new SpawnInterface();
  }
 
  @Override
  public void draw(GL10 gl) {
    spawnInterface.draw(gl);
  }

  @Override
  public void update(long time) {
    spawnInterface.update(time);
  }
  
  public boolean onTouch(MotionEvent e) {
    return spawnInterface.onTouch(e);
  }
  
  /**
   * Refreshes screen positions of HUD elements. Should be called whenever
   * the screen resizes or changes orientation.
   */
  public void reposition() {
    spawnInterface.reposition();
  }
}
