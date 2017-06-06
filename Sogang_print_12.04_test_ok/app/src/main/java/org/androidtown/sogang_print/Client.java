package org.androidtown.sogang_print;

/**
 * Created by KoJunghwan on 2016-10-18.
 */

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;



public class Client extends AsyncTask<String, Void, Void> {
    String[] params;
    String postmessage;
    public String output;
    String path;
    private MyEventListener callback;


    Client( String pm, String[] params, MyEventListener cb) {
        this.params = params;
        this.callback = cb;
        this.postmessage=pm;
        output="-1";
    }

    Client(String pm, String[] params, String path, MyEventListener cb) {
        this.params = params;
        this.callback = cb;
        this.path = path;
        this.postmessage=pm;
        output="-1";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    HttpPost post;
    @Override
    protected Void doInBackground(String... str) {
        try {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create() //객체 생성...
                    .setCharset(Charset.forName("UTF-8")) //인코딩을 UTF-8로.. 다들 UTF-8쓰시죠?
                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            HttpClient client = AndroidHttpClient.newInstance("Android");
//post = new HttpPost("http://10.0.92.108:80" + postmessage); //전송할 URL

            //post = new HttpPost("http://192.168.219.110:80/" + postmessage); //전송할 URL
            post = new HttpPost("http://192.168.0.23:80/" + postmessage); //전송할 URL

            for (String param: params) {
                builder.addPart("param[]", new StringBody(param, ContentType.TEXT_PLAIN));
            }
            if (params.length> 4) {
                builder.addPart("content", new FileBody(new File(path))); //빌더에 FileBody 객체에 인자로 File 객체를 넣어준다.
                //  builder.addPart("testKey",  "testData" ); //스트링 데이터..
            }

            //  try {
            post.setEntity(builder.build()); //builder.build() 메쏘드를 사용하여 httpEntity 객체를 얻는다.
            HttpResponse httpRes = client.execute(post);

            HttpEntity httpEntity = httpRes.getEntity();
            InputStream inputStream = httpEntity.getContent();
// Response
            BufferedReader bufferdReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;

            while ((line = bufferdReader.readLine()) != null) {
                stringBuilder.append(line);
                System.out.println("line = "+line);
            }
            output=stringBuilder.toString();
            System.out.println("output : " +output);

            inputStream.close();


        } catch (UnsupportedEncodingException e) {
        } catch (ClientProtocolException e1) {
        } catch (IOException e1) {
        } catch (ParseException e) {
        }

        return null;
    }
    @Override
    protected void onPostExecute(Void feed) {
        if(callback != null) {
            callback.onEventCompleted();
            post.abort();
        }
        //post.abort();

    }
}




