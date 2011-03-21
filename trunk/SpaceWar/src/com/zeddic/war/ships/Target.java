package com.zeddic.war.ships;

import android.graphics.Canvas;

import com.zeddic.common.Entity;

public interface Target {
  float getX();
  float getY();
  void set(float x, float y);
  void addFollower(Entity follower);
  void removeFollower(Entity follower);
  void update(long time);
  void draw(Canvas c);
  void addReachedHandler(Runnable handler);
}
