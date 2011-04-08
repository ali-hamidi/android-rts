package com.zeddic.war.effects;

import com.zeddic.common.particle.ParticleEmitter;
import com.zeddic.common.particle.ParticleEmitter.ParticleEmitterBuilder;
import com.zeddic.common.particle.PixelParticle;

/**
 * An explosion that creates a circular shockwave, with all particles
 * firing immediatly from the emitter.
 * 
 * @author Scott Bailey
 */
public class ShockwaveExplosion extends Explosion {

  public ShockwaveExplosion() {
    super(0, 0);
  }

  protected void createEmitter() {
    emitter = new ParticleEmitterBuilder()
        .at(x, y)
        .withEmitMode(ParticleEmitter.MODE_OMNI)
        .withEmitSpeedJitter(.5f)
        .withEmitLife(2000)
        .withParticleSpeed(12)
        .withParticleAlphaRate(0)
        .withParticleLife(2000)
        .withMaxParticles(40)
        .withEmitRate(2000)
        .withParticleClass(PixelParticle.class)
        .build();
  }
}
