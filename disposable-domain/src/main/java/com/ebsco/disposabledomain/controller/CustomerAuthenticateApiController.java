package com.ebsco.disposabledomain.controller;

import com.ebsco.disposabledomain.service.DomainValidationDatabaseService;
import com.ebsco.disposabledomain.service.DomainValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

@RestController
public class CustomerAuthenticateApiController {

    @Autowired
    DomainValidationService domainValidationService;
    @Autowired
    DomainValidationDatabaseService domainValidationDatabaseService;


    /*
    check the requested domain  using rest call to csv file through resttemplate
     */
    @GetMapping(value = "/email/authenticate/{domainName}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<String> authenticateDomainUsingRestApi(@PathVariable String domainName) {
        String filePath = "https://f.hubspotusercontent40.net/hubfs/2832391/Marketing/Lead-Capture/free-domains-2.csv";
        return domainValidationService.validateDomainUsingRestApi(filePath, domainName);
    }

    /*
    compare the requested domain first with file
    if it not fount reload file and again check with that file
     */
    @GetMapping(value = "/customers/authenticate/{emailDomain}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<String> authenticateDomainUsingCSVFile(@PathVariable String emailDomain) {
        String filePath = "https://f.hubspotusercontent40.net/hubfs/2832391/Marketing/Lead-Capture/free-domains-2.csv";
        return domainValidationService.authenticateDomain(filePath, emailDomain);
    }

    /*
    check domain using database where disposable data is stored
     */
    @GetMapping(value = "authenticate/{domainName}")
    public Mono<String> authenticateDomainUsingDatabase(@PathVariable String domainName) {
        return domainValidationDatabaseService.validate(domainName);
    }

    /*
    compare two csv file
     */
    @GetMapping(value = "/compare/csv")
    public Mono<List<String>> campareCsvFile() {
        String filePath1 = "D:\\EBSCO\\disposable-domain\\publicDomain.csv";
        String filePath2 = "D:\\EBSCO\\disposable-domain\\validDomain.csv";
        return domainValidationService.compareCsv(filePath1, filePath2);
    }

    /*
    compare two csv data through database
     */
    @GetMapping(value = "/compare", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<Set<String>> compareUsingDatabase() {
        return domainValidationDatabaseService.compare();
    }


}
