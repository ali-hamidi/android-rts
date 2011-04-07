package com.zeddic.common.opengl;

public class Color {

  public float r;
  public float g;
  public float b;
  public float a;
  
  public Color(int r, int g, int b, int a) {
    set(r, g, b, a);
  }
  
  public Color(float r, float g, float b, float a) {
    set(r, g, b, a);
  }

  public void set(float red, float green, float blue, float alpha) {
    r = red;
    g = green;
    b = blue;
    a = alpha;
  }
  
  public void set(int red, int green, int blue, int alpha) {
    r = (float) red / 255;
    g = (float) green / 255;
    b = (float) blue / 255;
    a = (float) alpha / 255;
  }
}
