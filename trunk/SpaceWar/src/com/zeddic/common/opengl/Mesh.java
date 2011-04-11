package com.zeddic.common.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public abstract class Mesh {
  
  // Translate params.
  public float x = 0;
  public float y = 0;
  public float z = 0;
  
  public float scale = 1;

  // Rotate transformation parameters.
  public float rx = 0;
  public float ry = 0;
  public float rz = 0;

  // Our vertex buffer.
  private FloatBuffer verticesBuffer = null;
  private ShortBuffer indicesBuffer = null;
  private int numOfIndices = -1;

  // Flat or smooth coloring.
  private float[] rgba = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
  private FloatBuffer colorBuffer = null;
  
  public Texture texture = null;
  private FloatBuffer textureCoordBuffer = null;

  public void draw(GL10 gl) {
    
    // Enable the vertex buffer and specify vertices.
    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesBuffer);
    
    // Enable coloring if specified.
    gl.glColor4f(rgba[0], rgba[1], rgba[2], rgba[3]);
    if ( colorBuffer != null ) {
        // Enable the color array buffer to be used during rendering.
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        // Point out the where the color buffer is.
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
    }
    
    // Enable textures if specified.
    if (texture != null && textureCoordBuffer != null) {
      gl.glEnable(GL10.GL_TEXTURE_2D);
      gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
      gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureCoordBuffer);
      gl.glBindTexture(GL10.GL_TEXTURE_2D, texture.id);
    }

    // Output the vertices with any requested transformations.
    gl.glPushMatrix();
    
    applyActiveTransformations(gl);
    
    gl.glDrawElements(GL10.GL_TRIANGLES, numOfIndices, GL10.GL_UNSIGNED_SHORT, indicesBuffer);

    gl.glPopMatrix();
    
    // Disable any enabled client states.
    gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    
    if ( colorBuffer != null) {
      gl.glDisableClientState(GL10.GL_COLOR_ARRAY);;
    }

    if (texture != null && textureCoordBuffer != null) {
      gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
      gl.glDisable(GL10.GL_TEXTURE_2D);
    }
  }
  
  private void applyActiveTransformations(GL10 gl) {
    // Only apply transformations if necessary. Excess stack
    // transformations can cause slow down if done enough.
    if (x != 0 || y  != 0 || z != 0) {
      gl.glTranslatef(x, y, z);
    }
    
    if (rx != 0) {
      gl.glRotatef(rx, 1, 0, 0);
    }
    
    if (ry != 0) {
      gl.glRotatef(ry, 0, 1, 0);
    }
    
    if (rz != 0) {
      gl.glRotatef(rz, 0, 0, 1);
    }
    
    if (scale != 0) {
      gl.glScalef(scale, scale, scale);
    }
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

  protected void setIndices(short[] indices) {
    // short is 2 bytes, therefore we multiply the number if
    // vertices with 2.
    ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
    ibb.order(ByteOrder.nativeOrder());
    indicesBuffer = ibb.asShortBuffer();
    indicesBuffer.put(indices);
    indicesBuffer.position(0);
    numOfIndices = indices.length;
  }

  public void setColor(float red, float green, float blue, float alpha) {
    rgba[0] = red;
    rgba[1] = green;
    rgba[2] = blue;
    rgba[3] = alpha;
  }
  
  public void setColor(Color color) {
    rgba[0] = color.r;
    rgba[1] = color.g;
    rgba[2] = color.b;
    rgba[3] = color.a;
  }
   
  /**
   * Sets the alpha transparency of the mesh. 1 is solid. 0 for completely
   * transparent.
   */
  public void setAlpha(float alpha) {
    rgba[3] = alpha;
  }
  
  protected void setColors(float[] colors) {
    // float has 4 bytes.
    ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
    cbb.order(ByteOrder.nativeOrder());
    colorBuffer = cbb.asFloatBuffer();
    colorBuffer.put(colors);
    colorBuffer.position(0);
  }
  
  public void setTexture(Texture texture) {
    this.texture = texture;
  }

  protected void setTextureCoordinates(float[] textureCoords) {
    ByteBuffer byteBuf = ByteBuffer
    .allocateDirect(textureCoords.length * 4);
    byteBuf.order(ByteOrder.nativeOrder());
    textureCoordBuffer = byteBuf.asFloatBuffer();
    textureCoordBuffer.put(textureCoords);
    textureCoordBuffer.position(0);
  }
}