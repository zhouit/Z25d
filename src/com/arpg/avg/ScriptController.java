package com.arpg.avg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javafx.event.Event;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import com.arpg.AbstarctController;
import com.arpg.utils.IOUtils;
import com.arpg.utils.LineReader;
import com.arpg.utils.RpgConstants;
import com.arpg.utils.StringUtils;

/**
 * 脚本执行器,主要负责对话剧情,不参与界面切换
 * 
 * @author zhou
 *
 */
public class ScriptController extends AbstarctController{
  private LineReader scripts;
  // 游戏脚本中的变量表
  private Map<String, String> vars;
  private Map<String, Cg> cgs;
  private String message;
  private int animateCounter = 2;
  /* 对话框选项 */
  private List<String> selects;
  /* 对话框中多选用户选项 */
  private int select = -1;
  private double fontHeight;

  public ScriptController(String scriptFile){
    if(!StringUtils.isBlank(scriptFile)){
      this.vars = new HashMap<String, String>();
      this.cgs = new LinkedHashMap<String, Cg>();
      readScript(scriptFile);
      this.fontHeight = Message.getTextBounds("中文").getHeight();
    }
  }

  /**
   * 当前脚本是否正在运行
   * 
   * @return
   */
  public boolean isRunning(){
    return scripts != null && scripts.hasNext();
  }

  @Override
  public void handle(Event event){
    if(event.getEventType() == MouseEvent.MOUSE_CLICKED){
      invokeScript();
    }else if(event.getEventType() == MouseEvent.MOUSE_MOVED){
      if(selects == null) return;

      MouseEvent me = (MouseEvent) event;
      select = (int) ((me.getY() - 350) / fontHeight);
      select = Math.max(0, select);
      select = Math.min(select, selects.size() - 1);
    }
  }

  @Override
  public void draw(GraphicsContext context){
    for(Cg cg : cgs.values()){
      context.drawImage(cg.getImage(), cg.getX(), cg.getY());
    }

    context.setGlobalAlpha(0.4d);
    context.drawImage(Message.loadBg(RpgConstants.CANVAS_WIDTH - 60, 130), 30, 315);
    context.setGlobalAlpha(1.0d);
    context.drawImage(Message.loadBorder(RpgConstants.CANVAS_WIDTH - 50, 140), 25, 310);

    drawMsg(context);
    drawSelects(context);
  }

  private void drawMsg(GraphicsContext context){
    if(message == null) return;

    context.setFill(Color.WHITESMOKE);
    context.setFont(Message.getMsgFont());
    String[] msgs = message.substring(0, animateCounter / 2).split("&");
    for(int i = 0; i < msgs.length; i++){
      context.fillText(msgs[i], 40, 350 + i * fontHeight);
    }

    animateCounter = Math.min(animateCounter + 1, message.length() * 2);
  }

  private void drawSelects(GraphicsContext context){
    if(selects == null) return;

    context.setFill(Color.WHITESMOKE);
    context.setFont(Message.getMsgFont());
    for(int i = 0; i < selects.size(); i++){
      context.fillText(selects.get(i), 55, 350 + i * fontHeight);
      if(select == i){
        context.drawImage(Message.getSelect(), 30, 360 + (i - 1) * fontHeight);
      }
    }
  }

  void invokeScript(){
    this.message = null;
    this.selects = null;
    this.animateCounter = 2;

    while(scripts.hasNext()){
      String instruct = null, obj = null;
      String ops = null, data = null;

      String script = scripts.next();
      StringTokenizer token = new StringTokenizer(script);
      if(token.hasMoreTokens()) instruct = token.nextToken();
      if(token.hasMoreTokens()) obj = token.nextToken();
      if(token.hasMoreTokens()) ops = token.nextToken();
      if(token.hasMoreTokens()) data = token.nextToken();

      if("set".equals(instruct)){
        vars.put("#{" + obj + "}", data);
      }else if("cg".equals(instruct)){
        if("del".equals(obj)){
          if(ops == null) cgs.clear();
          else
            cgs.remove(ops);
        }else{
          int x = 0, y = 0;
          if(ops != null) x = Integer.parseInt(ops);
          if(data != null) y = Integer.parseInt(data);

          cgs.put(obj, new Cg(vars.get(obj), x, y));
        }
      }else if("mes".equals(instruct)){
        message = parseMes(obj);
        break;
      }else if("if".equals(instruct)){

      }else if("in".equals(instruct)){
        parseSelect();
        break;
      }
    }
  }

  private void readScript(String file){
    String path = this.getClass().getResource("/com/zhc/arpg/avg/script/" + file).getFile();
    BufferedReader reader = null;
    try{
      reader = new BufferedReader(new FileReader(path));
      this.scripts = new LineReader();
      String statement = null;
      while((statement = reader.readLine()) != null){
        statement = statement.trim();
        if(statement.length() == 0) continue;
        // 单行注释
        if(statement.startsWith("//") || statement.startsWith("#")) continue;

        scripts.addLine(statement);
      }
    }catch(Exception e){
      e.printStackTrace();
    }finally{
      IOUtils.closeQuietly(reader);
    }

    invokeScript();
  }

  private String parseMes(String statement){
    StringBuilder param = new StringBuilder();
    StringBuilder msg = new StringBuilder();
    boolean start = false;
    for(int j = 0; j < statement.length(); j++){
      char temp = statement.charAt(j);
      if(start){
        if(temp == '}'){
          msg.append(vars.get("#{" + param + "}"));
          param.delete(0, param.length());
          start = false;
        }else{
          param.append(temp);
        }
      }else if(temp == '#' && j + 1 < statement.length() && statement.charAt(j + 1) == '{'){
        start = true;
        j++;
      }else{
        msg.append(temp);
      }
    }

    return msg.toString();
  }

  private void parseSelect(){
    selects = new ArrayList<String>(3);
    String line = scripts.next();
    while(!"out".equals(line)){
      selects.add(line);
      line = scripts.next();
    }
  }

}
