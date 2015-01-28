package com.arpg.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ResourceManager{
  
  private ResourceManager(){}

  public static String getUrl(String fileName){
    File file = new File(fileName);
    return file.toURI().toString();
  }

  public static String getResourceUrl(String classpath){
    return ResourceManager.class.getResource(classpath).toString();
  }

  public static Image loadImage(String fileName){
    Image result = null;
    FileInputStream image = null;
    try{
      image = new FileInputStream(fileName);
      result = new Image(image);
    }catch(FileNotFoundException e){
      e.printStackTrace();
    }finally{
      IOUtils.closeQuietly(image);
    }

    return result;
  }
  
  public static Image loadImage(String fileName,int width,int height){
    Image result = null;
    FileInputStream image = null;
    try{
      image = new FileInputStream(fileName);
      result = new Image(image,width,height,false,true);
    }catch(FileNotFoundException e){
      e.printStackTrace();
    }finally{
      IOUtils.closeQuietly(image);
    }
    
    return result;
  }
  
  public static Cursor loadCursor(){
    return new ImageCursor(loadImage("resource/image/cursor.png"));
  }

  public static int[][] loadCvs(String cvsFile){
    BufferedReader reader = null;
    int[][] result = null;
    try{
      reader = new BufferedReader(new FileReader(cvsFile));
      List<int[]> list = new LinkedList<int[]>();
      String line = null;
      while((line = reader.readLine()) != null){
        String[] nums = line.split(", *+");
        int[] ints = new int[nums.length];
        for(int i = 0; i < nums.length; i++){
          ints[i] = Integer.parseInt(nums[i]);
        }

        list.add(ints);
      }

      result = new int[list.size()][];
      for(int i = 0; i < list.size(); i++){
        result[i]=list.get(i);
      }
    }catch(Exception e){
      e.printStackTrace();
    }finally{
      IOUtils.closeQuietly(reader);
    }
    
    return result;
  }
  
  public static ImageView getViewOfClasspath(String fileName){
    Image result = loadImage(fileName);
    ImageView renVal = new ImageView(result);
    return renVal;
  }

}