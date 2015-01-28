package test.com.arpg;

import java.lang.reflect.Array;
import java.util.Arrays;

public class DebugUilts{

  public static void debug(Object... objs){
    for(Object obj : objs){
      if(obj.getClass().isArray()){
        int length=Array.getLength(obj);
        for(int i=0;i<length;i++){
          System.out.print(Array.get(obj, i) + " ,");  
        }
        System.out.println();
      }else{
        System.out.print(obj + " ,");        
      }
    }

    System.out.println();
  }

  public static void debug(int[][] map){
    for(int i = 0; i < map.length; i++){
      System.out.println(Arrays.toString(map[i]));
    }
  }

}
