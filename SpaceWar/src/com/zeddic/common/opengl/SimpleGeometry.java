package com.zeddic.common.opengl;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class SimpleGeometry {

  private static FloatBuffer lineBuffer = Buffers.create(6);

  public static void drawLine(GL10 gl, float x1, float y1, float x2, float y2, Color color) {

    lineBuffer.position(0);
    lineBuffer.put(x1);
    lineBuffer.put(y1);
    lineBuffer.put(0);
    lineBuffer.put(x2);
    lineBuffer.put(y2);
    lineBuffer.put(0);
    lineBuffer.position(0);
    
    gl.glLineWidth(2f);
    
    // Enable textures and prepare the buffer.
    gl.glDisable(GL10.GL_TEXTURE_2D);
    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, lineBuffer);

    // Draw.
    gl.glColor4f(color.r, color.g, color.b, color.a);
    gl.glDrawArrays(GL10.GL_LINES, 0, 2);

    // Revert everything back to the way it was.
    gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    gl.glEnable(GL10.GL_TEXTURE_2D);
  }
  
  public static void drawPoint(GL10 gl, float x, float y, Color color) {
    lineBuffer.position(0);
    lineBuffer.put(x);
    lineBuffer.put(y);
    lineBuffer.put(0);
    lineBuffer.position(0);

    // Enable textures and prepare the buffer.
    gl.glDisable(GL10.GL_TEXTURE_2D);
    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, lineBuffer);

    // Draw.
    gl.glColor4f(color.r, color.g, color.b, color.a);
    gl.glDrawArrays(GL10.GL_POINTS, 0, 1);

    // Revert everything back to the way it was.
    gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    gl.glEnable(GL10.GL_TEXTURE_2D);
  }
}
