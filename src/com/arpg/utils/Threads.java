package com.arpg.utils;

import java.util.concurrent.TimeUnit;

public class Threads{

  private Threads(){}
  
  public static void sleeps(long time){
    try{
      Thread.sleep(time);
    }catch(InterruptedException e){
      e.printStackTrace();
    }
  }

  public static void sleeps(long duration, TimeUnit unit){
    sleeps(unit.toMillis(duration));
  }

}
