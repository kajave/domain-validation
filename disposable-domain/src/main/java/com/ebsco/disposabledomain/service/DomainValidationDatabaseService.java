package com.ebsco.disposabledomain.service;

import com.ebsco.disposabledomain.entity.Disposable;
import com.ebsco.disposabledomain.entity.ValidDomain;
import com.ebsco.disposabledomain.repo.DisposableRepo;
import com.ebsco.disposabledomain.repo.ValidDomainRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DomainValidationDatabaseService {

    @Autowired
    private DisposableRepo disposableRepo;

    @Autowired
    private ValidDomainRepo validDomainRepo;

    public Mono<String> validate(String domainName) {
        Optional<Disposable> optional = disposableRepo.findByDomainName(domainName);
        return Mono.just(optional.get().getDomainName());
    }

    public Mono<Set<String>> compare() {

        Set<String> listOfdisposable = disposableRepo.findAll().stream().map(Disposable::getDomainName).collect(Collectors.toSet());
        Set<String> listOfValid = validDomainRepo.findAll().stream().map(ValidDomain::getName).collect(Collectors.toSet());
        Set<String> matching = new HashSet<>(listOfValid);
        matching.retainAll(listOfdisposable);
        return Mono.just(matching);
    }


}
