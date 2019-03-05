package cn.wujw.textboot.model;

import java.io.Serializable;
import java.util.List;

/**
 * Desc:
 *
 * @author wujw
 * @email jiwenwu@outlook.com
 * @date 2019-02-25
 */
public class ResultBody implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int SUCCESS = 200;
    private static final int FAILED = 400;
    private static final int ERROR = 500;

    private int code;
    private String message;
    private List<?> data;
    private String key;

    public ResultBody success(List data){
        this.code = SUCCESS;
        this.message = "操作成功";
        this.data = data;
        return this;
    }

    public ResultBody success(String key){
        this.code = SUCCESS;
        this.message = "操作成功";
        this.key = key;
        return this;
    }

    public ResultBody error(String message){
        this.code = ERROR;
        this.message = message;
        return this;
    }

    public ResultBody failed(List data){
        this.code = FAILED;
        this.message = "校验失败";
        this.data = data;
        return this;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


    public Object getData() {
        return data;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "ResultBody{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", key='" + key + '\'' +
                '}';
    }
}
