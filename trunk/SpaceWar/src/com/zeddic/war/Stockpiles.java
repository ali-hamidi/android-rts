package com.zeddic.war;

import javax.microedition.khronos.opengles.GL10;

import com.zeddic.common.AbstractGameObject;
import com.zeddic.common.util.ObjectStockpile;
import com.zeddic.war.guns.Bullet;
import com.zeddic.war.ships.FighterShip;
import com.zeddic.war.ships.Square;

public class Stockpiles extends AbstractGameObject {

  public ObjectStockpile bullets;
  public ObjectStockpile ships;
  
  public Stockpiles() {
    bullets = new ObjectStockpile();
    ships = new ObjectStockpile();
  }
  
  public void populate() {
    bullets.createSupply(Bullet.class, 300);
    
    ships.createSupply(FighterShip.class, 100);
    ships.createSupply(Square.class, 50);
  }
  
  public void reset() {
    bullets.reset();
    ships.reset();
  }
  
  @Override
  public void draw(GL10 gl) {
    bullets.draw(gl);
    ships.draw(gl);
  }
  
  @Override
  public void update(long time) {
    bullets.update(time);
    ships.update(time);
  }
}
