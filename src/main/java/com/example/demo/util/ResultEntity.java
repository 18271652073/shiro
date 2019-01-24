package com.example.demo.util;

/**
 * @author 佟星龙
 * @date 2018/6/15.
 */
public class ResultEntity {

    public static final String SC_INTERNAL_SERVER_ERROR = "500";
    public static final String CODE = "0000";

    private boolean success = true;

    private String resultMessage;

    private String resultCode;

    private Object result;

    public static ResultEntity build() {
        return new ResultEntity();
    }

    public static ResultEntity error() {
        return error(SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
    }

    public static ResultEntity error(String msg) {
        return error(SC_INTERNAL_SERVER_ERROR, msg);
    }

    public static ResultEntity error(String resultCode, String resultMessage) {
        return new ResultEntity().setResultCode(resultCode).setResultMessage(resultMessage)
            .setSuccess(false);
    }

    /**
     * 定义一个传入返回结果信息的ok方法
     */
    public static ResultEntity ok(String resultMessage) {
        return new ResultEntity().setSuccess(true).setResultCode(CODE)
            .setResultMessage(resultMessage);
    }

    /**
     * 定义一个无参的ok方法
     */
    public static ResultEntity ok() {
        return new ResultEntity().setSuccess(true).setResultCode(CODE);
    }

    public boolean getSuccess() {
        return success;
    }

    public ResultEntity setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public ResultEntity setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
        return this;
    }

    public String getResultCode() {
        return resultCode;
    }

    public ResultEntity setResultCode(String resultCode) {
        this.resultCode = resultCode;
        return this;
    }

    public Object getResult() {
        return result;
    }

    public ResultEntity setResult(Object result) {
        this.result = result;
        return this;
    }
}
