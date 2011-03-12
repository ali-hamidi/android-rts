package com.zeddic.war.ships;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class LocationTarget implements Target {

  private static final Paint PAINT;
  static {
    PAINT = new Paint();
    PAINT.setColor(Color.RED);
    PAINT.setStyle(Paint.Style.STROKE);
    PAINT.setStrokeWidth(3);
  }
  
  private float x;
  private float y;
  
  public LocationTarget(float x, float y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public float getX() {
    return x;
  }

  @Override
  public float getY() {
    return y;
  }

  @Override
  public void update(long time) {
  }

  @Override
  public void draw(Canvas c) {
    c.drawCircle(x, y, 10, PAINT);
  }

  @Override
  public void set(float x, float y) {
    this.x = x;
    this.y = y;
  }
}
