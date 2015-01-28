package com.arpg.utils;

import javafx.geometry.Dimension2D;
import javafx.scene.text.Font;

import com.sun.javafx.tk.Toolkit;

/**
 * 字体工具类
 * 
 * @author zhou
 *
 */
public class FontUtils{

  private FontUtils(){
  }

  public static Dimension2D getFontBounds(String text){
    return getFontBounds(text, Font.getDefault());
  }

  public static Dimension2D getFontBounds(String text, Font font){
    float width = Toolkit.getToolkit().getFontLoader().computeStringWidth(text, font);
    float height = Toolkit.getToolkit().getFontLoader().getFontMetrics(font).getLineHeight();
    /*
     * Label label = new Label(text); label.setFont(font); Bounds bounds =
     * label.getLayoutBounds();
     */
    return new Dimension2D(width, height);
  }

}
