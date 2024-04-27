package com.br.flavioreboucassantos.quarkussocial.rest.dto;

import java.time.LocalDateTime;

public class ResponsePost {

    public final String postText;
    public final LocalDateTime dateTime;

    public ResponsePost(String postText, LocalDateTime dateTime) {
        this.postText = postText;
        this.dateTime = dateTime;
    }
}
