package com.zeddic.war.ships;

import javax.microedition.khronos.opengles.GL10;

import com.zeddic.common.Entity;
import com.zeddic.common.opengl.Sprite;
import com.zeddic.common.util.Components;
import com.zeddic.war.R;
import com.zeddic.war.collision.CollideBehavior;
import com.zeddic.war.guns.Arsenal;
import com.zeddic.war.guns.Gun;
import com.zeddic.war.guns.control.EnemyAimingGunControl;

public class FighterShip extends Entity {

  private Gun gun;
  private Components components;
  private float speed;
  private StraightPath path;
  private Sprite sprite = new Sprite(40, 40, R.drawable.ship);

  public FighterShip() {
    this(0, 0);
  }

  public FighterShip(float x, float y) {
    super(x, y);

    this.radius = 8;
    this.speed = 100;
    this.collide.setBehavior(CollideBehavior.HIT_RECEIVE);
    
    path = new StraightPath(this, speed);
    gun = Arsenal.getPeaShooter(this);
    gun.setGunControl(new EnemyAimingGunControl(this, 200));
    
    components = new Components();
    components.add(path);
    components.add(gun);
  }
  
  public void reset() {
    enable();
    gun.reset();
  }
  
  public void spawn(float x, float y) {
    reset();
    this.x = x;
    this.y = y;
  }

  @Override
  public void update(long time) {
    super.update(time);
    gun.setAutoFire(path.getTarget() == null);
    components.update(time);
  }
  
  @Override
  public void draw(GL10 gl) {
    sprite.x = x;
    sprite.y = y;
    sprite.rz = angle;
    sprite.draw(gl);
    components.draw(gl);
  }
  
  public void setTarget(Target target) {
    this.path.setTarget(target);
  }

  public Target getTarget() {
    return this.path.getTarget();
  }
}
