package org.zj.utils.http;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.util.Map;

/**
 * 网络请求Util
 */

public class OkHttpUtil {


    /**
     * 通用的调用方法
     *
     * @param method
     * @param url
     * @param paramMap
     * @param resp
     */
    private void baseRequest(Map<String, String> headMap, String method, String url, Map<String, Object> paramMap, IResponce resp) {

        String paramJsonStr = JSONObject.toJSONString(paramMap);
        baseRequest(headMap, method, url, paramJsonStr, resp);
    }

    void baseRequest(Map<String, String> headParam, String method, String url, String paramJsonStr, IResponce resp) {

        System.out.println("请求地址:" + url);
        System.out.println("请求方法:" + method);

        OkHttpClient client = new OkHttpClient.Builder().build();


        String respStr = null;
        if (method.equals("POST")) {

            RequestBody requestBody = FormBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8")
                    , paramJsonStr);
            System.out.println("这是请求json 参数   " + paramJsonStr);
            Request.Builder header = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .removeHeader("User-Agent")
                    .header("Content-Type", "application/json; charset=utf-8")
                    .header("Accept", "application/json")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36");

            if(headParam!=null){
                for(Map.Entry<String,String> entry:headParam.entrySet()){
                    header.header(entry.getKey(),entry.getValue());
                }
            }

            Request request = header.build();

            Response execute;
            try {

                execute = client.newCall(request).execute();
                respStr = execute.body().string();
                System.out.println("这是响应信息" + respStr);


                System.out.println(respStr.contains("请求成功"));

                resp.handleResult(respStr);

            } catch (Exception e) {
                e.printStackTrace();
                resp.fail(e);
            }


        }


    }


}
