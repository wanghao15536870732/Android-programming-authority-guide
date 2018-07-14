package com.example.lab.android.nuc.photogallery.Base;

import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FlickrFetchr {

    private static final String TAG = "FlickrFetchr";

    private static final String API_KEY = "d4b253a9d18ab84e3f075f41a255cc47";

    private static final String FETCH_RECENTS_METHOD = "flickr.photos.getRecent";
    private static final String SEARCH_METHOD = "flickr.photos.search";
    //这里创建了完整的Flickr API请求URL，便利类Url。Builder可创建正确转移的参数化URL
    private static final Uri ENDPOINT =  Uri.
            parse("https://api.flickr.com/services/rest/")
            .buildUpon()
            .appendQueryParameter("method", "flickr.photos.getRecent")
            .appendQueryParameter("api_key", API_KEY)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .appendQueryParameter("extras", "url_s")   //该参数制定了图片的大小
            //这里用Url.builder()构建了完整的Flickr APi请求URL,
            .build();



    public byte[] getUrlBytes(String urlSpec) throws IOException {
        //先根据传入的字符串参数，及网站，创建一个URL对象
        URL url = new URL(urlSpec);
        ///然后调用openConnection()方法创建一个只想要访问URL的链接对象。
        //强制转换成HttpURLConnection对象
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            //调用getInputStream()方法得到连接到指定的URl地址
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            //调用read()方法读循环读取网络数据，直到读取完为止
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        //将得到的数据转换成String类型的数据
        return new String(getUrlBytes(urlSpec));
    }

    public List<GalleryItem> fetchRecentPhotos(){
        String url = buildUrl(FETCH_RECENTS_METHOD,null);
        return downloadGalleryItems( url );
    }

    public List<GalleryItem> searchPhotos(String query){
        String url = buildUrl( SEARCH_METHOD,query );
        return downloadGalleryItems( url );
    }



    private List<GalleryItem> downloadGalleryItems(String url) {
        List<GalleryItem> items = new ArrayList<>();
        try {
            String jsonString = getUrlString(url);
            Log.e(TAG, "Received JSON: " + jsonString);
            //将jsonString 类型的数据装换成JSONObject而类型的
            JSONObject jsonBody = new JSONObject(jsonString);
            //调用parseItems()方法进行数组存储
            parseItems(items,jsonBody);
        } catch (JSONException e) {
            Log.e(TAG, "Failed to parse JSON");
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }
        return items;
    }

    /**
     * 创建新方法用于搜索下载和查询URL参数
     */
    private String buildUrl(String method,String query){
        Uri.Builder uriBuilder = ENDPOINT.buildUpon()
                .appendQueryParameter( "method",method );
        if (method.equals( SEARCH_METHOD )){
            uriBuilder.appendQueryParameter( "text",query );
        }
        return uriBuilder.build().toString();
    }

    //将多个jsonObject数据用数组包装
    private void parseItems(List<GalleryItem> items, JSONObject jsonBody) throws IOException, JSONException {
        JSONObject photosJsonObject = jsonBody.getJSONObject("photos");
        //这下面的photoJsonArray 是嵌套在身上面的JSONObject里面的
        JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");
        //去遍历整个数组，查找JsonObject
        for (int i = 0; i < photoJsonArray.length(); i++) { 
            //
            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);
            //创建对象来保存解析后的数据
            GalleryItem item = new GalleryItem();
            //选择存放Id和Caption
            item.setId(photoJsonObject.getString("id"));
            item.setCaption(photoJsonObject.getString("title"));

            //判断一下并不是每一张图片都有对应的url
            if (!photoJsonObject.has("url_s")) {
                continue;
            }
            item.setUrl(photoJsonObject.getString("url_s"));
            items.add(item);
        }
    }
}
