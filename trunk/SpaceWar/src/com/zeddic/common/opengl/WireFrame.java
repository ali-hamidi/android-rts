package com.zeddic.common.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.zeddic.common.util.Vector3f;

public class WireFrame {

  public float x = 0;
  public float y = 0;
  public float z = 0;
  public float scale = 1;
  public float rx = 0;
  public float ry = 0;
  public float rz = 0;

  private FloatBuffer verticesBuffer = null;
  private int numOfVertices = -1;
  private float[] rgba = new float[]{1.0f, 1.0f, 1.0f, 1.0f};

  public WireFrame(List<Vector3f> vertices) {

    numOfVertices = vertices.size();
    float data[] = new float[vertices.size() * 3];
    for (int i = 0; i < vertices.size(); i++) {
      int index = i * 3;
      data[index] = vertices.get(i).x;
      data[index + 1] = vertices.get(i).y;
      data[index + 2] = vertices.get(i).z;
    }
    setVertices(data);
  }

  public void draw(GL10 gl) {
 
    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesBuffer);
    
    // Set flat color
    gl.glColor4f(rgba[0], rgba[1], rgba[2], rgba[3]);

    gl.glPushMatrix();
    gl.glTranslatef(x, y, z);
    gl.glRotatef(rx, 1, 0, 0);
    gl.glRotatef(ry, 0, 1, 0);
    gl.glRotatef(rz, 0, 0, 1);
    
    if (scale != 1) {
      gl.glScalef(scale, scale, scale);
    }

    gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, numOfVertices);
    gl.glPopMatrix();
    
    gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

  }
  
  protected void setVertices(float[] vertices) {
    // a float is 4 bytes, therefore we multiply the number if
    // vertices with 4.
    ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
    vbb.order(ByteOrder.nativeOrder());
    verticesBuffer = vbb.asFloatBuffer();
    verticesBuffer.put(vertices);
    verticesBuffer.position(0);
  }

  public void setColor(float red, float green, float blue, float alpha) {
    // Setting the flat color.
    rgba[0] = red;
    rgba[1] = green;
    rgba[2] = blue;
    rgba[3] = alpha;
  }

  public void setColor(int red, int green, int blue, int alpha) {
    setColor(
        (float) red / 255,
        (float) green / 255,
        (float) blue / 255,
        (float) alpha / 255);
  }
  
  public static class Builder {
    List<Vector3f> vertices = new ArrayList<Vector3f>();
    
    public Builder add(float x, float y, float z) {
      vertices.add(new Vector3f(x, y, z));
      return this;
    }
    
    public WireFrame build() {
      return new WireFrame(vertices);
    }
  }
}
