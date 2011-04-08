package com.zeddic.war.effects;

import javax.microedition.khronos.opengles.GL10;

import com.zeddic.common.Entity;
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
        .withEmitLife(600)
        .withParticleSpeed(20)
        .withParticleAlphaRate(-.03f)
        .withParticleLife(500)
        .withMaxParticles(20)
        .withEmitRate(1000)
        .withEmitCycle(false)
        .withSprite(new Sprite(16, 16, R.drawable.redparticle))
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
