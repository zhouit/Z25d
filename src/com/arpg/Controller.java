package com.arpg;

import java.awt.Point;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;

/**
 * 界面控制器
 * 
 * @author zhou
 *
 */
public interface Controller extends EventHandler<Event>{

  /**
   * 每渲染一帧就执行一次
   * 
   * @param context
   */
  public void draw(GraphicsContext context);

  /**
   * 获取当前鼠标位置
   * 
   * @return
   */
  public Point getMousePoint();

  /**
   * 每渲染一帧就执行一次
   * 
   * @return
   */
  public Controller invoke();

  /**
   * 获取控制组件
   * 
   * @return
   */
  public Node getControlBar();
  
}
