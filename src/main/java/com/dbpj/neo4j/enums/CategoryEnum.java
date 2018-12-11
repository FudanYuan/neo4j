package com.dbpj.neo4j.enums;

import lombok.Getter;

/**
 * @Author: Jeremy
 * @Date: 2018/12/11 13:44
 */
@Getter
public enum CategoryEnum {
    PAPER(0, "paper"),
    AUTHOR(1, "author"),
    CONFERENCE(2, "conference"),
    FIELD(3, "field"),
    DEPARTMENT(4, "department")
    ;
    private Integer code;

    private String message;

    CategoryEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }
}
