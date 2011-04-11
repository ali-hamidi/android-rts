package com.zeddic.war;

import javax.microedition.khronos.opengles.GL10;

import com.zeddic.common.AbstractGameObject;
import com.zeddic.common.util.ObjectStockpile;
import com.zeddic.war.guns.Bullet;
import com.zeddic.war.guns.ShardBullet;
import com.zeddic.war.ships.FighterShip;
import com.zeddic.war.ships.Square;

public class Stockpiles extends AbstractGameObject {

  //public ObjectStockpile enemies;
  public ObjectStockpile bullets;
  //public ObjectStockpile ui;
  public ObjectStockpile ships;
  
  public Stockpiles() {
    //enemies = new ObjectStockpile();
    
    bullets = new ObjectStockpile();
    ships = new ObjectStockpile();
    //ui = new ObjectStockpile();
  }
  
  public void populate() {
    /*enemies.createSupply(SimpleEnemyShip.class, 100);
    enemies.createSupply(DeathStar.class, 50);
    enemies.createSupply(DaBomb.class, 50);
    enemies.createSupply(Blinker.class, 50);
    enemies.createSupply(Arrow.class, 100); */
    
    bullets.createSupply(Bullet.class, 300);
    
    ships.createSupply(FighterShip.class, 100);
    ships.createSupply(Square.class, 50);
    
    //ui.createSupply(TextFlash.class, 10);
  }
  
  public void reset() {
    //enemies.reset();
    bullets.reset();
    ships.reset();
    //ui.reset();
  }
  
  @Override
  public void draw(GL10 gl) {
    //enemies.draw(c);
    bullets.draw(gl);
    ships.draw(gl);
    //ui.draw(c);
  }
  
  @Override
  public void update(long time) {
    //enemies.update(time);
    bullets.update(time);
    ships.update(time);
    //ui.update(time);
  }
  
  /*public void killAllEnemies() {
    EnemyShip ship;
    for (ObjectPoolManager<? extends GameObject> set : enemies.supply.values()) {
      for (int i = 0 ; i < set.pool.items.length; i++) {
        ship = ((EnemyShip) set.pool.items[i]);
        if (ship.active) {
          ship.die();
        }
      }
    }
  } */
}
