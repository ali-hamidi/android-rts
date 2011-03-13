package com.zeddic.war.ships;

import android.graphics.Canvas;

import com.zeddic.game.common.PhysicalObject;

public interface Target {
  float getX();
  float getY();
  void set(float x, float y);
  void addFollower(PhysicalObject follower);
  void removeFollower(PhysicalObject follower);
  void update(long time);
  void draw(Canvas c);
  void addReachedHandler(Runnable handler);
}
