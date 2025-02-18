package com.example.demo.infrastructure.persistence.reseller;

import com.example.demo.domain.entity.reseller.PhoneNumberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumberEntity, Long> {

}