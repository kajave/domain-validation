package com.ebsco.disposabledomain.repo;

import com.ebsco.disposabledomain.entity.Disposable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface DisposableRepo extends JpaRepository<Disposable,Integer> {


   @Query("select d from Disposable d where d.domainName = ?1")
   Optional<Disposable> findByDomainName(String domainName);
}
