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
    private Object data;

    public ResultBody success(List data){
        this.code = SUCCESS;
        this.message = "操作成功";
        this.data = data;
        return this;
    }

    public ResultBody success(String data){
        this.code = SUCCESS;
        this.message = "操作成功";
        this.data = data;
        return this;
    }


    public ResultBody error(String message){
        this.code = ERROR;
        this.message = message;
        return this;
    }

    public ResultBody failed(Object data){
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

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ResultBody{");
        sb.append("code=").append(code);
        sb.append(", message='").append(message).append('\'');
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}
