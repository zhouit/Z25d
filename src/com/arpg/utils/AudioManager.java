package com.arpg.utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioManager{
  static MediaPlayer player=new MediaPlayer(new Media(ResourceManager.getUrl("resource/audio/bg.mp3")));
  
  private AudioManager(){}
  
  public static void playBg(){
    player.setCycleCount(MediaPlayer.INDEFINITE);
    player.setVolume(0.5D);
    player.setAutoPlay(true);
    player.play();
  }
  
  public static void stopBg(){
    player.stop();
    player.dispose();
    player=null;
  }

}
