package com.engwork.pgndb.chessengine.wsclient;

import com.engwork.pgndb.chessengine.engineinfo.EngineInfo;
import com.engwork.pgndb.chessengine.engineinfo.EngineInfoManagingBean;
import com.engwork.pgndb.chessengine.utils.EngineInfoParser;
import com.engwork.pgndb.exceptionhandling.OperationException;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.ws.WebSocket;
import static okhttp3.ws.WebSocket.TEXT;
import okhttp3.ws.WebSocketCall;
import okhttp3.ws.WebSocketListener;
import okio.Buffer;

public class WebSocketClientBeanImpl implements WebSocketListener,WebSocketClientBean {
  private final EngineInfoManagingBean engineInfoManagingBean;
  public WebSocketClientBeanImpl(EngineInfoManagingBean engineInfoManagingBean) {
    this.engineInfoManagingBean = engineInfoManagingBean;
  }
  private boolean connected;
  private final ExecutorService writeExecutor = Executors.newSingleThreadExecutor();
  private String position;
  private Integer time;

  private final static String CHESS_ENGINE_ERROR="Chess engine error";
  private final static String POSITION_EVAL_START="Starting evaluating position";
  private final static String UNKNOWN_ERROR="Unknown error";
  private final static String UCI_NEW_GAME="ucinewgame";
  private final static String GO_MOVE_TIME="go movetime ";
  private final static String ENGINE_URL="/ws_engine";
  private final static String AUTHORIZATION="Authorization";
  private final static String BEARER="Bearer ";


  public void startEvaluatingPosition(String url,String token,String position, Integer time)  {
    if(connected)
      this.writeExecutor.shutdown();
    setData(position,time);
    OkHttpClient client = new OkHttpClient.Builder().build();
    okhttp3.Request request = new okhttp3.Request.Builder()
        .addHeader(AUTHORIZATION, BEARER + token)
        .url(url+ENGINE_URL)
        .build();
    WebSocketCall.create(client, request).enqueue(this);
  }

  private void setData(String position, Integer time) {
    this.position = position;
    this.time = time;
    engineInfoManagingBean.deleteEngineInfo();
    engineInfoManagingBean.insertEngineInfo(new EngineInfo(position,null,null,null,null));
  }

  @Override
  public void onOpen(WebSocket webSocket, Response response) {
    connected = true;
    try {
      webSocket.sendMessage(RequestBody.create(TEXT,UCI_NEW_GAME));
      webSocket.sendMessage(RequestBody.create(TEXT,String.format("position fen %s",position)));
      webSocket.sendMessage(RequestBody.create(TEXT,GO_MOVE_TIME+time));
    } catch (IOException e) {
      throw new OperationException(POSITION_EVAL_START,UNKNOWN_ERROR+": "+e.getMessage());
    }
  }

  @Override
  public void onFailure(IOException e, Response response) {
    connected = false;
    writeExecutor.shutdown();
    throw new OperationException(CHESS_ENGINE_ERROR,UNKNOWN_ERROR+": "+e.getMessage());
  }

  @Override
  public void onMessage(ResponseBody message) throws IOException {
    String msgString = message.string();
    if (Pattern.compile("^info .*").matcher(msgString).find()) {
      String fen = engineInfoManagingBean.getEngineInfo().get(0).getFen();
      EngineInfo engineInfo = EngineInfoParser.parse(msgString, fen);
      if (engineInfo != null) {
        engineInfoManagingBean.insertEngineInfo(engineInfo);
      }
    }
    message.close();
  }

  @Override
  public void onPong(Buffer payload) {

  }

  @Override
  public void onClose(int code, String reason) {
    connected = false;
    writeExecutor.shutdown();
  }
}
