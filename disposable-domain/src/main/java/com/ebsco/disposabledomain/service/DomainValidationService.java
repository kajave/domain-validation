package com.ebsco.disposabledomain.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
public class DomainValidationService {


    @Autowired
    RestTemplate restTemplate;


    public Mono<String> validateDomainUsingRestApi(String path, String search) {
        return Mono.just(restTemplate.exchange(path, HttpMethod.GET, null, new ParameterizedTypeReference<byte[]>() {
                }))
                .map(responseEntity -> new String(responseEntity.getBody()))
                .map(str -> str.contains(search) ? search : "not contain");

    }

    public Mono<List<String>> compareCsv(String filePath1, String filePath2) {
        try (
                CSVReader csvReader1 = new CSVReader(new FileReader(filePath1));
                CSVReader csvReader2 = new CSVReader(new FileReader(filePath2))
        ) {
            List<String> blockedList = csvReader1.readAll().stream()
                    .map(line -> line[0])
                    .collect(Collectors.toList());

            List<String> validList = csvReader2.readAll().stream()
                    .map(line -> line[2])
                    .collect(Collectors.toList());

            List<String> list = blockedList.stream()
                    .filter(validList::contains)
                    .collect(Collectors.toList());

            return Mono.just(list);
        } catch (IOException | CsvException e) {
            return Mono.error(e);
        }
    }

    public Mono<String> authenticateDomain(String url, String serach) {
        if (validateUsingCsvFile("D:\\EBSCO\\disposable-domain\\large", serach)) {
            return Mono.just(serach);
        }
        loadCsvFile(url);
        if (validateUsingCsvFile("D:\\EBSCO\\disposable-domain\\large", serach)) {
            return Mono.just(serach);
        } else {
            return Mono.just("not contain in file");
        }
    }

    public void loadCsvFile(String url) {
        RequestCallback requestCallback = request -> request.getHeaders()
                .setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL));
        ResponseExtractor<Void> responseExtractor = response -> {
            Path path1 = Paths.get("D:\\EBSCO\\disposable-domain\\large");
            Files.copy(response.getBody(), path1, REPLACE_EXISTING);
            return null;
        };
        restTemplate.execute(URI.create(url), HttpMethod.GET, requestCallback, responseExtractor);
    }

    public boolean validateUsingCsvFile(String filePath, String searchDomain) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
           return bufferedReader.lines().anyMatch(s -> s.equals(searchDomain)) ? true:false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
