package com.konkuk.vecto.image.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
public class ImageUrlResponse {
    List<String> url;

    public void setUrl(String url){
        this.url.add(url);
    }

    public void setUrl(List<String> url){
        this.url = url;
    }
}
