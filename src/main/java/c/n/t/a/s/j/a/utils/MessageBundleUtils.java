package c.n.t.a.s.j.a.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @ClassName MessageBundleUtils
 * @Author Colain.Yin
 * @date 2025/9/11 11:50
 */
public class MessageBundleUtils {
    private static final String BASE_NAME = "messages";
    private static final Logger logger = LoggerFactory.getLogger(MessageBundleUtils.class);


    public static String getMessage(String key) {
        return getMessage(key, Locale.getDefault(), null);
    }

    public static String getMessage(String key, Object... args) {
        return getMessage(key, Locale.getDefault(), args);
    }

    public static String getMessage(String key, Locale locale, Object[] args) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        ResourceBundle rb = ResourceBundle.getBundle(BASE_NAME, locale);
        try {
            return loadArgs(rb.getString(key),  args);
        } catch (Exception mre) {
            logger.warn(mre.getMessage());  // exception is logged
            return key;
        }
    }


    private static String loadArgs(String message, Object[] args){
        if (StringUtils.isNotBlank(message) && args != null){
            return  MessageFormat.format(message, args);
        }
        return message;
    }

    public static void main(String[] args) {

    }
}
