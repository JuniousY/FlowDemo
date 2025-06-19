package org.dev.flowdemo.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dev.flowdemo.constants.ResponseCode;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResp<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer code;    // 状态码
    private String message;  // 返回消息
    private T data;          // 返回数据
    private Long timestamp;  // 时间戳

    public ServiceResp(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> ServiceResp<T> success() {
        return new ServiceResp<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), null);
    }

    public static <T> ServiceResp<T> success(T data) {
        return new ServiceResp<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), data);
    }

    public static <T> ServiceResp<T> fail(ResponseCode responseCode) {
        return new ServiceResp<>(responseCode.getCode(), responseCode.getMessage(), null);
    }

    // 失败响应
    public static <T> ServiceResp<T> fail(Integer code, String message) {
        return new ServiceResp<>(code, message, null);
    }
}
