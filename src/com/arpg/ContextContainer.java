package com.arpg;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;

import com.arpg.utils.AudioManager;
import com.arpg.utils.ResourceManager;

/**
 * 游戏主线程及上下文
 * 
 * @author zhou
 *
 */
public class ContextContainer extends AnimationTimer{
  private Canvas canvas;
  private Node controlBar;
  private Group root;
  private ViewResolver resolver;

  public ContextContainer(Controller boot, Canvas canvas){
    this.canvas = canvas;
    this.resolver = new ViewResolver(boot);
    this.controlBar = resolver.getControlBar();
  }

  public void processEvent(){
    // 注:Canvas不能监听KeyPress时间
    this.canvas.getScene().setOnKeyPressed(resolver);
    this.canvas.getScene().setOnKeyReleased(resolver);
    this.canvas.setOnMouseClicked(resolver);
    this.canvas.setOnMouseMoved(resolver);
    this.canvas.setOnMouseExited(resolver);
  }

  public Parent getSurface(){
    root = new Group();
    root.getChildren().add(canvas);
    root.setCursor(ResourceManager.loadCursor());
    if(controlBar != null){
      root.getChildren().add(controlBar);
    }
    return root;
  }

  /**
   * 启动游戏
   */
  public void startUp(){
    processEvent();
    start();
//    AudioManager.playBg();
  }

  public void shoutdown(){
    stop();
    AudioManager.stopBg();
  }

  @Override
  public void handle(long now){
    resolver.renderView(canvas.getGraphicsContext2D());
    Node temp = resolver.getControlBar();
    
    if(temp == controlBar) return;

    controlBar = temp;
    if(controlBar == null && root.getChildren().size() > 1){
      root.getChildren().remove(1);
    }else if(controlBar != null && root.getChildren().size() > 1){
      root.getChildren().set(1, controlBar);
    }else if(controlBar != null && root.getChildren().size() == 1){
      root.getChildren().add(controlBar);
    }
  }

}
