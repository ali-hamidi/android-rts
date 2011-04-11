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
import com.zeddic.common.util.Vector2d;
import com.zeddic.war.collision.ProximityUtil;
import com.zeddic.war.guns.Gun;
import com.zeddic.war.ships.Ship;
import com.zeddic.war.ships.Square;

public class EnemyAimingGunControl extends GunControl {

  private final Vector2d aimVector = new Vector2d();
  private final Ship owner;
  private float range;
  
  private Entity target = null;

  public EnemyAimingGunControl(Ship owner, float range) {
    this.owner = owner;
    this.range = range;
  }

  public boolean shouldFire(Gun gun) {
    target = ProximityUtil.getClosest(Square.class, owner.x, owner.y, range);
    return target != null;
  }
  
  public void aim(Gun gun) {
    if (target == null) {
      return;
    }

    aimVector.x = target.x - owner.x;
    aimVector.y = target.y - owner.y;
    float angle = aimVector.getAngle();
    gun.setAimAngle(angle);
    owner.angle = angle;
  }
}
