package com.example.yao.pm;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.util.Map;

public class NetworkFactory {

    public static EmptyClient touchMessageSocket(int uid, Handler handler){
        try {
            client = new EmptyClient(new URI("ws://54.250.199.234/api/notification/touch?uid="+uid));
            client.handler = handler;
            client.connect();
            Log.d("test","touch");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return client;
    }

    private static EmptyClient client;

    public static String Hello(){
        return "Hello";
    }

    public static Bitmap getImage(String src){
        try {
            Log.d("testImageDownloadError","1"+src);

            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(1000);
            Log.d("testImageDownloadError","1"+src);
            connection.connect();
            InputStream input = connection.getInputStream();
            Log.d("testImageDownloadError","2");

            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            Log.d("testImageDownloadError",e.getLocalizedMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static void httpConnectionPost(String apiUrl,Map<String, String> params,Handler handler) {
        PostTask task = new PostTask(apiUrl,"POST",params,handler);
        task.execute();
    }

    public static void httpConnectionGet(String apiUrl,Handler handler) {
        FetchTask task = new FetchTask(apiUrl,handler);
        task.execute();
    }

    public static String httpConnectionPostSync(String apiUrl,Map<String, String> params,Handler handler) {
        PostTask task = new PostTask(apiUrl,"POST",params,handler);
        String result = "";
        result = task.doInBackground();
        return result;
    }

    public static String httpGet(String apiUrl){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultStr = "";

        try {
            URL url = new URL(apiUrl);

            // create request
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            //get data
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            resultStr = buffer.toString();
        } catch (IOException e) {
            Log.d("testTaskError", "Error ", e);
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.d("testTaskError", "Error when closing stream", e);
                }
            }
        }
        return resultStr;
    }

    public static String httpPost(String urlStr,String method,Map<String,String> map){
        Log.d("testT","b:"+Thread.currentThread().getId());
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultStr = "";

        try {

            StringBuilder response = new StringBuilder();
            URL url = new URL(urlStr);


            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod(method);

            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setDoInput(true); //允許輸入流，即允許下載
            urlConnection.setDoOutput(true); //允許輸出流，即允許上傳
            urlConnection.setUseCaches(false); //設置是否使用緩存

            OutputStream os = urlConnection.getOutputStream();

            BufferedOutputStream bos = new BufferedOutputStream(os);

            DataOutputStream writer = new DataOutputStream(os);
            String jsonString = NetworkFactory.getJSONString(map);
            Log.d("testPost",jsonString);
            writer.write(jsonString.getBytes());
            writer.flush();
            writer.close();
            os.close();
            //Get Response

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            //get data
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            resultStr = buffer.toString();
            return resultStr;
        } catch (IOException e) {
            Log.d("TaskError", "Error ", e);
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.d("TaskError", "Error when closing stream", e);
                }
            }
        }
    }


    public static String httpConnectionGetSync(String apiUrl,Handler handler) {
        FetchTask task = new FetchTask(apiUrl,handler);
        String result = "";
        result = task.doInBackground();
        return result;
    }

    public static String getJSONString(Map<String, String> params){
        JSONObject json = new JSONObject();
        for(String key:params.keySet()) {
            try {
                String UrlEncodedStr = URLEncoder.encode(params.get(key));
                System.out.println(params.get(key));
                System.out.println(UrlEncodedStr);
                json.put(key, params.get(key));
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return json.toString();
    }
}


class EmptyClient extends WebSocketClient {

    public Handler handler;

    public int getRetryTimer() {
        return retryTimer;
    }

    public void setRetryTimer(int retryTimer) {
        this.retryTimer = retryTimer;
    }

    private int retryTimer = 0;
    private void ApplyMessage(String str){

        Message msg = new Message();
        msg.obj = str;
        //1 = message
        msg.what = 1;
        handler.sendMessage(msg);
    }

    public EmptyClient(URI serverURI) {
        super(serverURI);
    }

    public void restartSocket(){
        this.close();
        this.reconnect();
    }

    public void restartDefine(URI serverURI){
        this.close();
        this.uri = serverURI;
        this.connect();
    }

    public void retry(){
        if(retryTimer > 0)
        {
            new Runnable(){
                @Override
                public void run(){
                    try {
                        Thread.sleep(5000);
                        restartSocket();
                    }
                    catch (Exception ex){
                        Log.e("test","SocketRestartThreadError");
                    }
                }
            }.run();
        }
    }


    @Override
    public void onOpen(ServerHandshake handshakedata) {
        send("message send");
        Log.d("testSocket","new connection opened");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.d("testSocket","closed with exit code " + code + " additional info: " + reason);
        retry();
    }

    @Override
    public void onMessage(String message) {
        Log.d("testWsmessage" , message);
        ApplyMessage(message);
    }

    @Override
    public void onMessage(ByteBuffer message) {
        ApplyMessage("received ByteBuffer");
    }

    @Override
    public void onError(Exception ex) {
        Log.d("testSocket","an error occurred:" + ex);
        retry();
    }
}

class FetchTask extends AsyncTask<String, Void, String> {

    public String getUrlStr() {
        return urlStr;
    }

    public void setUrlStr(String urlStr) {
        this.urlStr = urlStr;
    }

    private String urlStr = "http://54.250.199.234/api/whoami?accessuid=1";
    private String method = "GET";
    private Handler handler;

    public FetchTask(String urlStr, Handler handler) {
        this.urlStr = urlStr;
        this.handler = handler;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultStr = "";

        try {
            URL url = new URL(urlStr);

            // create request
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(method);
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            //get data
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            resultStr = buffer.toString();
            return resultStr;
        } catch (IOException e) {
            Log.d("testTaskError", "Error ", e);
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.d("testTaskError", "Error when closing stream", e);
                }
            }
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Message msg = new Message();
        msg.obj = result;
        msg.what = 1;
        handler.sendMessage(msg);
    }

}

class PostTask extends AsyncTask<String, Void, String> {


    public String getUrlStr() {
        return urlStr;
    }

    public void setUrlStr(String urlStr) {
        this.urlStr = urlStr;
    }

    private String urlStr = "http://54.250.199.234/api/whoami?accessuid=1";
    private String method = "POST";
    private Map<String,String> map;
    private Handler handler;



    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public PostTask(String urlStr, String method, Map<String, String> map, Handler handler) {
        this.urlStr = urlStr;
        this.method = method;
        this.map = map;
        this.handler = handler;
    }

    @Override
    protected String doInBackground(String... params) {
        Log.d("testT","b:"+Thread.currentThread().getId());
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultStr = "";

        try {

            StringBuilder response = new StringBuilder();
            URL url = new URL(urlStr);


            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod(method);

            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setDoInput(true); //允許輸入流，即允許下載
            urlConnection.setDoOutput(true); //允許輸出流，即允許上傳
            urlConnection.setUseCaches(false); //設置是否使用緩存

            OutputStream os = urlConnection.getOutputStream();

            BufferedOutputStream bos = new BufferedOutputStream(os);

            DataOutputStream writer = new DataOutputStream(os);
            String jsonString = NetworkFactory.getJSONString(map);
            Log.d("testPost",jsonString);
            writer.write(jsonString.getBytes());
            writer.flush();
            writer.close();
            os.close();
            //Get Response

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            //get data
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            resultStr = buffer.toString();
            return resultStr;
        } catch (IOException e) {
            Log.d("TaskError", "Error ", e);
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.d("TaskError", "Error when closing stream", e);
                }
            }
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Message msg = new Message();
        msg.obj = result;
        msg.what = 1;
        handler.sendMessage(msg);

    }
}




