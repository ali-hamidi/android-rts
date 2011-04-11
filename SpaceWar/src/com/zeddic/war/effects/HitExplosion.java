package com.zeddic.war.effects;

import com.zeddic.common.opengl.Color;
import com.zeddic.common.opengl.Sprite;
import com.zeddic.common.particle.ParticleEmitter;
import com.zeddic.common.particle.ParticleEmitter.ParticleEmitterBuilder;
import com.zeddic.war.R;

public class HitExplosion extends Explosion {

  private static final Sprite sprite = new Sprite(5, 5, R.drawable.sparkle);
  
  public HitExplosion() {
    this(0, 0);
  }

  public HitExplosion(float x, float y) {
    super(x, y);
  }

  protected void createEmitter() {
    emitter = new ParticleEmitterBuilder()
        .at(x, y)
        .withEmitMode(ParticleEmitter.MODE_DIRECTIONAL)
        .withEmitSpeedJitter(18)
        .withEmitLife(600)
        .withParticleSpeed(100)
        .withParticleAlphaRate(-1f)
        .withParticleLife(1000)
        .withMaxParticles(5)
        .withEmitRate(200)
        .withEmitAngle(0)
        .withEmitCycle(false)
        .withEmitAngleJitter(20)
        .withParticleScale(1f, -.5f, .5f)
        .withSprite(sprite, new Color(255, 255, 0, 255))
        .build();
  }
}
