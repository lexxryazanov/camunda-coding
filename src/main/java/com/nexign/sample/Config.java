package com.nexign.sample;

import com.nexign.sample.client.CamundaRestClient;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public CamundaRestClient getCamundaRestClient() {
        CamundaRestClient client = Feign.builder()
            .client(new OkHttpClient())
            .encoder(new GsonEncoder())
            .decoder(new GsonDecoder())
            .target(CamundaRestClient.class, "http://localhost:8080/rest");

        return client;
    }
}
