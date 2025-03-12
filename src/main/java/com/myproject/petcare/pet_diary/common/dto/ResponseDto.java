package com.myproject.petcare.pet_diary.common.dto;

import lombok.Getter;

@Getter
public class ResponseDto {
    private boolean success;
    private String message;
    private Object data;

    public ResponseDto(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public ResponseDto(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
