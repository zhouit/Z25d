package com.arpg.fight;

/**
 * 秒表
 * 
 * @author zhou
 *
 */
public final class Stopwatch{
  public static final long SECONDS = 1000000000L;

  private long startTick;

  public void start(){
    startTick = System.nanoTime();
  }

  /**
   * 从当前距启动的时间
   * 
   * @return
   */
  public int count(){
    long stopTick = System.nanoTime();

    return (int) ((stopTick - startTick) / SECONDS);
  }

  public void reset(){
    this.startTick = -1;
  }

}
