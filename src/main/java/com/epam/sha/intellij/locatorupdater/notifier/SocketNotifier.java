package com.epam.sha.intellij.locatorupdater.notifier;

import com.epam.sha.intellij.locatorupdater.handler.RequestHandler;
import com.epam.sha.intellij.locatorupdater.model.RequestData;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.net.HTTPMethod;
import java.io.UnsupportedEncodingException;
import org.apache.commons.net.io.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static java.net.URLDecoder.decode;

/**
 *
 */
public class SocketNotifier implements RequestNotifier {

  private static final Logger log = Logger.getInstance(SocketNotifier.class);
  private final Collection<RequestHandler> handlers = new HashSet<>();
  private final ServerSocket serverSocket;
  private final ObjectMapper mapper;
  private static final String CRLF = "\r\n";
  private static final String NL = "\n";

  public SocketNotifier(ServerSocket serverSocket) {
    this.serverSocket = serverSocket;
    this.mapper = initMapper();
  }

  public void addRequestHandler(RequestHandler handler) {
    handlers.add(handler);
  }

  public void run() {
    while (true) {
      Socket clientSocket;
      try {
        //noinspection SocketOpenedButNotSafelyClosed
        clientSocket = serverSocket.accept();
      }
      catch (IOException e) {
        if (serverSocket.isClosed()) {
          break;
        }
        else {
          log.error("Error while accepting", e);
          continue;
        }
      }

      InputStream inputStream = null;
      try {
        inputStream = clientSocket.getInputStream();
      }
      catch (IOException e) {
        log.error(e);
      }
      if (inputStream != null) {
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        try {
          String inputLine;
          StringBuilder requestString = new StringBuilder();

          while ((inputLine = in.readLine()) != null && !inputLine.equals(CRLF) && !inputLine.equals(NL) && !inputLine.isEmpty()) {
            requestString.append(inputLine);
          }
          clientSocket.getOutputStream().write(("HTTP/1.1 204 No Content" + CRLF + "Access-Control-Allow-Origin: *" + CRLF + CRLF).getBytes(StandardCharsets.UTF_8.name()));
          clientSocket.close();

          StringTokenizer tokenizer = new StringTokenizer(requestString.toString());
          String method = tokenizer.hasMoreElements() ? tokenizer.nextToken() : "";
          if (!method.equals(HTTPMethod.POST.name())) {
            log.warn("Only POST requests allowed");
            continue;
          }

          log.info("Received request " + requestString);
          Map<String, String> parameters = getParametersFromUrl(tokenizer.nextToken());
          RequestData data = mapper.convertValue(parameters, RequestData.class);
          log.info("Received data " + data);
          handle(data);
        }
        catch (IOException e) {
          log.error("Error", e);
        }
        finally {
          Util.closeQuietly(in);
        }
      }
    }
  }

  /**
   * Parse input string to parameters map
   * @param url
   * @return
   */
  private static Map<String, String> getParametersFromUrl(String url) throws UnsupportedEncodingException {
    String parametersString = url.substring(url.indexOf('?') + 1);
    Map<String, String> parameters = new HashMap<>();
    StringTokenizer tokenizer = new StringTokenizer(parametersString, "&");
    while (tokenizer.hasMoreElements()) {
      String[] parametersPair = tokenizer.nextToken().split("=", 2);
      if (parametersPair.length > 1) {
        parameters.put(parametersPair[0], decode(parametersPair[1], StandardCharsets.UTF_8.name()));
      }
    }

    return parameters;
  }


  /**
   * Processing incoming request with handler
   * @param request
   */
  private void handle(RequestData request) {
    for (RequestHandler handler : handlers) {
      handler.handle(request);
    }
  }

  private ObjectMapper initMapper(){
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return objectMapper;
  }
}
