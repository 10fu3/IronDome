package ru.plsecuritylab.irondome.Network;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by msfblue1 on 2017/04/05.
 */
public class HTTPConnect {

    public String getData(String URL) {
        String returndata = "";
        try {
            //APIサーバーに名前を渡してデータをGET
            URL url = new URL(URL);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "IronDome");
            connection.setDoInput(true);
            InputStream inStream = connection.getInputStream();
            BufferedReader input =
                    new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
            StringBuilder data = new StringBuilder();
            String line;
            while ((line = input.readLine()) != null) {
                data.append(line);
            }
            inStream.close();
            input.close();
            //データの取得に成功したら
            returndata = data.toString();

        } catch (Exception e) {
            return returndata;
        }
        return returndata;
    }

    public JSONObject getJSONData(String URL) {
        JSONObject json;
        try {

            String data = getData(URL);
            //データの取得に成功したら
            if (!data.isEmpty()) {
                try {
                    json = (JSONObject) new JSONParser().parse(data);

                } catch (ParseException e) {
                    e.printStackTrace();
                    json = new JSONObject();
                }
            } else {
                json = new JSONObject();
            }

        } catch (Exception e) {
            json = new JSONObject();
        }
        return json;
    }
}
