package com.example.mandaprojet.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class EmbeddingService {
    private final String PYTHON_API_URL = "http://localhost:8000/extract-embedding/";

    public String sendImageForEmbedding(MultipartFile image) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<byte[]> requestEntity = new HttpEntity<>(image.getBytes(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                PYTHON_API_URL,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        return response.getBody();
    }
}
