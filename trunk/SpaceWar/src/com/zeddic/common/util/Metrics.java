package com.zeddic.common.util;

import java.util.LinkedList;
import java.util.Queue;

/**
 * A simply utility class for collect at most N samples and calculating
 * an average.
 * 
 * @author scott@zeddic.com (Scott Bailey)
 */
public class Metrics {
  private final Queue<Long> queue = new LinkedList<Long>();
  private final int maxSamples; 
  
  public Metrics(int maxSamples) {
    this.maxSamples = maxSamples;
  }

  /** Adds a new sample. */
  public void addSample(long value) {
    queue.add(value);
    
    if (queue.size() > maxSamples) {
      queue.remove();
    }
  }

  /** Returns the average of available samples. */
  public long getAverage() {
    if (queue.size() == 0) {
      return 0;
    }
    
    long total = 0;
    for (long value : queue) {
      total += value;
    }
    
    return total / queue.size();
  }

  /** Erases all existing samples. */
  public void clear() {
    queue.clear();
  }
}
