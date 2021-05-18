package com.xiaozhan;

import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MyClientTest {
    @Test
    public void test01(){
        //1、创建HttpClient对象:包含请求 参数，发起请求，获取应答结果
        CloseableHttpClient httpClient= HttpClients.createDefault();
        //2、创建请求
        String uri="https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=13822222222";
        HttpGet httpGet=new HttpGet(uri);
        //3、向uri发起请求
        try {
            CloseableHttpResponse response= httpClient.execute(httpGet);
            //4、获取访问接口的结果，使用哪个response对象
           /* Header[] headers=response.getAllHeaders();
            for (Header header : headers) {
                System.out.println("header="+header);
            }*/
            StatusLine statusLine=response.getStatusLine();//StatusLine:包含httpStatus状态码
            if(statusLine.getStatusCode()== HttpStatus.SC_OK){
                //获取返回的数据 response.getEntity()
                HttpEntity entity=response.getEntity();//返回结果的内容
                InputStream in=entity.getContent();
                StringBuilder builder=new StringBuilder();
                BufferedReader br=new BufferedReader(new InputStreamReader(in,"GBK"));
                String line=null;
                while((line=br.readLine())!=null){
                    builder.append(line);
                }
                System.out.println("result="+builder.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    /*空指针异常：68行*/
    @Test
    public void test02(){
        CloseableHttpClient httpClient=HttpClients.createDefault();
        String uri="https://tcc.taobao.com/cc/json/mobile_tel_segment.htm";
        HttpPost post=new HttpPost(uri);
           //添加参数
            List<NameValuePair> pairs=new ArrayList<>();
            //添加参数和值
            pairs.add(new BasicNameValuePair("tel","13999995555"));
        try {
            //其他参数
            HttpEntity entity=new UrlEncodedFormEntity(pairs);
            post.setEntity(entity);
            //执行请求
            CloseableHttpResponse response=httpClient.execute(post);
            //获取结果
            if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK) {
                String str = EntityUtils.toString(response.getEntity());
                System.out.println("请求的返回结果：" + str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
