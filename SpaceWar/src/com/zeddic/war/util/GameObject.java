package com.zeddic.war.util;

import android.graphics.Canvas;

public interface GameObject {
  void draw(Canvas canvas);
  void update(long time);
  void reset();
}
