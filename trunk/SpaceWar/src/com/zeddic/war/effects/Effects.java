package com.zeddic.war.effects;

import javax.microedition.khronos.opengles.GL10;

import com.zeddic.common.AbstractGameObject;
import com.zeddic.common.util.ObjectStockpile;
import com.zeddic.common.util.Vector2d;

/**
 * A collection of common particle effects used by the game. A singleton instance
 * of Effects may called with the desired x/y coordinates of one of the desired
 * effects. This class maintains a stockpile of all current effects and recycles
 * objects back into an object pool once the effect is over.
 * 
 * @author scott@zeddic.com (Scott Bailey)
 */
public class Effects extends AbstractGameObject {

  private static final Effects singleton = new Effects();
  
  private final ObjectStockpile stockpile;

  private Effects() {
    stockpile = new ObjectStockpile();
    createSupply();
  }
  
  private void createSupply() {
    stockpile.createSupply(Explosion.class, 50);
    stockpile.createSupply(ShockwaveExplosion.class, 500);
  }

  /**
   * Causes an explosion to be shown at the given world location.
   */
  public Explosion explode(float x, float y) {
    Explosion explosion = stockpile.getSupply(Explosion.class).take();
    if (explosion == null)
      return null;
    
    explosion.x = x;
    explosion.y = y;
    explosion.ignite();
    return explosion;
  }
  
  /**
   * Causes a pixel based shockwave to be shown at the given world location.
   */
  public ShockwaveExplosion shockwave(float x, float y) { 
    ShockwaveExplosion explosion = stockpile.getSupply(ShockwaveExplosion.class).take();
    if (explosion == null)
      return null;

    explosion.x = x;
    explosion.y = y;
    explosion.ignite();
    return explosion;
  }

  @Override
  public void reset() {
    stockpile.reset();
  }
  
  @Override
  public void draw(GL10 gl) {
    stockpile.draw(gl);
  }
  
  @Override
  public void update(long time) {
    stockpile.update(time);
  }
  
  public static Effects get() {
    return singleton;
  }
}
