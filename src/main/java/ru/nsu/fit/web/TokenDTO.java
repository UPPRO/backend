package ru.nsu.fit.web;

public class TokenDTO {
    private String data;

    public TokenDTO() {
    }

    TokenDTO(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
