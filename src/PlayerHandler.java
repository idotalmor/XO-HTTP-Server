import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

/**
 * Created by Ido Talmor on 27/05/2017.
 */
public class PlayerHandler implements HttpHandler {

    static boolean Iso,Moved;
    public static byte table[][]=new byte[3][3];
    static byte counter,xscore,oscore,GameStatus;//GameStatus - 0-gameon,1-x win,2-o win,3-tie
    short status=400;
    static int row,column;
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        if(PlayerHandler.GameStatus!=0)return;
        switch(httpExchange.getRequestMethod().toUpperCase()){
            case "GET":{String query= httpExchange.getRequestURI().getQuery();
                String[] str=query.split("=");
            if(str.length!=2)return;
            else{
                switch(str[1].toLowerCase()){
                    case "resetscore":{xscore=0;oscore=0;
                        httpExchange.sendResponseHeaders(200,"OK".length());
                        OutputStream os = httpExchange.getResponseBody();
                        os.write("OK".getBytes());
                        os.close();

                    }

                }

            }}
            case "POST":{
                JSONObject json=readJson(httpExchange.getRequestBody());
                boolean Player=json.optBoolean("Player");
                row=json.optInt("Row");
                column=json.optInt("Column");
               if(Player==PlayerHandler.Iso&&PlayerMove(row,column)){status=200;
                   PlayerHandler.Iso=!PlayerHandler.Iso;//toggle
               }
                Response(httpExchange);
            }

        }
    }


    private JSONObject readJson(InputStream is)throws IOException{
        String jsonStr = new BufferedReader(new InputStreamReader(is)).readLine();
        try {
            return new JSONObject(jsonStr);
        }catch (JSONException e){
            return null;
        }
    }

    private boolean PlayerMove(int row,int column){
        if(table[row][column]==0){
            table[row][column]=Iso?(byte)2:1;
            PlayerHandler.Moved=true;
            counter++;
            if(counter>=5&&check(row,column)){}
            return true;
        }return false;


    }

    public boolean check(int row,int column){
        byte i,matches,player=PlayerHandler.Iso?(byte)2:(byte)1,xlength=(byte)PlayerHandler.table.length;

        for(i=0,matches=0;i<xlength&&table[row][i]==player;i++){matches++;}
        if(matches==xlength){return Won();}

        for(i=0,matches=0;i<xlength&&table[i][column]==player;i++){matches++;}
        if(matches==xlength){return Won();}

        if(row+column==xlength-1||row==column){
            if(table[0][xlength-1]==player&&table[xlength-1][0]==player){
                for(i=0,matches=0;i<xlength&&table[i][xlength-1-i]==player;i++){matches++;}
                if(matches==xlength){return Won();}
            }
            if(table[0][0]==player&&table[xlength-1][xlength-1]==player){
                for(i=0,matches=0;i<xlength&&table[i][i]==player;i++){matches++;}
                if(matches==xlength){return Won();}
            }
        }

        if(counter==(byte)Math.pow(xlength,2)){
            GameStatus=3;
            return false;
        }
        return false;
    }

    private boolean Won(){
        PlayerHandler.Iso=!PlayerHandler.Iso;//toggle - the winner start the next game
        if(Iso){oscore++;GameStatus=2;}else {xscore++;GameStatus=1;}

        return true;
    }
    private void Response(HttpExchange httpExchange)throws IOException {
        JSONObject jn=new JSONObject();
        try {
            jn.put("Moved",PlayerHandler.Moved).put("GameStatus",PlayerHandler.GameStatus);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpExchange.sendResponseHeaders(status,jn.toString().length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(jn.toString().getBytes());
        os.close();
        PlayerHandler.Moved=false;
    }




}
