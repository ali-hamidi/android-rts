package com.zeddic.war.ships;

import android.graphics.Canvas;

public interface Target {
  float getX();
  float getY();
  void set(float x, float y);
  void update(long time);
  void draw(Canvas c);
}
