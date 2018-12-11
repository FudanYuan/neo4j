package com.dbpj.neo4j.enums;

import lombok.Getter;

/**
 * @Author: Jeremy
 * @Date: 2018/12/11 11:49
 */
@Getter
public enum ResultEnum {
    SUCCESS(0, "成功"),
    ERROR(1, "错误"),
    TYPE_ERROR(2, "类型错误"),
    REQUEST_NULL(3, "请求为空")
    ;

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }
}
