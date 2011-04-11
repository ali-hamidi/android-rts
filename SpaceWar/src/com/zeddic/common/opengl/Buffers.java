package com.zeddic.common.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Buffers {
  
  public static FloatBuffer create(int size) {
    //float lineVertices[] = { 0, 0, 0, 0 };
    ByteBuffer bb = ByteBuffer.allocateDirect(size * 4);
    bb.order(ByteOrder.nativeOrder());
    FloatBuffer buffer = bb.asFloatBuffer();
    buffer.position(0);
    
    return buffer;
  }
}
