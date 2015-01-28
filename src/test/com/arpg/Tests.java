package test.com.arpg;

import javafx.scene.paint.Color;

import org.junit.Test;

import com.arpg.avg.Message;
import com.arpg.sprite.Sprite;
import com.arpg.utils.ImageUtils;
import com.arpg.utils.JavafxImageWriter;
import com.arpg.utils.RpgConstants;

public class Tests{
  
  @Test
  public void testBg(){
//    ImageUtils.save(ResourceManager.loadImage("image/map/map.png", 150, 120), "image/map/mini.png");
//    ImageUtils.save(Message.loadBorder(130, 100), "image/test.png");
    ImageUtils.save(Message.loadBg(130, 100), "resource/image/test.png");
  }
  
  @Test
  public void testSnow(){
    Sprite enmey = new Sprite("resource/image/role/assassin.png", RpgConstants.DOWN);
    JavafxImageWriter iwriter=new JavafxImageWriter(enmey.getSpriteImage());
    iwriter.hue(Color.SNOW);
    ImageUtils.save(iwriter.getImage(), "resource/image/test.png");
  }
  
  @Test
  public void testIcon(){
    
  }

}
