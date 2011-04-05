package com.zeddic.common.opengl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class TextureLibrary {

  private static TextureLibrary singleton = new TextureLibrary();
  private List<Texture> textures = new ArrayList<Texture>();
  private Map<Integer, Texture> cache = new HashMap<Integer, Texture>();
  private GL10 gl;
  private Context context;
  private boolean initialized = false;
  
  public static TextureLibrary get() {
    return singleton;
  }
  
  public void init(GL10 gl, Context context) {
    this.gl = gl;
    this.context = context;
    initialized = true;
  }
  
  public void add(Texture texture) {
    textures.add(texture);
    if (initialized) {
      loadTexture(texture);
    }
  }
  
  public void reload() {
    clear();
    loadTextures();
  }
  
  private void clear() {
    for (Texture texture : textures) {
      gl.glDeleteTextures(1, new int[] { texture.id }, 0);
      texture.id = -1;
    }
    cache.clear();
  }
  
  private void loadTextures() {
    
    for (Texture texture : textures) {
      loadTexture(texture);
    }
  }
  
  public void reloadTexture(Texture texture) {
    if (texture.id != -1) {
      gl.glDeleteTextures(1, new int[] { texture.id }, 0);
      texture.id = -1;
    }
    loadTexture(texture);
  }
  
  public void loadTexture(Texture texture) {

    if (!initialized) {
      return;
    }

    if (cache.containsKey(texture.resourceId)) {
      Texture cached = cache.get(texture.resourceId);
      texture.id = cached.id;
      texture.width = cached.width;
      texture.height = cached.height;
      return;
    }
    
    if (texture.hasBitmap()) {
      createTextureFromBitmap(texture.bitmap, texture);
      return;
    }

    if (texture.hasResource()) {
      Bitmap bitmap = getBitmap(texture.resourceId);
      createTextureFromBitmap(bitmap, texture);
      bitmap.recycle();
      
      cache.put(texture.resourceId, texture);
    }
  }

  private void createTextureFromBitmap(Bitmap bitmap, Texture texture) {

    texture.id = newTextureId();
    texture.width = bitmap.getWidth();
    texture.height = bitmap.getHeight();
    
    gl.glBindTexture(GL10.GL_TEXTURE_2D, texture.id);
    
    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
    
    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
    
    gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);
  
    GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
  }

  public Bitmap getBitmap(int resource) {
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inScaled = false;
    
    Bitmap bitmap = BitmapFactory.decodeResource(
        context.getResources(),
        resource,
        options);

    return bitmap;
  }
  
  private int newTextureId() {
    int[] textures = new int[1];
    gl.glGenTextures(1, textures, 0);
    return textures[0];
  }
  
  /*Map<Integer, TextureLoadHandler>
  
  public addLoadHandler(TextureLoadHandler handler) {
    
  }
  
  public static interface TextureLoadHandler {
    onLoad(int textureid);
  }*/
  
  /*public Bitmap getBitmap(int resource) {
  
  if (bitmaps.containsKey(resource)) {
    return bitmaps.get(resource);
  }
  
  Bitmap bitmap = BitmapFactory.decodeResource(
      context.getResources(),
      resource);
  
  bitmaps.put(resource, bitmap);
  
  return bitmap;
} */

/*public int getTexture(GL10 gl, int resource) {
  Bitmap bitmap = getBitmap(resource);
  return getTexture(gl, bitmap);
}

public int getTexture(GL10 gl, Bitmap bitmap) {
  int textureId = createNewTextureId(gl);
  loadTexture(gl, bitmap, textureId);
  return textureId;
} */
  //  //Map<Integer, Bitmap> bitmaps = new HashMap<Integer, Bitmap>();
}
