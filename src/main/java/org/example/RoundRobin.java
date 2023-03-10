package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;

import static spark.Spark.port;
import static spark.Spark.staticFiles;
import static spark.Spark.*;

public class RoundRobin {

    public static String url = "http://";
    private static final String USER_AGENT = "Mozilla/5.0";
    private static int valueRoundRobin = 0;


    public static void main(String... args){

        port(getPort());

        staticFiles.location("/public");

        get("/app", (req,res) -> getData());

        post("/app", (req,res) -> getPost(req.body()));

        get("favicon.ico", (req,res) -> "");

    }

    public static String getData() throws IOException {
        String[] ips = {"54.152.180.238", "100.26.214.186", "34.229.255.142"};
        Random r = new Random();
        String num = ips[r.nextInt(3)];
        URL obj = new URL(url + num + "/service");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader( con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            return "GET request not worked";
        }
    }

    public static String getPost(String text) throws IOException {
        String[] ips = {"54.152.180.238", "100.26.214.186", "34.229.255.142"};
        Random r = new Random();
        String num = ips[r.nextInt(3)];
        URL obj = new URL(url + num + "/service");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "text/plain");
        con.setRequestProperty("Accept", "text/plain");
        con.setDoOutput(true);

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = text.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader( con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            return "POST request not worked";
        }

    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4566;
    }
}
