package c.n.t.a.s.j.a.client;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import c.n.t.a.s.j.a.contants.Constants;
import c.n.t.a.s.j.a.contants.ResultCode;
import c.n.t.a.s.j.a.utils.CommonUtils;
import c.n.t.a.s.j.a.utils.EnhancedJsonUtils;
import c.n.t.a.s.j.a.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static c.n.t.a.s.j.a.contants.Constants.*;

/**
 * @ClassName HttpUtils
 * @Author Colain.Yin
 * @date 2025/9/11 14:01
 */
public class HttpUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class.getSimpleName());

    private static final int BUFFER_SIZE = 4096;
    private static final String DEFAULT_CHARSET = Constants.CHARSET_UTF8;
    private static Locale locale = Locale.ENGLISH;
    private static JsonParser jsonParser = new JsonParser();
    private static List success = Arrays.asList(HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_CREATED, HttpURLConnection.HTTP_NO_CONTENT);

    /**
     * Sets local.
     *
     * @param locale the locale
     */
    public static void setLocal(Locale locale) {
        HttpUtils.locale = locale;
    }


    /**
     * The type Trust all trust manager.
     */
    public static class TrustAllTrustManager implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    }

    private HttpUtils() {
    }

    /**
     * Request string.
     *
     * @param requestUrl     the request url
     * @param requestMethod  the request method
     * @param connectTimeout the connect timeout
     * @param readTimeout    the read timeout
     * @param userData       the user data
     * @param headerMap      the header map
     * @return the string
     */
    public static String request(String requestUrl, String requestMethod, int connectTimeout, int readTimeout, String userData, Map<String, String> headerMap, int retryTimes) {
        return request(requestUrl, requestMethod, connectTimeout, readTimeout, userData, headerMap, null, retryTimes);
    }

    /**
     * Compress request string.
     *
     * @param requestUrl     the request url
     * @param requestMethod  the request method
     * @param connectTimeout the connect timeout
     * @param readTimeout    the read timeout
     * @param userData       the user data
     * @param headerMap      the header map
     * @return the string
     */
    public static String compressRequest(String requestUrl, String requestMethod, int connectTimeout, int readTimeout, String userData, Map<String, String> headerMap, int retryTimes) {
        return compressRequest(requestUrl, requestMethod, connectTimeout, readTimeout, userData, headerMap, null, retryTimes);
    }

    /**
     * Request string.
     *
     * @param requestUrl     the request url
     * @param requestMethod  the request method
     * @param connectTimeout the connect timeout
     * @param readTimeout    the read timeout
     * @param userData       the user data
     * @param headerMap      the header map
     * @param saveFilePath   the save file path
     * @return the string
     */
    public static String request(String requestUrl, String requestMethod, int connectTimeout, int readTimeout, String userData, Map<String, String> headerMap, String saveFilePath, int retryTimes) {
        try {
            return request(requestUrl, requestMethod, connectTimeout, readTimeout, userData, false, headerMap, saveFilePath);
        } catch (Exception e) {
            FileUtils.deleteFile(saveFilePath);
            logger.error("Exception Occurred. Details:", e);
            if (e instanceof IOException) {
                return EnhancedJsonUtils.getSdkJson(ResultCode.SDK_UN_CONNECT);
            } else {
                return EnhancedJsonUtils.getSdkJson(ResultCode.SDK_RQUEST_EXCEPTION);
            }

        }
    }

    /**
     * Request string.
     *
     * @param requestUrl     the request url
     * @param requestMethod  the request method
     * @param connectTimeout the connect timeout
     * @param readTimeout    the read timeout
     * @param headerMap      the header map
     * @param uploadFilePath the upload file path
     * @return the string
     */
    public static String uploadRequest(String requestUrl, String requestMethod, int connectTimeout, int readTimeout, Map<String, Object> requestParams, Map<String, String> headerMap, String uploadFilePath, int retryTimes) {
        try {
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = getConnection(requestUrl, connectTimeout, readTimeout);
                return uploadNewRequest(urlConnection, requestMethod, headerMap, uploadFilePath, requestParams);
                //return finalUploadRequest(urlConnection, requestMethod, requestParams, false, headerMap, uploadFilePath);
            } catch (IOException e) {
                if (e instanceof ConnectException) {
                    throw (ConnectException) e;
                } else if (e instanceof SocketTimeoutException) {
                    throw (SocketTimeoutException) e;
                }
                logger.error("IOException Occurred. Details:", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return EnhancedJsonUtils.getSdkJson(ResultCode.SDK_RQUEST_EXCEPTION);
        } catch (Exception e) {
            logger.error("Exception Occurred. Details:", e);
            if (e instanceof IOException) {
                return EnhancedJsonUtils.getSdkJson(ResultCode.SDK_UN_CONNECT);
            } else {
                return EnhancedJsonUtils.getSdkJson(ResultCode.SDK_RQUEST_EXCEPTION);
            }
        }
    }


    /**
     * 文件上传请求
     *
     * @param httpConn
     * @param requestMethod
     * @param headerMap
     * @param uploadFileName
     * @param params
     * @return
     * @throws ConnectException
     * @throws SocketTimeoutException
     */
    private static String uploadNewRequest(HttpURLConnection httpConn, String requestMethod,
                                           Map<String, String> headerMap, String uploadFileName,
                                           Map<String, Object> params) throws ConnectException, SocketTimeoutException {
        String boundary = "----" + System.currentTimeMillis();
        String LINE_FEED = "\r\n";
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            httpConn.setUseCaches(false);
            httpConn.setDoInput(true);
            httpConn.setRequestMethod(requestMethod);
            httpConn.setRequestProperty(Constants.ACCESS_LANGUAGE, getLanguageTag(locale));
            httpConn.setRequestProperty(REQ_HEADER_SDK_VERSION, Constants.SDK_VERSION);

            if ("GET".equalsIgnoreCase(requestMethod) || "DELETE".equalsIgnoreCase(requestMethod)) {
                httpConn.connect();
            } else {
                httpConn.setDoOutput(true);
            }

            // 设置额外 headers
            if (headerMap != null) {
                for (Map.Entry<String, String> header : headerMap.entrySet()) {
                    httpConn.setRequestProperty(header.getKey(), header.getValue());
                }
            }
            httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            OutputStream outputStream = httpConn.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true);

            // 添加表单参数
            if (params != null) {
                String userData = JSON.toJSONString(params);
                String requestParams = Base64.getEncoder().encodeToString(userData.getBytes(StandardCharsets.UTF_8));

                writer.append("--").append(boundary).append(LINE_FEED);
                writer.append("Content-Disposition: form-data; name=\"").append("params").append("\"")
                        .append(LINE_FEED);
                writer.append("Content-Type: text/plain; charset=UTF-8").append(LINE_FEED);
                writer.append(LINE_FEED).append(requestParams).append(LINE_FEED);
                writer.flush();
            }

            // 添加文件部分
            if (uploadFileName != null) {
                File uploadFile = new File(uploadFileName);
                String fileName = uploadFile.getName();
                writer.append("--").append(boundary).append(LINE_FEED);
                writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(fileName).append("\"")
                        .append(LINE_FEED);
                writer.append("Content-Type: ").append("application/octet-stream").append(LINE_FEED);
                writer.append(LINE_FEED);
                writer.flush();

                FileInputStream inputStream = new FileInputStream(uploadFile);
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
                inputStream.close();

                writer.append(LINE_FEED).flush();
            }
            // 结束 multipart 请求体
            writer.append("--").append(boundary).append("--").append(LINE_FEED);
            writer.close();

            // 读取响应
            int status = httpConn.getResponseCode();

            InputStream responseStream = success.contains(status) ?
                    httpConn.getInputStream() : httpConn.getErrorStream();

            Map<String, List<String>> map = httpConn.getHeaderFields();
            extractResponseHeader(map, headerMap);


            reader = new BufferedReader(new InputStreamReader(responseStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }

            return jsonParser.parse(response.toString()).getAsJsonObject().toString();
        } catch (SocketTimeoutException localSocketTimeoutException) {
            if (StringUtils.containsIgnoreCase(localSocketTimeoutException.toString(), "Read timed out")) {
                logger.error("SocketTimeoutException Occurred. Details:", localSocketTimeoutException);
                return EnhancedJsonUtils.getSdkJson(ResultCode.SDK_CONNECT_TIMEOUT);
            } else {
                throw localSocketTimeoutException;
            }

        } catch (ConnectException localConnectException) {
            throw localConnectException;
        } catch (FileNotFoundException fileNotFoundException) {
            logger.error("FileNotFoundException Occurred. Details:", fileNotFoundException);
            return EnhancedJsonUtils.getSdkJson(ResultCode.SDK_FILE_NOT_FOUND);
        } catch (Exception ignored) {
            logger.error("Exception Occurred. Details:", ignored);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error("IOException Occurred. Details:", e);
                }
            }
            if (writer != null) {
                writer.close();
            }
            if (httpConn != null) {
                httpConn.disconnect();
            }
        }

        return EnhancedJsonUtils.getSdkJson(ResultCode.SDK_RQUEST_EXCEPTION);
    }

    /**
     * Compress request string.
     *
     * @param requestUrl     the request url
     * @param requestMethod  the request method
     * @param connectTimeout the connect timeout
     * @param readTimeout    the read timeout
     * @param userData       the user data
     * @param headerMap      the header map
     * @param saveFilePath   the save file path
     * @return the string
     */
    public static String compressRequest(String requestUrl, String requestMethod, int connectTimeout, int readTimeout, String userData, Map<String, String> headerMap, String saveFilePath, int retryTimes) {
        try {
            return request(requestUrl, requestMethod, connectTimeout, readTimeout, userData, true, headerMap, saveFilePath);
        } catch (Exception e) {
            if (StringUtils.isNotBlank(saveFilePath)) {
                FileUtils.deleteFile(saveFilePath);
            }
            logger.error("Occurred. Details:", e);
            return EnhancedJsonUtils.getSdkJson(ResultCode.SDK_UN_CONNECT);
        }
    }

    private static boolean isExceptionShouldRetry(Throwable e) {
        if (e instanceof ConnectException) {
            return true;
        } else if (e instanceof SocketTimeoutException) {
            return true;
        } else {
            return false;
        }
    }

    private static String request(String requestUrl, String requestMethod, int connectTimeout, int readTimeout, String userData, boolean compressData,
                                  Map<String, String> headerMap, String saveFilePath) throws ConnectException, SocketTimeoutException {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = getConnection(requestUrl, connectTimeout, readTimeout);
            return finalRequest(urlConnection, requestMethod, userData, compressData, headerMap, saveFilePath);
        } catch (IOException e) {
            if (e instanceof ConnectException) {
                throw (ConnectException) e;
            } else if (e instanceof SocketTimeoutException) {
                throw (SocketTimeoutException) e;
            }
            logger.error("IOException Occurred. Details:", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return EnhancedJsonUtils.getSdkJson(ResultCode.SDK_RQUEST_EXCEPTION);
    }

    private static String finalRequest(HttpURLConnection urlConnection, String requestMethod, String userData, boolean compressData,
                                       Map<String, String> headerMap, String saveFilePath) throws ConnectException, SocketTimeoutException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        FileOutputStream fileOutputStream = null;
        String filePath = null;
        try {
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod(requestMethod);
            if (locale != null) {
                urlConnection.setRequestProperty(Constants.ACCESS_LANGUAGE, getLanguageTag(locale));
            }
            urlConnection.setRequestProperty(REQ_HEADER_SDK_VERSION, Constants.SDK_VERSION);
            if (headerMap != null) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            if ("GET".equalsIgnoreCase(requestMethod) || "DELETE".equalsIgnoreCase(requestMethod)) {
                urlConnection.connect();
            } else {
                urlConnection.setDoOutput(true);
            }

            if ((null != userData) && (userData.length() > 0)) {

                logger.info("http api req>>>>>>>>>{}", userData);
                OutputStream outputStream = null;
                try {
                    outputStream = urlConnection.getOutputStream();
                    if (!compressData) {
                        outputStream.write(userData.getBytes("UTF-8"));
                    } else {
                        //String hexString = CommonUtils.byteToHex(compressData(userData.getBytes("UTF-8")));
                        outputStream.write(compressData(userData.getBytes(StandardCharsets.UTF_8)));
                    }
                } finally {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                }
            }

            if (saveFilePath != null) {
                filePath = saveFilePath + File.separator + FileUtils.generateMixString(16);

                File fileDir = new File(saveFilePath);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                fileOutputStream = new FileOutputStream(filePath);

                int bytesRead;
                byte[] buffer = new byte[BUFFER_SIZE];
                while ((bytesRead = urlConnection.getInputStream().read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }


                return EnhancedJsonUtils.getSdkJson(ResultCode.SUCCESS, filePath);
            }
            Map<String, List<String>> map = urlConnection.getHeaderFields();
            List<String> contentTypeHeaders = map.get("Content-Type");
            String contentType = contentTypeHeaders != null && !contentTypeHeaders.isEmpty() ? contentTypeHeaders.get(0) : "";
            if (urlConnection.getResponseCode() == 200 || urlConnection.getResponseCode() == 201
                    || urlConnection.getResponseCode() == 204) {
                String contentEncoding = map.get("Content-Encoding") != null ? map.get("Content-Encoding").get(0) : "";
                InputStream inputStream = urlConnection.getInputStream();
                if ("gzip".equalsIgnoreCase(contentEncoding)) {
                    // Decompress GZIP content manually
                    inputStream = new GZIPInputStream(inputStream);
                    logger.info("response-gunzip");
                }
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            } else {
                if (urlConnection.getErrorStream() != null) {
                    bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream(), "utf-8"));
                } else {
                    bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                }
            }
            extractResponseHeader(map, headerMap);

            String str;
            while ((str = bufferedReader.readLine()) != null) {
                stringBuilder.append(str);
            }
            String resultStr = stringBuilder.toString();
            if (!StringUtils.containsIgnoreCase(contentType, "json") && StringUtils.isNotBlank(resultStr)) {
                logger.warn(resultStr);
            }
            //logger.info("http api rsp<<<<<<<<<{}", resultStr);
            if (StringUtils.isBlank(resultStr)) {
                resultStr = "{}";
            } else if (!StringUtils.startsWith(resultStr, "{")) {
                resultStr = String.format("{%s}", resultStr);
            }
            JsonObject json = jsonParser.parse(resultStr).getAsJsonObject();
            return json.toString();
        } catch (SocketTimeoutException localSocketTimeoutException) {
            if (StringUtils.containsIgnoreCase(localSocketTimeoutException.toString(), "Read timed out")) {
                FileUtils.deleteFile(filePath);
                logger.error("SocketTimeoutException Occurred. Details:", localSocketTimeoutException);
                return EnhancedJsonUtils.getSdkJson(ResultCode.SDK_CONNECT_TIMEOUT);
            } else {
                throw localSocketTimeoutException;
            }

        } catch (ConnectException localConnectException) {
            throw localConnectException;

        } catch (FileNotFoundException fileNotFoundException) {
            FileUtils.deleteFile(filePath);
            logger.error("FileNotFoundException Occurred. Details:", fileNotFoundException);
            return EnhancedJsonUtils.getSdkJson(ResultCode.SDK_FILE_NOT_FOUND);
        } catch (Exception ee) {
            FileUtils.deleteFile(filePath);
            logger.error("Exception Occurred. Details:", ee);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    logger.error("IOException Occurred. Details:", e);
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    logger.error("IOException Occurred. Details:", e);
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return EnhancedJsonUtils.getSdkJson(ResultCode.SDK_RQUEST_EXCEPTION);
    }

    private static Long extractResponseHeader(Map<String, List<String>> responseMap, Map<String, String> headerMap) {
        //logger.info("extractResponseHeader:{}", JSON.toJSONString(responseMap));
        Long cost = 0L;
        try {
            Long ts1 = headerMap.get(REQ_HEADER_SDK_TIMESTAMP_NS) != null ? Long.parseLong(headerMap.get(REQ_HEADER_SDK_TIMESTAMP_NS)) : 0L;
            List<String> headList = Arrays.asList(REQ_HEADER_SDK_AUTHAPP_NS, REQ_HEADER_SDK_AUTHRAND_NS,
                    REQ_HEADER_SDK_AUTHSIG_NS, REQ_HEADER_SDK_TIMESTAMP_NS);
            for (Map.Entry<String, List<String>> entry : responseMap.entrySet()) {
                if (headList.contains(entry.getKey())) {
                    headerMap.put(entry.getKey(), entry.getValue().get(0));
                }
            }
            if (headerMap.get(REQ_HEADER_SDK_TIMESTAMP_NS) != null) {
                cost = Long.parseLong(headerMap.get(REQ_HEADER_SDK_TIMESTAMP_NS)) - ts1;
            }
        } catch (Exception e) {
            logger.error("extractResponseHeader error", e);
        }
        return cost;
    }

    private static HttpURLConnection getConnection(String requestUrl, int connectTimeout, int readTimeout) throws IOException {
        URL url = new URL(requestUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (conn instanceof HttpsURLConnection) {
            HttpsURLConnection connHttps = (HttpsURLConnection) conn;
            try {
                SSLContext ctx = SSLContext.getInstance("TLS");
                ctx.init(null, new TrustManager[]{new TrustAllTrustManager()}, new SecureRandom());
//                ctx.init(null, new TrustManager[] { new SafeTrustManager(Constants.CA_PATH, Constants.REMOTE_URL) }, new SecureRandom());
                connHttps.setSSLSocketFactory(ctx.getSocketFactory());
                connHttps.setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
            } catch (GeneralSecurityException e) {
                logger.error("GeneralSecurityException Occurred. Details:", e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            connHttps.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            conn = connHttps;
        }

        conn.setConnectTimeout(connectTimeout);
        conn.setReadTimeout(readTimeout);
        return conn;
    }

    private static URL buildGetUrl(String url, String query) throws IOException {
        if (StringUtils.isEmpty(query)) {
            return new URL(url);
        }

        return new URL(buildRequestUrl(url, query));
    }

    /**
     * Build request url string.
     *
     * @param url     the url
     * @param queries the queries
     * @return the string
     */
    public static String buildRequestUrl(String url, String... queries) {
        if (queries == null || queries.length == 0) {
            return url;
        }

        StringBuilder newUrl = new StringBuilder(url);
        boolean hasQuery = url.contains("?");
        boolean hasPrepend = url.endsWith("?") || url.endsWith("&");

        for (String query : queries) {
            if (!StringUtils.isEmpty(query)) {
                if (!hasPrepend) {
                    if (hasQuery) {
                        newUrl.append("&");
                    } else {
                        newUrl.append("?");
                        hasQuery = true;
                    }
                }
                newUrl.append(query);
                hasPrepend = false;
            }
        }
        return newUrl.toString();
    }

    /**
     * Build query string.
     *
     * @param params  the params
     * @param charset the charset
     * @return the string
     * @throws IOException the io exception
     */
    public static String buildGetQuery(Map<String, Object> params, String charset) throws IOException {
        if (params == null || params.isEmpty()) {
            return null;
        }

        StringBuilder query = new StringBuilder();
        Set<Map.Entry<String, Object>> entries = params.entrySet();
        boolean hasParam = false;

        for (Map.Entry<String, Object> entry : entries) {
            String name = entry.getKey();
            String value = entry.getValue().toString();
            // 忽略参数名或参数值为空的参数
            if (CommonUtils.areNotEmpty(name, value)) {
                if (hasParam) {
                    query.append("&");
                } else {
                    hasParam = true;
                }

                query.append(name).append("=").append(URLEncoder.encode(value, charset));
            }
        }

        return query.toString();
    }

    /**
     * Gets response as string.
     *
     * @param conn the conn
     * @return the response as string
     * @throws IOException the io exception
     */
    protected static String getResponseAsString(HttpURLConnection conn) throws IOException {
        String charset = getResponseCharset(conn.getContentType());
        if (conn.getResponseCode() < 400) {
            String contentEncoding = conn.getContentEncoding();
            if (Constants.CONTENT_ENCODING_GZIP.equalsIgnoreCase(contentEncoding)) {
                return getStreamAsString(new GZIPInputStream(conn.getInputStream()), charset);
            } else {
                return getStreamAsString(conn.getInputStream(), charset);
            }
        } else {// Client Error 4xx and Server Error 5xx
            throw new IOException(conn.getResponseCode() + " " + conn.getResponseMessage());
        }
    }

    /**
     * Gets stream as string.
     *
     * @param stream  the stream
     * @param charset the charset
     * @return the stream as string
     * @throws IOException the io exception
     */
    public static String getStreamAsString(InputStream stream, String charset) throws IOException {
        try {
            Reader reader = new InputStreamReader(stream, charset);
            StringBuilder response = new StringBuilder();

            final char[] buff = new char[1024];
            int read = 0;
            while ((read = reader.read(buff)) > 0) {
                response.append(buff, 0, read);
            }

            return response.toString();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    /**
     * Gets response charset.
     *
     * @param ctype the ctype
     * @return the response charset
     */
    public static String getResponseCharset(String ctype) {
        String charset = DEFAULT_CHARSET;

        if (!StringUtils.isEmpty(ctype)) {
            String[] params = ctype.split(";");
            for (String param : params) {
                param = param.trim();
                if (param.startsWith("charset")) {
                    String[] pair = param.split("=", 2);
                    if (pair.length == 2) {
                        if (!StringUtils.isEmpty(pair[1])) {
                            charset = pair[1].trim();
                        }
                    }
                    break;
                }
            }
        }

        return charset;
    }

    /**
     * 使用默认的UTF-8字符集反编码请求参数值。
     *
     * @param value 参数值
     * @return 反编码后的参数值 string
     */
    public static String decode(String value) {
        return decode(value, DEFAULT_CHARSET);
    }

    /**
     * 使用默认的UTF-8字符集编码请求参数值。
     *
     * @param value 参数值
     * @return 编码后的参数值 string
     */
    public static String encode(String value) {
        return encode(value, DEFAULT_CHARSET);
    }

    /**
     * 使用指定的字符集反编码请求参数值。
     *
     * @param value   参数值
     * @param charset 字符集
     * @return 反编码后的参数值 string
     */
    public static String decode(String value, String charset) {
        String result = null;
        if (!StringUtils.isEmpty(value)) {
            try {
                result = URLDecoder.decode(value, charset);
            } catch (IOException e) {
                logger.error("IOException Occurred. Details:", e);
            }
        }
        return result;
    }

    /**
     * 使用指定的字符集编码请求参数值。
     *
     * @param value   参数值
     * @param charset 字符集
     * @return 编码后的参数值 string
     */
    public static String encode(String value, String charset) {
        String result = null;
        if (!StringUtils.isEmpty(value)) {
            try {
                result = URLEncoder.encode(value, charset);
            } catch (IOException e) {
                logger.error("IOException Occurred. Details:", e);
            }
        }
        return result;
    }

    /**
     * 从URL中提取所有的参数。
     *
     * @param query URL地址
     * @return 参数映射 map
     */
    public static Map<String, String> splitUrlQuery(String query) {
        Map<String, String> result = new HashMap<String, String>();

        String[] pairs = query.split("&");
        if (pairs != null && pairs.length > 0) {
            for (String pair : pairs) {
                String[] param = pair.split("=", 2);
                if (param != null && param.length == 2) {
                    result.put(param[0], param[1]);
                }
            }
        }

        return result;
    }

    /**
     * Compress data byte [ ].
     *
     * @param bytes the bytes
     * @return the byte [ ]
     * @throws IOException the io exception
     */
    public static byte[] compressData(byte[] bytes)
            throws IOException {
        if (null == bytes) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
        gzipOutputStream.write(bytes);
        gzipOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    private static String getLanguageTag(Locale locale) {
        if (locale != null) {
            String localeStr = locale.toString();
            return localeStr.replace("_", "-");
        }
        return null;
    }


}
