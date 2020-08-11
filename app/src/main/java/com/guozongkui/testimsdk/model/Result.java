package com.guozongkui.testimsdk.model;

import com.guozongkui.testimsdk.common.ErrorCode;
import com.guozongkui.testimsdk.common.NetConstant;

public class Result<T> {
    public int code;
    public T result;

    public Result(){
    }

    public Result(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public boolean isSuccess(){
        return code == NetConstant.REQUEST_SUCCESS_CODE;
    }

    public String getErrorMessage(){
        return ErrorCode.fromCode(code).getMessage();
    }
}

