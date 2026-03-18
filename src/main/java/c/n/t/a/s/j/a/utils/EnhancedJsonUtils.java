package c.n.t.a.s.j.a.utils;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import c.n.t.a.s.j.a.base.dto.SdkObject;
import c.n.t.a.s.j.a.contants.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @ClassName EnhancedJsonUtils
 * @Author Colain.Yin
 * @date 2025/9/11 14:27
 */
public class EnhancedJsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(EnhancedJsonUtils.class.getSimpleName());
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateTypeAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    /**
     * 根据javaBean生成Json对象格式字符串
     *
     * @param object 任意javaBean类型对象
     * @return 拼接好的String对象 string
     */
    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    /**
     * 根据Sdk返回的Json字符串生成Javabean，json字符串封装在data中
     *
     * @param <T>        the type parameter
     * @param sdkJsonStr Json字符串
     * @param clazz      the clazz
     * @return Javabean对象 t
     */
    public static <T> T fromJson(String sdkJsonStr, Class<T> clazz) {
        //logger.debug("Response body is [{}]\n", sdkJsonStr);
        return gson.fromJson(sdkJsonStr, clazz);
    }

    /**
     * From json t.
     *
     * @param <T>        the type parameter
     * @param sdkJsonStr the sdk json str
     * @param typeOfT    the type of t
     * @return the t
     */
    public static <T> T fromJson(String sdkJsonStr, Type typeOfT) {
        logger.debug(sdkJsonStr);
        return gson.fromJson(sdkJsonStr, typeOfT);
    }

    /**
     * Gets sdk json.
     *
     * @param resultCode the result code
     * @return the sdk json
     */
    public static String getSdkJson(int resultCode) {
        String message = "";
        switch (resultCode) {
            case ResultCode.SDK_PARAM_ERROR:
                message = "16100";
                break;
            case ResultCode.SDK_UNINIT:
                message = "16101";
                break;
            case ResultCode.SDK_DEC_ERROR:
                message = "16102";
                break;
            case ResultCode.SDK_JSON_ERROR:
                message = "16103";
                break;
            case ResultCode.SDK_CONNECT_TIMEOUT:
                message = "16104";
                break;
            case ResultCode.SDK_UN_CONNECT:
                message = "16105";
                break;
            case ResultCode.SDK_RQUEST_EXCEPTION:
                message = "16106";
                break;
            case ResultCode.SDK_UNZIP_FAILED:
                message = "16107";
                break;
            case ResultCode.SDK_MD_FAILED:
                message = "16108";
                break;
            case ResultCode.SDK_REPLACE_VARIABLES_FAILED:
                message = "16109";
                break;
            case ResultCode.SDK_INIT_FAILED:
                message = "16110";
                break;
            case ResultCode.SDK_FILE_NOT_FOUND:
                message = "16111";
                break;

        }
        message = MessageBundleUtils.getMessage(message);
        return getSdkJson(resultCode, message);
    }

    /**
     * Gets sdk json.
     *
     * @param resultCode the result code
     * @param message    the message
     * @return the sdk json
     */
    public static String getSdkJson(int resultCode, String message) {
        SdkObject sdkObject = new SdkObject();
        sdkObject.setBusinessCode(resultCode);
        sdkObject.setMessage(message);
        return toJson(sdkObject);
    }

    private static class DateTypeAdapter implements JsonDeserializer<Date> {
        private DateFormat format;

        /**
         * Instantiates a new Date type adapter.
         */
        DateTypeAdapter() {
        }

        /**
         * Instantiates a new Date type adapter.
         *
         * @param format the format
         */
        public DateTypeAdapter(DateFormat format) {
            this.format = format;
        }

        public synchronized Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            if (!(json instanceof JsonPrimitive)) {
                throw new JsonParseException("This is not a primitive value");
            }

            String jsonStr = json.getAsString();
            if (format != null) {

                try {
                    return format.parse(jsonStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return new Date(Long.parseLong(jsonStr));
        }
    }


    public static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

        private static final DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        @Override
        public void write(JsonWriter out, LocalDateTime value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            out.value(value.format(formatter));
        }

        @Override
        public LocalDateTime read(JsonReader in) throws IOException {
            String str = in.nextString();
            return LocalDateTime.parse(str, formatter);
        }
    }
}
