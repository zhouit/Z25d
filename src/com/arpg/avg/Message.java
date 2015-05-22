package com.arpg.avg;

import javafx.geometry.Dimension2D;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import com.arpg.utils.FontUtils;
import com.arpg.utils.ImageUtils;
import com.arpg.utils.JavafxImageWriter;
import com.arpg.utils.ResourceManager;

public class Message{
  private static Image cache = ResourceManager.loadImage("resource/image/window.png");
  private static Image select = ResourceManager.loadImage("resource/image/creese.png");
  private static Font msgFont = Font.font("KaiTi", FontWeight.BOLD, 25);

  public static Image loadBorder(){
    return ImageUtils.clip(cache, 64, 64, 128, 0);
  }

  /**
   * 请使用 {@link Message#loadBorder()}和{@link com.arpg.utils.NinePatch}技术
   * 
   * @param width
   * @param height
   * @return
   */
  @Deprecated
  public static Image loadBorder(int width, int height){
    Image frame = ImageUtils.clip(cache, 64, 64, 128, 0);
    Image left = ImageUtils.clip(frame, 10, height, 0, 27, 10, 64 - 27);
    Image leftUp = ImageUtils.clip(frame, 27, 27, 0, 0);
    Image right = ImageUtils.clip(frame, 10, height, 64 - 10, 27, 64, 64 - 27);
    Image rightUp = ImageUtils.clip(frame, 27, 27, 64 - 27, 0);
    Image up = ImageUtils.clip(frame, width, 10, 27, 0, 64 - 27, 10);
    Image leftDown = ImageUtils.clip(frame, 27, 27, 0, 64 - 27);
    Image down = ImageUtils.clip(frame, width, 10, 27, 64 - 10, 64 - 27, 64);
    Image rightDown = ImageUtils.clip(frame, 27, 27, 64 - 27, 64 - 27);

    JavafxImageWriter iwriter = new JavafxImageWriter(width, height);
    iwriter.fillImage(left);
    iwriter.fillImage(right, width - 10, 0);
    iwriter.fillImage(up);
    iwriter.fillImage(down, 0, height - 10);

    iwriter.fillImage(leftUp);
    iwriter.fillImage(rightUp, width - 27, 0);
    iwriter.fillImage(leftDown, 0, height - 27);
    iwriter.fillImage(rightDown, width - 27, height - 27);

    return iwriter.getImage();
  }

  public static Image loadBg(int width, int height){
    return ImageUtils.clip(cache, width, height, 128, 64, 128 + 32, 64 + 32);
  }

  public static Font getMsgFont(){
    return msgFont;
  }

  public static Image getSelect(){
    return select;
  }

  public static Dimension2D getTextBounds(String msg){
    return FontUtils.getFontBounds(msg, msgFont);
  }

}
