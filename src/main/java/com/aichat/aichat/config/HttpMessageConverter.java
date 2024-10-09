package com.aichat.aichat.config;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;
import java.io.InputStream;

public class HttpMessageConverter extends AbstractHttpMessageConverter<ByteArrayResource>{

    public HttpMessageConverter() {
        super(new MediaType("audio", "mp3"));
    }

    @Override
    protected boolean supports(@NonNull Class<?> clazz) {
        return ByteArrayResource.class.isAssignableFrom(clazz);
    }

    @Override
    protected @NonNull ByteArrayResource readInternal(@NonNull Class<? extends ByteArrayResource> clazz, @NonNull HttpInputMessage inputMessage) throws IOException {
        try (InputStream inputStream = inputMessage.getBody()) {
            byte[] bytes = inputStream.readAllBytes();
            return new ByteArrayResource(bytes);
        }
    }

    @Override
    protected void writeInternal(@NonNull ByteArrayResource resource, @NonNull HttpOutputMessage outputMessage) throws IOException {
        outputMessage.getBody().write(resource.getByteArray());
    }

}
