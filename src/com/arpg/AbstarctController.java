package com.arpg;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public abstract class AbstarctController implements Controller{
  /* 玩家可能的多按键输入,用集合缓存用户按键,最多4个 */
  protected List<KeyCode> keys = new ArrayList<>(4);
  /* 当前鼠标所在游戏界面的坐标(不计算地图滚动),当为null时表示鼠标离开界面 */
  private Point mouse = null;

  @Override
  public Node getControlBar(){
    return null;
  }

  public Point getMousePoint(){
    return mouse;
  }

  /**
   * <ul>
   * 判断方向的基本原则：每当用户按下一个方向键，就把它的keyCode存进数组（数组里不可以有重复）；每当一个方向键弹起，则把keyCode从数组里清除。
   * <li>若数组里只有一个键,那就是轴方向运动;</li>
   * <li>两个键，两种情况：如果是复合键，就是斜方向运动；如果是一个A一个D这样一组相反键，则只响应先按的那个，物体只做轴方向运动;</li>
   * <li>三个或四个键只响应数组里最先按的那一组复合键，做斜方向运动.</li>
   * </ul>
   * <p>
   * 使用数组的好处在于可以处理复杂的多按键情况（无论是玩家有意乱按还是无意多按的），大大提高手感。
   * </p>
   * <p>
   * 例如这样一组输入：先后按下A、W、D再释放A，之后按下S并释放W、D。玩家最可能的意图是，先左上走，然后改为右上，最后改为下。
   * 这样的按键手感方便但是就造成一般的if ...else判断方法很难响应。
   * </p>
   * <p>
   * 因为Javafx里始终只不断给出最后一个键按下的消息.
   * 新的按键消息会不断把以前的按键消息“冲”掉。而且同时按3个或4个键在if...else里判断很麻烦，
   * 即使建立了类似Ctrl+Shift这样的变量来追踪以前的按键。 而使用有序且不重复的数组则可以完美解决这些问题。
   * </p>
   */

  protected void preKeyEvent(Event event){
    if(!(event instanceof KeyEvent)) return;

    KeyEvent ke = (KeyEvent) event;
    if(event.getEventType() == KeyEvent.KEY_PRESSED && keys.size() < 4){
      keys.add(ke.getCode());
    }else if(event.getEventType() == KeyEvent.KEY_PRESSED){
      keys.remove(ke);
    }
  }

  /**
   * 处理鼠标移动和退出事件
   * 
   * @param event
   */
  protected void preMouseEvent(Event event){
    if(!(event instanceof MouseEvent)) return;

    MouseEvent me = (MouseEvent) event;
    if(event.getEventType() == MouseEvent.MOUSE_MOVED){
      mouse = new Point((int) me.getX(), (int) me.getY());
    }else if(event.getEventType() == MouseEvent.MOUSE_EXITED){
      mouse = null;
    }
  }

  @Override
  public void handle(Event event){
  }

  @Override
  public Controller invoke(){
    return this;
  }

}
