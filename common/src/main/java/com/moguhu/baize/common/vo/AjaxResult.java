package com.moguhu.baize.common.vo;

public class AjaxResult {

    public static final Integer SUCCESS_CODE = 1000;

    public static final Integer FAIL_CODE = -1;

    private Integer code;

    private String msg;

    private Object data;

    private AjaxResult(Integer code) {
        this.code = code;
    }

    private AjaxResult(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static AjaxResult success() {
        return new AjaxResult(AjaxResult.SUCCESS_CODE);
    }

    public static AjaxResult success(Object data) {
        return new AjaxResult(AjaxResult.SUCCESS_CODE, "", data);
    }

    public static AjaxResult success(String msg, Object data) {
        return new AjaxResult(AjaxResult.SUCCESS_CODE, msg, data);
    }

    public static AjaxResult error() {
        return new AjaxResult(AjaxResult.FAIL_CODE);
    }

    public static AjaxResult error(String msg) {
        return new AjaxResult(AjaxResult.FAIL_CODE, msg, null);
    }

    public static AjaxResult error(String msg, Object data) {
        return new AjaxResult(AjaxResult.FAIL_CODE, msg, data);
    }

    public static AjaxResult error(Integer failCode, String msg, Object data) {
        return new AjaxResult(failCode, msg, data);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
