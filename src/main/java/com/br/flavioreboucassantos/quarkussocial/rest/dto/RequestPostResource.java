package com.br.flavioreboucassantos.quarkussocial.rest.dto;

import jakarta.validation.constraints.NotBlank;

public class RequestPostResource {

    @NotBlank(message = "Text is Required")
    private String postText;

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }
}
