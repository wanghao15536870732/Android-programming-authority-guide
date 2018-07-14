# PhotoGallery
### 先根据传入的字符串参数，及网站，创建一个URL对象
### 然后调用openConnection()方法创建一个只想要访问URL的链接对象。
      强制转换成HttpURLConnection对象
```java
 public byte[] getUrlBytes(String urlSpec) throws IOException{
        //先根据传入的字符串参数，及网站，创建一个URL对象
        URL url = new URL(urlSpec);
        ///然后调用openConnection()方法创建一个只想要访问URL的链接对象。
        //强制转换成HttpURLConnection对象
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            //调用getInputStream()方法得到连接到指定的URl地址
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                throw new IOException(connection.getResponseMessage() +
                    ": with " +
                    urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            //调用read()方法读取网络数据，直到读取完为止
            while ((bytesRead = in.read(buffer)) > 0){
                out.write(buffer,0,bytesRead);
            }
            out.close();
            return out.toByteArray();
        }finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpet) throws IOException{
        //将得到的数据转换成String类型的数据
        return new String(getUrlBytes(urlSpet));
    }
```

### 这里创建了完整的Flickr API请求URL，便利类Url。Builder可创建正确转移的参数化URL
```java
 public void FetchItems() {
        try {
            //这里创建了完整的Flickr API请求URL，便利类Url。Builder可创建正确转移的参数化URL
            String url = Uri.parse("https://api.flickr.com/services/rest/")
                    .buildUpon()
                    .appendQueryParameter("method", "flickr.photos.getRecent")
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("extras", "url_s")
                    //这里用Url.builder()构建了完整的Flickr APi请求URL,
                    .build().toString();
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }
    }
```
