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

package com.zeddic.common.util;

public class Vector3f {
  public float x;
  public float y;
  public float z;
  
  public Vector3f() {
    this.x = 0;
    this.y = 0;
    this.z = 0;
  }
  
  public Vector3f(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  public void normalize() {
    float midLength = (float) Math.sqrt(x * x + y * y);
    float length = (float) Math.sqrt(midLength * midLength + z * z);
    
    this.x = x / length;
    this.y = y / length;
    this.z = z / length;
  }
  
  
  public void scale(float scale) {
    this.x = x * scale;
    this.y = y * scale;
    this.z = z * scale;
  }
  
  public Vector3f copy() {
    return new Vector3f(x, y, z);
  }
  
  public boolean equals(Vector3f other) {
    return this.x == other.x && this.y == other.y && this.z == other.z;
  }
  
  @Override
  public String toString() {
    return "<" + x + "," + y + "," + z + ">";
  }
}
