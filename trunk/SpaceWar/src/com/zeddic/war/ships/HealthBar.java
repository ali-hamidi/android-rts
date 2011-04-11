package com.zeddic.war.ships;

import javax.microedition.khronos.opengles.GL10;

import com.zeddic.common.opengl.Color;
import com.zeddic.common.opengl.SimpleGeometry;

public class HealthBar {
  public float x;
  public float y;
  public int length;
  public float curHealth;
  
  private float maxHealth;
  
  private static final Color green = new Color(0, 255, 0, 255);
  private static final Color red = new Color(255, 0, 0, 255);
  
  public HealthBar(float maxHealth, int length) {
    this.maxHealth = maxHealth;
    this.length = length;
  }
  
  public void draw(GL10 gl) {
    if (curHealth == maxHealth) {
      return;
    }
    SimpleGeometry.drawLine(gl, x, y, x + length, y, red);
    SimpleGeometry.drawLine(gl, x, y, x + (length * (curHealth / maxHealth)), y, green);
  }
}
