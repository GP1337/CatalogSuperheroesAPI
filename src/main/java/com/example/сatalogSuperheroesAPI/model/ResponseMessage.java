package com.example.сatalogSuperheroesAPI.model;

import lombok.Data;

import java.math.BigInteger;

@Data
public class ResponseMessage<T> {

    private static final String CODE_OK = "Ok";

    private String errorMessage = "";

    private T data;

    public ResponseMessage() {
    }

    public ResponseMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ResponseMessage(T data) {
        this.data = data;
    }

    public static String getElementNotFoundMessage(Class pClass, BigInteger id) {
        return pClass.getSimpleName() + " with id " + id + " not found";
    }

    public static String getIdUnacceptableMessage() {
        return "Id in POST request is unacceptable, use path variable";
    }

    public static ResponseMessage ResponsePostPutOk(){

        ResponseMessage responseMessage = new ResponseMessage();

        responseMessage.setData(CODE_OK);

        return responseMessage;

    }

}
