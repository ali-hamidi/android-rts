package com.zeddic.common;

import com.zeddic.war.util.GameObject;

public abstract class AbstractGameObject implements GameObject {

  /** Whether an object should be drawn and updated. */
  public boolean enabled = true;

  /** Whether an object can be restored to an object pool. */
  public boolean canRecycle = false;

  /** Whether an object was obtained from an object pool. */
  public boolean taken = false;

  public void enable() {
    setEnabled(true);
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
    canRecycle = false;
  }

  public void kill() {
    this.enabled = false;
    canRecycle = true;
  }
  
  @Override
  public void reset() {
    // Default to doing nothing.
  }
}

