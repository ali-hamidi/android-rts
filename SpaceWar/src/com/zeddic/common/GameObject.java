package com.zeddic.common;

import javax.microedition.khronos.opengles.GL10;

public interface GameObject {
  void draw(GL10 gl);
  void update(long time);
  void reset();
}
