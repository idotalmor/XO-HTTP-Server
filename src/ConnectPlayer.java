import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Ido Talmor on 05/06/2017.
 */
public class ConnectPlayer implements HttpHandler {

    static byte Players;
    static boolean Iso;
    int status=200;
    public void handle(HttpExchange httpExchange) throws IOException {
        switch(httpExchange.getRequestMethod().toUpperCase()){
            case "GET":{
                if(Players>=2)return;
                httpExchange.sendResponseHeaders(status,1);
                OutputStream os=httpExchange.getResponseBody();
                os.write(Iso?"O".getBytes():"X".getBytes());
                os.close();
                Players++;
                System.out.println("player added");
                Iso=!Iso;
            }
        }
    }

}
