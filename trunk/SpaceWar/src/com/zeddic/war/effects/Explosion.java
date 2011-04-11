package com.zeddic.war.effects;

import javax.microedition.khronos.opengles.GL10;

import com.zeddic.common.Entity;
import com.zeddic.common.opengl.Color;
import com.zeddic.common.opengl.Sprite;
import com.zeddic.common.particle.ParticleEmitter;
import com.zeddic.common.particle.ParticleEmitter.ParticleEmitterBuilder;
import com.zeddic.war.R;

/**
 * A basic, omni directional explosion particle effect.
 * 
 * @author scott@zeddic.com (Scott Bailey)
 */
public class Explosion extends Entity {

  private static final Sprite sprite = new Sprite(10, 10, R.drawable.sparkle);
  protected ParticleEmitter emitter;
  
  public Explosion() {
    this(0, 0);
  }
  
  public Explosion(float x, float y) {
    super(x, y);
    createEmitter();
  }
  
  protected void createEmitter() {
    emitter = new ParticleEmitterBuilder()
        .at(x, y)
        .withEmitMode(ParticleEmitter.MODE_OMNI)
        .withEmitSpeedJitter(2)
        .withEmitLife(2000)
        .withParticleSpeed(150)
        .withParticleAlphaRate(-1f)
        .withParticleScale(1, 1f, .5f)
        .withParticleAcceleration(-100f)
        .withParticleLife(1000)
        .withMaxParticles(30)
        .withEmitRate(250)
        .withEmitCycle(false)
        .withSprite(sprite, new Color(255, 255, 0, 255))
        .build();
  }

  public void ignite() {
    enable();
    emitter.reset();
  }
  
  @Override
  public void draw(GL10 gl) {
    emitter.draw(gl);
  }
  
  @Override
  public void update(long time) {
    super.update(time);
    emitter.x = this.x;
    emitter.y = this.y;
    emitter.update(time);
    if (!emitter.enabled)
      kill();
  }

  @Override
  public void reset() {
    emitter.reset();
  }
}
