package com.arpg.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.Channel;

/**
 * 文件流处理工具
 * 
 * @author zhou
 *
 */
public class IOUtils{

  private IOUtils(){
  }

  public static void closeQuietly(Writer writer){
    try{
      if(writer != null) writer.close();
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  public static void closeQuietly(Reader reader){
    try{
      if(reader != null) reader.close();
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  public static void closeQuietly(InputStream in){
    try{
      if(in != null) in.close();
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  public static void closeQuietly(OutputStream out){
    try{
      if(out != null) out.close();
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  public static void closeQuietly(Channel channel){
    try{
      if(channel != null) channel.close();
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  public static void closeQuietly(Socket socket){
    try{
      if(socket != null) socket.close();
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  public static void closeQuietly(ServerSocket server){
    try{
      if(server != null) server.close();
    }catch(Exception e){
      e.printStackTrace();
    }
  }

}