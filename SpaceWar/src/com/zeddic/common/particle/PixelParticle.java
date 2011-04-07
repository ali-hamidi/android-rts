package com.zeddic.common.particle;

import javax.microedition.khronos.opengles.GL10;

import com.zeddic.common.opengl.Color;
import com.zeddic.common.opengl.SimpleGeometry;

public class PixelParticle extends Particle {

  private Color color = new Color(0, 255, 0, 255);
  
  public PixelParticle() {
    super(0, 0);
  }

  @Override
  public void draw(GL10 gl) {
    color.a = alpha;
    SimpleGeometry.drawPoint(gl, x, y, color);
  }
}
