package com.example.demo.infrastructure.persistence.reseller;

import com.example.demo.domain.entity.reseller.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

}