package com.arpg.sprite;

import com.arpg.fight.Magic;

/**
 * 精灵状态
 * 
 * @author zhou
 *
 */
public class SpriteState{
  public static final SpriteState NONE = new SpriteState("无");
  public static final SpriteState STAND = new SpriteState("站立");
  public static final SpriteState RUN = new SpriteState("奔跑");
  public static final SpriteState ATTACK = new SpriteState("攻击");
  public static final SpriteState DEAD = new SpriteState("死亡");

  public static final class Injury extends SpriteState{
    /**
     * 造成伤害的魔法
     */
    private Magic magic;

    public Injury(Magic magic){
      super("受到攻击");
      this.magic = magic;
    }

    public Magic getMagic(){
      return magic;
    }

  }

  /**
   * 状态名称
   */
  private String name;

  private SpriteState(String name){
    this.name = name;
  }

  public String getName(){
    return name;
  }

}
