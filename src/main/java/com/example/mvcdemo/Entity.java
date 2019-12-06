package com.example.mvcdemo;



public class Entity {
    private Long request;
    private String addition;

    public Long getRequest() {
        return request;
    }

    public void setRequest(Long request) {
        this.request = request;
    }

    public String getAddition() {
        return addition;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "request='" + request + '\'' +
                ", addition='" + addition + '\'' +
                '}';
    }
}
