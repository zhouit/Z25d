package com.arpg.sprite;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import javafx.scene.canvas.GraphicsContext;

/**
 * 角色群
 * 
 * @author zhou
 * 
 */
public class SpriteContainer implements Comparator<Sprite>{
  private Sprite hero;
  /**
   * 地图上所有角色,包括hero
   */
  private Set<Sprite> characters = new TreeSet<Sprite>(this);

  public SpriteContainer(){
  }

  public void setHero(Sprite role){
    this.hero = role;
    characters.add(role);
  }

  /**
   * 指定点上是否存在精灵
   * 
   * @param x
   * @param y
   * @return
   */
  public boolean isHit(int x, int y){
    for(Sprite role : characters){
      if((role.getX() == x || role.getX() + role.getSpriteOffsetX() == x)
          && (role.getY() == y || role.getY() + role.getSpriteOffsetY() == y)){
        return true;
      }
    }

    return false;
  }

  /**
   * 角色群绘制
   * 
   * @param context
   * @param offsetX
   * @param offsetY
   */
  public void draw(GraphicsContext context, int offsetX, int offsetY){
    for(Sprite role : characters){
      role.draw(context, offsetX, offsetY);
    }
  }

  @Override
  public int compare(Sprite one, Sprite two){
    return (one.getPx() > two.getPx() + 96) || (one.getPy() > two.getPy()) ? 1 : 0;
  }

  /**
   * 检查指定地点是否有Role
   * 
   * @param x
   * @param y
   * @return
   */
  public Sprite hasRole(int x, int y){
    for(Sprite role : characters){
      if(role.getX() == x && role.getY() == y){
        return role;
      }
    }

    return null;
  }

  public void addCharacter(Sprite role){
    this.characters.add(role);
  }

  public void removeCharacter(Sprite sprite){
    this.characters.remove(sprite);
  }

  public Sprite getHero(){
    return hero;
  }

  /**
   * 获取地图上所有精灵
   * 
   * @return
   */
  public Set<Sprite> getCharacters(){
    return characters;
  }

}
