package com.hungnt.project_SB.repository.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "email", url = "https://api.brevo.com")
public interface EmailClient {
    @PostMapping(value = "v3/smtp/email", produces = MediaType.APPLICATION_JSON_VALUE)
    Object sendEmail(@RequestHeader("api-key") String apiKey, @RequestBody Object body);
}
