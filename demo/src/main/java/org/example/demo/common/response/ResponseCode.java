package org.example.demo.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class ResponseCode<T> implements Serializable {
    public static final ResponseCode<?> SUCCESS = new ResponseCode<>(0, "success");
    public static final ResponseCode<?> INVALID_PARAM = new ResponseCode<>(400, "Invalid parameter format");
    public static final ResponseCode<?> UNAUTHORIZED_ERROR = new ResponseCode<>(401, "Unauthorized");
    public static final ResponseCode<?> INSUFFICIENT_PRIVILEGE = new ResponseCode<>(403, "Insufficient privilege");
    public static final ResponseCode<?> RESOURCE_NOT_EXISTS = new ResponseCode<>(404, "Resource does not exist");
    public static final ResponseCode<?> NOT_FOUND = new ResponseCode<>(404, "Resource does not exist");
    public static final ResponseCode<?> RESOURCE_CONTENTION = new ResponseCode<>(409, "Resource contention");
    public static final ResponseCode<?> NOT_SUPPORT_MODIFY = new ResponseCode<>(410,"The current resource does not support modification");
    public static final ResponseCode<?>  GATEWAY_TIMEOUT = new ResponseCode<>(504,"Gateway timeout");
    public static final ResponseCode<?>  BAD_GATEWAY = new ResponseCode<>(502,"Gateway exception");
    public static final ResponseCode<?> SYSTEM_ERROR = new ResponseCode<>(500, "System exception");
    public static final ResponseCode<?> SYSTEM_BUSY = new ResponseCode<>(-1, "System busy");
    public static final ResponseCode<?> INDEX_OUT_OF_BOUND = new ResponseCode<>(100002, "Array index out of bound");
    public static final ResponseCode<?> TIME_OUT = new ResponseCode<>(100003, "Internal system timeout");
    public static final ResponseCode<?> INVALID_JSON_STRING = new ResponseCode<>(100004, "Invalid JSON string");
    public static final ResponseCode<?> RESOURCE_EXISTS = new ResponseCode<>(100005, "Resource already exists");

    public static final ResponseCode<?> NON_SUPPORTED_OPER = new ResponseCode<>(100007, "Unsupported operation");
    public static final ResponseCode<?> NO_PERMISSION = new ResponseCode<>(100008, "No permission to operate");
    public static final ResponseCode<?> SESSION_EXPIRED = new ResponseCode<>(100009, "Session expired, please log in again");
    public static final ResponseCode<?> REQUEST_METHOD_NOT_SUPPORTED = new ResponseCode<>(100010, "Request method not supported");
    public static final ResponseCode<?> MEDIA_TYPE_NOT_SUPPORTED = new ResponseCode<>(100011, "Unsupported media type");
    public static final ResponseCode<?> REQUEST_FREQUENCY_TOO_FAST = new ResponseCode<>(100012, "Access frequency too fast");
    public static final ResponseCode<?> LACK_NECESSARY_REQUEST_HEADER = new ResponseCode<>(100013, "Missing necessary request header");
    public static final ResponseCode<?> NOT_YET_LOGIN = new ResponseCode<>(1000014, "Login expired, please log in again");
    public static final ResponseCode<?> REQUEST_URL_NOT_FOUND = new ResponseCode<>(1000015, "Request interface not found");
    public static final ResponseCode<?> REQUEST_DATA_NOT_VALID = new ResponseCode<>(1000016, "Invalid request data");
    public static final ResponseCode<?> REQUEST_SOURCE_NOT_ALLOWED = new ResponseCode<>(1000017, "Invalid request source");
    public static final ResponseCode<?> DATA_EXPIRED_NEED_REFRESH = new ResponseCode<>(100018, "Data expired, needs to be refreshed");
    public static final ResponseCode<?> REQUEST_EXPIRED = new ResponseCode<>(100019, "Request expired");
    public static final ResponseCode<?> SIGN_INVALID = new ResponseCode<>(100020, "Invalid signature");
    public static final ResponseCode<?> TOO_MANY_USER = new ResponseCode<>(100021, "Too many users for this business, please try again later");


    @JsonProperty(value = "code")
    private int code;

    @JsonProperty(value = "msg")
    private String msg;

    @JsonProperty(value = "data")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    @JsonIgnore
    private String i18nCode;

    public ResponseCode() {
    }

    public ResponseCode(int code, String msg, String i18nCode){
        this.code = code;
        this.msg = msg;
        this.i18nCode=i18nCode;
    }

    public ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseCode(ResponseCode<T> responseCode, String msg) {
        this.code = responseCode.getCode();
        this.msg = msg;
    }



    public static <T> ResponseCode<T> buildResponse(T data) {
        ResponseCode<T> responseCode = new ResponseCode<>(SUCCESS.getCode(), "请求成功");
        responseCode.setData(data);
        return responseCode;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.code == SUCCESS.getCode();
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

}

