package com.zeddic.common.opengl;

import android.graphics.Bitmap;

public class Texture {

  int id = -1;
  int resourceId = -1;
  Bitmap bitmap = null;

  float width;
  float height;

  public Texture(int resourceId) {
    this.resourceId = resourceId;
    register();
  }
  
  public Texture(Bitmap bitmap) {
    this.bitmap = bitmap;
    register();
  }

  public boolean hasResource() {
    return resourceId != -1;
  }
  
  public boolean hasBitmap() {
    return bitmap != null;
  }
  
  public boolean isLoaded() {
    return id != -1;
  }
  
  public void reload() {
    TextureLibrary.get().reloadTexture(this);
  }
  
  private void register() {
    TextureLibrary.get().add(this);
  }
}
