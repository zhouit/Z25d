package com.arpg.sprite;

/**
 * 精灵类型
 * 
 * @author zhou
 *
 */
public enum SpriteType {
  PLAYER("玩家"), NPC("NPC"), MONSTER("怪");

  private String name;

  private SpriteType(String name){
    this.name = name;
  }

  public String getName(){
    return name;
  }

}
