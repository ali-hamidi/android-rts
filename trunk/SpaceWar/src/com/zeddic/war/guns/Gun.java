package com.zeddic.war.guns;

import javax.microedition.khronos.opengles.GL10;

import com.zeddic.common.Entity;
import com.zeddic.common.GameObject;
import com.zeddic.common.util.Countdown;
import com.zeddic.war.GameState;
import com.zeddic.war.guns.control.GunControl;

public class Gun implements GameObject {
  
  protected Entity owner;
  protected int fireCooldown;
  protected float fireOffset;
  protected float bulletSpeed;
  protected float bulletDamage;
  protected GunControl control;
  protected boolean autoFire;
  protected float aimAngle = 0;
  protected int clipSize;
  protected long reloadTime;
  protected Class<? extends Bullet> bulletClass;
  protected int multiplier = 1;
  protected float multiplierStartAngle = 0;
  protected float multiplierAngleBetweenBullets = 0;
  protected float xOffset;
  protected float yOffset;
  protected long lastFire;
  private int clipCount;
  private Countdown reloadTimer;
  private boolean reloading;
  
  public Gun() {
    
  }
  
  public void init() {
    reloadTimer = new Countdown(reloadTime);
    reset();
  }
  
  protected void aimGun() {
    if (control != null) {
      control.aim(this);
    }
  }
  
  public boolean canFire() {
    return !reloading;
  }
  
  public boolean shouldFire() {
    return control != null ? control.shouldFire(this) : true;
  }

  public void fire() {

    if (!canFire() || !shouldFire()) {
      return;
    }

    long now = System.currentTimeMillis();
    long passedTime = now - lastFire;
    if (passedTime > fireCooldown) {
      long timesToFire = (fireCooldown == 0 ? clipCount : passedTime / fireCooldown);
      for (int i = 0 ; i < timesToFire ; i++) {
        
        fireOnce();

        clipCount--;
        if (clipCount <= 0) {
          reloadTimer.reset();
          reloadTimer.start();
          reloading = true;
          break;
        }
      }

      lastFire = (fireCooldown == 0 ? now : now - passedTime % fireCooldown);
    }
  }
  
  private void fireOnce() {

    aimGun();
    
    float fireAngle = multiplierStartAngle + aimAngle;
    
    for (int i = 0 ; i < multiplier ; i++) {
      
      Bullet bullet = GameState.stockpiles.bullets.take(bulletClass);
      if (bullet == null) {
        return;
      }
      bullet.x = owner.x + xOffset;
      bullet.y = owner.y + yOffset;
      bullet.angle = fireAngle;
      bullet.setVelocityBySpeed(fireAngle, bulletSpeed);
      bullet.offset(fireOffset);
      bullet.enable();
      bullet.life = 0;
      
      fireAngle += multiplierAngleBetweenBullets;
    }
  }
  
  @Override
  public void draw(GL10 gl) {
    // Nothing to draw.
  }
  
  public void reset() {
    lastFire = System.currentTimeMillis();
    reloading = false;
    clipCount = clipSize;
  }

  public void update(long time) {
    if (reloading) {
      reloadTimer.update(time);
      if (reloadTimer.isDone()) {
        reloading = false;
        clipCount = clipSize;
      }
    }

    if (autoFire) {
      fire();
    }
  }
  
  public void setOwner(Entity owner) {
    this.owner = owner;
  }
  
  public void setFireOffset(float fireOffset) {
    this.fireOffset = fireOffset;
  }
  
  public void setBulletSpeed(float bulletSpeed) {
    this.bulletSpeed = bulletSpeed;
  }
  
  public void setBulletDamage(float bulletDamage) {
    this.bulletDamage = bulletDamage;
  }
  
  public void setFireCooldown(int fireCooldown) {
    this.fireCooldown = fireCooldown;
  }
  
  public void setGunControl(GunControl control) {
    this.control = control;
  }
  
  public void setAutoFire(boolean autoFire) {
    this.autoFire = autoFire;
  }
  
  public void setAimAngle(float aimAngle) {
    this.aimAngle = aimAngle;
  }
 
  public void setClipSize(int clipSize) {
    this.clipSize = clipSize;
    this.clipCount = clipSize;
  }
  
  public void setReloadTime(long reloadTime) {
    this.reloadTime = reloadTime;
    this.reloadTimer.duration = reloadTime;
  }
  
  public float getAimAngle() {
    return aimAngle;
  }
}
