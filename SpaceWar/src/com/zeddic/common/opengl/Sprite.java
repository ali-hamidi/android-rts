package com.zeddic.common.opengl;

import android.graphics.Bitmap;


public class Sprite extends Mesh {
  
  //private Bitmap bitmap;
  
  public Sprite(int width, int height, int resource) {
    setDimensions(width, height);
    setTextureCoordinates();
    setTexture(new Texture(resource));
  }
  
  public Sprite(int width, int height, Bitmap bitmap) {
    setDimensions(width, height);
    setTextureCoordinates();
    setTexture(new Texture(bitmap));
  }
  
  private void setDimensions(int width, int height) {
    int halfWidth = width / 2;
    int halfHeight = height / 2;
    
    float vertices[] = {
        -halfWidth, -halfHeight, 0, // 0
         halfWidth, -halfHeight, 0, // 1
        -halfWidth,  halfHeight, 0, // 2
         halfWidth,  halfHeight, 0, // 3
    };
    
    short indices[] = {
        0, 1, 2,
        1, 3, 2,
    };
    
    setIndices(indices);
    setVertices(vertices);
  }
  
  private void setTextureCoordinates() {
    float textureCoordinates[] = {
        0.0f, 1.0f,
        1.0f, 1.0f,
        0.0f, 0.0f,
        1.0f, 0.0f,
    };
    
    setTextureCoordinates(textureCoordinates);
  }
  
  //public Sprite(int width, int height, Bitmap bitmap) {
  //  this.bitmap = bitmap;
  //}
  
  /*@Override
  public void draw(GL10 gl) {
    int textures[] = new int[1];
    gl.glGenTextures(1, textures, 0);
    gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

    GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
    //setTexture(textures[0]);
    super.draw(gl);
    gl.glDeleteTextures(1, textures, 0);
  } */
}
