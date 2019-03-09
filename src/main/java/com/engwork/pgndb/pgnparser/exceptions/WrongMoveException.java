package com.engwork.pgndb.pgnparser.exceptions;

import lombok.Data;

@Data
public class WrongMoveException extends Exception {
    private String moveSan;
    public WrongMoveException(String san){
        super(String.format("Incorrect move with san: %s",san));
        this.moveSan = san;
    }
}
