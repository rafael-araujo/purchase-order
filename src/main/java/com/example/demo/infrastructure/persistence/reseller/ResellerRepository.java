package com.example.demo.infrastructure.persistence.reseller;

import com.example.demo.domain.entity.reseller.ResellerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResellerRepository extends JpaRepository<ResellerEntity, Long> {

    public Boolean existsByCnpj(Long cnpj);
}