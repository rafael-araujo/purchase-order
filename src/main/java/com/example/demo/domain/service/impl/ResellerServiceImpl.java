package com.example.demo.domain.service.impl;

import com.example.demo.application.exception.BusinessException;
import com.example.demo.application.model.request.ResellerRequest;
import com.example.demo.application.model.response.ResellerResponse;
import com.example.demo.application.utils.CnpjValidator;
import com.example.demo.domain.entity.reseller.ResellerEntity;
import com.example.demo.domain.service.ResellerService;
import com.example.demo.infrastructure.persistence.reseller.AddressRepository;
import com.example.demo.infrastructure.persistence.reseller.ContactRepository;
import com.example.demo.infrastructure.persistence.reseller.PhoneNumberRepository;
import com.example.demo.infrastructure.persistence.reseller.ResellerRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Log4j2
@Service
public class ResellerServiceImpl implements ResellerService {

    Logger logger = LoggerFactory.getLogger(ResellerServiceImpl.class);

    @Autowired
    private ResellerRepository repository;

    @Autowired
    private PhoneNumberRepository phoneNumberRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Transactional
    public ResellerResponse createSeller(ResellerRequest request) {
        logger.info("Iniciando criação do revendedor com CNPJ: {}", request.getCnpj());

        if (!CnpjValidator.isValidCnpj(request.getCnpj())) {
            logger.warn("CNPJ inválido: {}", request.getCnpj());
            throw new BusinessException(
                    "CNPJ inválido",
                    "O CNPJ informado não é válido",
                    HttpStatus.CONFLICT.value()
            );
        }

        if (repository.existsByCnpj(Long.valueOf(request.getCnpj()))) {
            logger.warn("CNPJ duplicado: {}", request.getCnpj());
            throw new BusinessException(
                    "CNPJ Duplicado",
                    "Já existe um Revendedor com este número de CNPJ",
                    HttpStatus.CONFLICT.value()
            );
        }

        ResellerEntity entity = repository.save(new ResellerEntity(request));
        logger.info("Revendedor criado com sucesso: {}", entity.getId());

        return new ResellerResponse(entity);
    }

    public ResellerResponse getSellerById(Long id) {
        logger.info("Buscando revendedor com ID: {}", id);

        Optional<ResellerEntity> entity = repository.findById(id);

        if (entity.isEmpty()) {
            logger.warn("Revendedor não encontrado com ID: {}", id);
        }

        return entity.map(ResellerResponse::new).orElse(null);
    }

    public List<ResellerResponse> getAllSellers() {
        logger.info("Buscando todos os revendedores");

        List<ResellerEntity> list = repository.findAll();
        List<ResellerResponse> listResult = new ArrayList<>();

        list.forEach(seller -> {
            listResult.add(new ResellerResponse(seller));
        });

        logger.info("Total de revendedores encontrados: {}", list.size());

        return listResult;
    }

    @Transactional
    public void updateSeller(Long id, ResellerRequest request) {
        logger.info("Iniciando atualização do revendedor com ID: {}", id);

        if (!CnpjValidator.isValidCnpj(request.getCnpj())) {
            logger.warn("CNPJ inválido: {}", request.getCnpj());
            throw new BusinessException(
                    "CNPJ inválido",
                    "O CNPJ informado não é válido",
                    HttpStatus.CONFLICT.value()
            );
        }

        if (!repository.existsById(id)) {
            logger.warn("Revendedor não encontrado com ID: {}", id);
            throw new BusinessException(
                    "Revendedor não encontrado",
                    "Não existe um Revendedor com este id",
                    HttpStatus.NOT_FOUND.value()
            );
        }

        Optional<ResellerEntity> entity = repository.findById(id);

        if (entity.isPresent()) {
            if (Objects.nonNull(request.getCnpj())) {
                if (repository.existsByCnpj(Long.valueOf(request.getCnpj()))) {
                    logger.warn("CNPJ duplicado: {}", request.getCnpj());
                    throw new BusinessException(
                            "CNPJ Duplicado",
                            "Já existe um Revendedor com este número de CNPJ",
                            HttpStatus.CONFLICT.value()
                    );
                }

                entity.get().setCnpj(Long.valueOf(request.getCnpj()));
            }

            if (Objects.nonNull(request.getCorporateName()))
                entity.get().setCorporateName(request.getCorporateName());

            if (Objects.nonNull(request.getFantasyName()))
                entity.get().setFantasyName(request.getFantasyName());

            if (Objects.nonNull(request.getEmail()))
                entity.get().setEmail(request.getEmail());

            if (Objects.nonNull(request.getPhones()) && !request.getPhones().isEmpty()) {
                request.getPhones().forEach(phone -> {
                    if (!phoneNumberRepository.existsById(phone.getId())) {
                        logger.warn("Telefone não encontrado com ID: {}", phone.getId());
                        throw new BusinessException(
                                "Telefone não encontrado",
                                "Não foi localizado telefone com id = " + phone.getId(),
                                HttpStatus.CONFLICT.value()
                        );
                    } else {
                        entity.get().getPhones().stream()
                                .filter(phoneOld -> Objects.equals(phoneOld.getId(), phone.getId()))
                                .forEach(phoneOld -> {
                                    if (Objects.nonNull(phone.getPhoneNumber()))
                                        phoneOld.setPhoneNumber(phone.getPhoneNumber());
                                });
                    }
                });
            }

            if (Objects.nonNull(request.getContacts()) && !request.getContacts().isEmpty()) {
                request.getContacts().forEach(contact -> {
                    if (!contactRepository.existsById(contact.getId())) {
                        logger.warn("Contato não encontrado com ID: {}", contact.getId());
                        throw new BusinessException(
                                "Contato não encontrado",
                                "Não foi localizado contato com id = " + contact.getId(),
                                HttpStatus.CONFLICT.value()
                        );
                    } else {
                        entity.get().getContacts().stream()
                                .filter(contactOld -> Objects.equals(contactOld.getId(), contact.getId()))
                                .forEach(contactOld -> {
                                    if (Objects.nonNull(contact.getContactName()))
                                        contactOld.setContactName(contact.getContactName());

                                    if (Objects.nonNull(contact.getPrincipal()))
                                        contactOld.setPrincipal(contact.getPrincipal());
                                });
                    }
                });
            }

            if (Objects.nonNull(request.getAddress()) && !request.getAddress().isEmpty()) {
                request.getAddress().forEach(address -> {
                    if (!addressRepository.existsById(address.getId())) {
                        logger.warn("Endereço não encontrado com ID: {}", address.getId());
                        throw new BusinessException(
                                "Endereço não encontrado",
                                "Não foi localizado endereço com id = " + address.getId(),
                                HttpStatus.CONFLICT.value()
                        );
                    } else {
                        entity.get().getAddress().stream()
                                .filter(addressOld -> Objects.equals(addressOld.getId(), address.getId()))
                                .forEach(addressOld -> {
                                    if (Objects.nonNull(address.getStreet()))
                                        addressOld.setStreet(address.getStreet());

                                    if (Objects.nonNull(address.getNumber()))
                                        addressOld.setNumber(address.getNumber());

                                    if (Objects.nonNull(address.getCity()))
                                        addressOld.setCity(address.getCity());

                                    if (Objects.nonNull(address.getState()))
                                        addressOld.setState(address.getState());

                                    if (Objects.nonNull(address.getPostalCode()))
                                        addressOld.setPostalCode(address.getPostalCode());

                                    if (Objects.nonNull(address.getCountry()))
                                        addressOld.setCountry(address.getCountry());
                                });
                    }
                });
            }

            repository.save(entity.get());
            logger.info("Revendedor atualizado com sucesso: {}", id);
        }
    }

    @Transactional
    public void deleteSeller(Long id) {
        logger.info("Iniciando deleção do revendedor com ID: {}", id);

        Optional<ResellerEntity> entity = repository.findById(id);

        if (entity.isEmpty()) {
            logger.warn("Revendedor não encontrado com ID: {}", id);
            throw new BusinessException(
                    "Revendedor não encontrado",
                    "Não existe um Revendedor com este id",
                    HttpStatus.NOT_FOUND.value()
            );
        }

        repository.delete(entity.get());
        logger.info("Revendedor deletado com sucesso: {}", id);
    }
}
