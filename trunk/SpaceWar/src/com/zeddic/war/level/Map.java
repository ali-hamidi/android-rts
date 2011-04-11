/*
 * Copyright (C) 2010 Geo Siege Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zeddic.war.level;

import javax.microedition.khronos.opengles.GL10;

import com.zeddic.common.AbstractGameObject;
import com.zeddic.common.opengl.Color;
import com.zeddic.common.util.Countdown;
import com.zeddic.war.GameState;
import com.zeddic.war.ships.Square;

public class Map extends AbstractGameObject {
  
  private static final float EDGE_BUFFER = 0;
  private static final float SPAWN_BUFFER = 50;
  
  public float width;
  public float height;
  public float top;
  public float left;
  public float right;
  public float bottom;
  
  public float spawnLeft;
  public float spawnTop;
  public float spawnWidth;
  public float spawnHeight;
  public float spawnRight;
  public float spawnBottom;
  
  //private final List<Planet> planets = new ArrayList<Planet>();
  
  private static final int ENEMIES_PER_WAVE = 10;
  
  private final Planet planet;
  private final InvadePath path;
  private final Countdown nextWaveCountdown = new Countdown(10000);
  private final Countdown spawnCountdown = new Countdown(1000);
  private int leftToSpawn;
  
  public Map(float width, float height) {
    setSize(width, height);

    planet = new Planet(750, 500, new Color(255, 187, 0, 255));
    path = new InvadePath.Builder()
        .add(0, 100)
        .add(400, 100)
        .add(400, 500)
        .add(750, 500)
        .build();
   
    leftToSpawn = ENEMIES_PER_WAVE;
    nextWaveCountdown.start();
    spawnCountdown.start();
  }
  
  private void setSize(float width, float height) {
    this.width = width;
    this.height = height;
    top = 0 + EDGE_BUFFER;
    left = 0 + EDGE_BUFFER;
    right = left + width; 
    bottom = top + height;
    
    spawnLeft = left + SPAWN_BUFFER;
    spawnTop = top + SPAWN_BUFFER;
    spawnWidth = width - 2 * SPAWN_BUFFER;
    spawnHeight = height - 2 * SPAWN_BUFFER;
    spawnBottom = spawnTop + spawnHeight;
    spawnRight = spawnLeft + spawnWidth;
  }
  
  public boolean inSpawnableArea(float x, float y) {
    return x >= spawnLeft && x <= spawnLeft + spawnWidth &&
        y >= spawnTop && y <= spawnTop + spawnHeight;
  }
  
  @Override
  public void update(long time) {
    planet.update(time);
    path.update(time);
    
    if (leftToSpawn > 0) {
      spawnCountdown.update(time);
      if (spawnCountdown.isDone()) {
        Square square = GameState.stockpiles.ships.take(Square.class);
        square.spawn(path);
        spawnCountdown.restart();
        leftToSpawn--;
      }
    } else {
      nextWaveCountdown.update(time);
      if (nextWaveCountdown.isDone()) {
        leftToSpawn = ENEMIES_PER_WAVE;
        nextWaveCountdown.restart();
      }
    }
  }
  
  @Override
  public void draw(GL10 gl) {
    planet.draw(gl);
    path.draw(gl);
  }
}
