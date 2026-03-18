package c.n.t.a.s.j.a.base.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 *
 * @author Colain.Yin
 * @date 2025-10-18
 */
public class SdkObject implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("businessCode")
    private int businessCode = -1;
    @SerializedName("message")
    private String message;



    /**
     * Sets result code.
     *
     * @param businessCode the result code
     */
    public void setBusinessCode(int businessCode) {
        this.businessCode = businessCode;
    }


    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SdkObject{" +
                "businessCode=" + businessCode +
                ", message='" + message + '\'' +
                '}';
    }
}
