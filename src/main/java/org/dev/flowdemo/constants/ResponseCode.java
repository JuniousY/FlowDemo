package org.dev.flowdemo.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),

    // 业务相关状态码（可根据需要扩展）
    BUSINESS_ERROR(1001, "业务异常"),
    DATA_NOT_EXIST(1002, "数据不存在"),
    DATA_EXISTED(1003, "数据已存在"),
    DATA_UPDATE_FAILED(1004, "数据更新失败"),
    DATA_DELETE_FAILED(1005, "数据删除失败");

    private final int code;
    private final String message;
}
