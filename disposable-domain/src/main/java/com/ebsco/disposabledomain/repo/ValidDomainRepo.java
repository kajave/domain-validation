package com.ebsco.disposabledomain.repo;

import com.ebsco.disposabledomain.entity.ValidDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValidDomainRepo extends JpaRepository<ValidDomain,Integer> {
}
