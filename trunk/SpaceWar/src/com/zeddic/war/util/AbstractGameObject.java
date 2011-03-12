package com.zeddic.war.util;

public abstract class AbstractGameObject implements GameObject {

  /** Whether an object should be drawn and updated. */
  public boolean enabled = true;

  /** Whether an object can be restored to an object pool. */
  public boolean canRecycle = false;

  /** Whether an object was obtained from an object pool. */
  public boolean taken = false;

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
    canRecycle = false;
  }

  public void kill() {
    this.enabled = false;
    canRecycle = true;
  }
}
