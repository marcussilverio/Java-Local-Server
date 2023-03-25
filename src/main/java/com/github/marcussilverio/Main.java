package com.github.marcussilverio;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.jboss.logging.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.http.HttpResponse;

public class Main {
  public static void main(String[] args) throws IOException {
    int port = 8000;
    String filePath = "D:\\Downloads\\imdb-Top250Movies.JSON\\";;

    HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
    server.createContext("/", new FileHandler(filePath));
    server.setExecutor(null);
    server.start();
  }

  static class FileHandler implements HttpHandler {
    private final String filePath;

    public FileHandler(String filePath) {
      this.filePath = filePath;
    }

    public void handle(HttpExchange t) throws IOException {
      byte[] bytes = readBytesFromFile(filePath);
      if(bytes == null){
        bytes  = "ERROR - File not found".getBytes();
        t.sendResponseHeaders(404, bytes.length);
      }else {
        t.sendResponseHeaders(200, bytes.length);
      }
      OutputStream os = t.getResponseBody();
      os.write(bytes);
      System.out.println("[ERROR] => Request " + t.getRemoteAddress() + " responseCode: "+ t.getResponseCode());
      os.close();
    }

    private static byte[] readBytesFromFile(String filePath) throws IOException {
      File file = new File(filePath);
      if (!file.exists()){
        return null;
      }
      byte[] bytes = new byte[(int) file.length()];
      FileInputStream fis = new FileInputStream(file);
      fis.read(bytes);
      fis.close();
      return bytes;
    }
  }
}