import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by Ido Talmor on 27/05/2017.
 */
public class XoServer {
    public static void main(String[] args) throws IOException {
        HttpServer server=HttpServer.create(new InetSocketAddress(999),0);
        server.createContext("/ConnectPlayer",new ConnectPlayer());
        server.createContext("/Play",new PlayerHandler());
        server.createContext("/Refresh",new RefreshHandler());
        server.start();

    }
}
