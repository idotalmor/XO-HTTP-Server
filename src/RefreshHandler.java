import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

/**
 * Created by Ido Talmor on 27/05/2017.
 */
public class RefreshHandler implements HttpHandler {

    protected static int Password=973846;
    short status=200;
    JSONObject JN=new JSONObject();
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        switch(httpExchange.getRequestMethod().toUpperCase()){
            case "":
            case "GET":{
               String query= httpExchange.getRequestURI().getQuery();
               String[] str=query.split("=");
               if(str.length!=2)return;
               if(Password==Integer.parseInt(str[1])){
                   try {
                       JN.put("GameStatus",PlayerHandler.GameStatus).put("Iso",PlayerHandler.Iso).put("Row",PlayerHandler.row).put("Column",PlayerHandler.column)
                               .put("XScore",PlayerHandler.xscore).put("OScore",PlayerHandler.oscore);
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               }

                httpExchange.sendResponseHeaders(200,JN.toString().length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(JN.toString().getBytes());
                os.close();
            }

        }
    }



}
