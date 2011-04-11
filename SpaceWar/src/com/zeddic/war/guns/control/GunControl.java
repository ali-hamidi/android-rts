package com.zeddic.war.guns.control;

import com.zeddic.war.guns.Gun;

/**
 * An interface for any object that knows how to aim a gun.
 * 
 * @author scott@zeddic.com (Scott Bailey)
 */
public interface GunControl {
  
  /**
   * Returns true if the gun is capable of firing. For example,
   * if an target is within range.
   */
  boolean shouldFire(Gun gun);

  /**
   * Aims the gun for a single shot by setting it's angle.
   */
  void aim(Gun gun);
}
