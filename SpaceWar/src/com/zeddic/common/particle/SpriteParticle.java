package com.zeddic.common.particle;

import javax.microedition.khronos.opengles.GL10;

import com.zeddic.common.opengl.Color;
import com.zeddic.common.opengl.Sprite;

public class SpriteParticle extends Particle {

  protected Sprite sprite = null;
  protected Color color = null;
  
  public SpriteParticle() {
    super(0, 0);
  }
  
  @Override
  public void onCreate(ParticleData data) {
    if (data instanceof SpriteParticleData) {
      this.sprite = ((SpriteParticleData) data).sprite;
      this.color = ((SpriteParticleData) data).color;
    }
  }

  @Override
  public void draw(GL10 gl) {
    if (sprite == null) {
      return;
    }

    sprite.scale = scale;
    sprite.setColor(color);
    sprite.x = x;
    sprite.y = y;
    sprite.setAlpha(alpha);
    sprite.draw(gl);
  }

  public static class SpriteParticleData implements ParticleData {
    Color color;
    Sprite sprite;

    public SpriteParticleData(Sprite sprite, Color color) {
      this.sprite = sprite;
      this.color = color;
    }
  }
}
