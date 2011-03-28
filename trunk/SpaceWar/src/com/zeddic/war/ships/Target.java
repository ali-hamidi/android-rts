package com.zeddic.war.ships;

import javax.microedition.khronos.opengles.GL10;

import com.zeddic.common.Entity;

public interface Target {
  float getX();
  float getY();
  void set(float x, float y);
  void addFollower(Entity follower);
  void removeFollower(Entity follower);
  void update(long time);
  void draw(GL10 gl);
  void addReachedHandler(Runnable handler);
}
