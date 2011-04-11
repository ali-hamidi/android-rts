package com.zeddic.common.opengl;

import java.nio.FloatBuffer;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.zeddic.common.AbstractGameObject;
import com.zeddic.common.util.Vector2d;

public class Path extends AbstractGameObject {

  private final FloatBuffer buffer;
  private int size;
  private float lineWidth = 1;
  private Color color = new Color(255, 255, 255, 255);
  
  public Path(List<Vector2d> points) {
    
    size = points.size();
    buffer = Buffers.create(points.size() * 3);
    for (Vector2d point : points) {
      buffer.put(point.x);
      buffer.put(point.y);
      buffer.put(0);
    }
    buffer.position(0);
  }
  
  public void setColor(Color color) {
    this.color = color;
  }
  
  public void setLineWidth(float lineWidth) {
    this.lineWidth = lineWidth;
  }

  @Override
  public void draw(GL10 gl) {
    gl.glLineWidth(lineWidth);

    // Enable textures and prepare the buffer.
    gl.glDisable(GL10.GL_TEXTURE_2D);
    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, buffer);

    // Draw.
    gl.glColor4f(color.r, color.g, color.b, color.a);
    gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, size);

    // Revert everything back to the way it was.
    gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    gl.glEnable(GL10.GL_TEXTURE_2D);
    
  }

  @Override
  public void update(long time) { }
}
