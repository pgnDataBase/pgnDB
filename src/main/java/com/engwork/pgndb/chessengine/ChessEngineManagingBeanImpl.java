package com.engwork.pgndb.chessengine;

import com.engwork.pgndb.appconfig.engineconfig.EngineConfig;
import com.engwork.pgndb.chessengine.wsclient.WebSocketClientBean;
import com.engwork.pgndb.exceptionhandling.OperationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

class ChessEngineManagingBeanImpl implements ChessEngineManagingBean{
  private final RestTemplate restTemplate;
  private final EngineConfig engineConfig;
  private final WebSocketClientBean webSocketClientBean;
  private String token=null;
  public ChessEngineManagingBeanImpl(RestTemplate restTemplate,EngineConfig engineConfig,WebSocketClientBean webSocketClientBean){
    this.restTemplate = restTemplate;
    this.engineConfig = engineConfig;
    this.webSocketClientBean = webSocketClientBean;
  }

  private final static String LOGIN_URL="/user/login";
  private final static String ENGINES_LIST_URL="/engine/available";
  private final static String ENGINE_START="/engine/start";
  private final static String ENGINE_STOP="/engine/stop";
  private final static String ENGINE_STARTING="Starting engine";
  private final static String UNABLE_START_ENGINE="Unable to start engine";
  private final static String ENGINE_LOGIN="Login to engine";

  private void login() {
    Request.LoginRequest loginRequest = new Request.LoginRequest(engineConfig.getLogin(),engineConfig.getPassword());
    ResponseEntity<Response.LoginResponse> response = restTemplate.postForEntity(engineConfig.getEngineUrl() + LOGIN_URL,
        loginRequest, Response.LoginResponse.class);
    Response.LoginResponse loginResponse = response.getBody();
    boolean isLoginSuccess = loginResponse != null && loginResponse.getSuccess();
    if (response.getStatusCode().equals(HttpStatus.OK) && isLoginSuccess) {
      token = loginResponse.getToken();
    } else {
      throw new OperationException(ENGINE_LOGIN,"");
    }
  }

  private HttpHeaders getHeaders(){
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Authorization",String.format("Bearer %s",token));
    return httpHeaders;
  }

  public Response.EnginesResponse getEngines(){
    if(token ==null)
      login();
    HttpEntity<String> entity = new HttpEntity<>("parameters", getHeaders());
    ResponseEntity<Response.EnginesResponse> response = restTemplate.exchange(engineConfig.getEngineUrl()+ENGINES_LIST_URL,
        HttpMethod.GET, entity, Response.EnginesResponse.class);
    return response.getBody();
  }

  public void startEngine(String engine,String position, Integer time){
    stopEngine();
    if(token ==null)
      login();
    Request.EngineStartRequest engineStart = new Request.EngineStartRequest(engine);
    HttpEntity<Request.EngineStartRequest> entity = new HttpEntity<>(engineStart, getHeaders());
    ResponseEntity<?> response = restTemplate.exchange(engineConfig.getEngineUrl()+ENGINE_START,
        HttpMethod.POST, entity, Void.class);
    if(!response.getStatusCode().is2xxSuccessful()){
      throw new OperationException(ENGINE_STARTING,UNABLE_START_ENGINE);
    }
    webSocketClientBean.startEvaluatingPosition(engineConfig.getEngineUrl(),token,position,time);
  }

  public Response.EngineStopResponse stopEngine(){
    if(token ==null)
      login();
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode obj = mapper.createObjectNode();
    HttpEntity<ObjectNode> entity = new HttpEntity<>(obj,getHeaders());
    ResponseEntity<Response.EngineStopResponse> response = restTemplate.exchange(engineConfig.getEngineUrl()+ENGINE_STOP,
        HttpMethod.POST, entity, Response.EngineStopResponse.class);
    return response.getBody();
  }
}
