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

package com.zeddic.war.guns.control;

import com.zeddic.common.Entity;
import com.zeddic.war.guns.Gun;

public class DirectionalGunControl implements GunControl {
  
  private static final float DEFAULT_ANGLE_OFFSET = 0;
  
  public Entity owner;
  public float angleOffset;
  
  public DirectionalGunControl(Entity owner) {
    this(owner, DEFAULT_ANGLE_OFFSET);
  }
  
  public DirectionalGunControl(Entity owner, float angleOffset) {
    this.owner = owner;
    this.angleOffset = angleOffset;
  }
  
  @Override
  public boolean shouldFire(Gun gun) {
    return true;
  }
  
  @Override
  public void aim(Gun gun) {
    gun.setAimAngle(owner.angle + angleOffset);
  }
}
