package com.example.demo.domain.service;

import com.example.demo.application.exception.BusinessException;
import com.example.demo.application.model.request.ResellerRequest;
import com.example.demo.application.model.response.ResellerResponse;
import com.example.demo.domain.entity.reseller.AddressEntity;
import com.example.demo.domain.entity.reseller.ContactEntity;
import com.example.demo.domain.entity.reseller.PhoneNumberEntity;
import com.example.demo.domain.entity.reseller.ResellerEntity;
import com.example.demo.domain.model.reseller.AddressDTO;
import com.example.demo.domain.model.reseller.ContactDTO;
import com.example.demo.domain.model.reseller.PhoneNumberDTO;
import com.example.demo.domain.service.impl.ResellerServiceImpl;
import com.example.demo.infrastructure.persistence.reseller.AddressRepository;
import com.example.demo.infrastructure.persistence.reseller.ContactRepository;
import com.example.demo.infrastructure.persistence.reseller.PhoneNumberRepository;
import com.example.demo.infrastructure.persistence.reseller.ResellerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResellerServiceImplTest {

    @Mock
    private ResellerRepository repository;

    @Mock
    private PhoneNumberRepository phoneNumberRepository;

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private ResellerServiceImpl resellerService;

    private ResellerRequest request;
    private ResellerEntity reseller1;
    private ResellerEntity reseller2;
    private List<AddressDTO> listAddress;
    private List<ContactDTO> listContact;
    private List<PhoneNumberDTO> listPhone;


    @BeforeEach
    void setUp() {

        this.listAddress = List.of(AddressDTO.builder()
            .id(1L)
            .street("Rua um")
            .number(1)
            .city("São Paulo")
            .state("SP")
            .postalCode("02269012")
            .country("Brasil")
            .build());

        this.listContact = List.of(
            ContactDTO.builder()
                .id(1L)
                .contactName("Contato 1")
                .principal(true)
                .build());

        this.listPhone = List.of(
            PhoneNumberDTO.builder()
                .id(1L)
                .phoneNumber(11956883247L)
                .build()
        );

        this.request = new ResellerRequest();
        request.setCnpj("69927396000113");
        request.setEmail("teste@teste.com");
        request.setFantasyName("teste 1");
        request.setCorporateName("teste 2");
        request.setAddress(this.listAddress);
        request.setContacts(this.listContact);
        request.setPhones(this.listPhone);

        this.reseller1 = new ResellerEntity(this.request);
        this.reseller1.setId(1L);
        this.reseller1.setPhones(
            request.getPhones().stream()
                .map(phone -> new PhoneNumberEntity(phone, this.reseller1) )
                .toList()
        );
        this.reseller1.setAddress(
            request.getAddress().stream()
                .map(address -> new AddressEntity(address, this.reseller1) )
                .toList()
        );
        this.reseller1.setContacts(
            request.getContacts().stream()
                .map(contact -> new ContactEntity(contact, this.reseller1) )
                .toList()
        );

        this.reseller2 = new ResellerEntity(this.request);
        this.reseller2.setId(2L);
    }

    @Test
    public void testCreateSellerSuccess() {

        when(repository.existsByCnpj(Long.valueOf(this.request.getCnpj()))).thenReturn(false);

        ResellerEntity savedEntity = new ResellerEntity(this.request);
        savedEntity.setId(1L);
        when(repository.save(any(ResellerEntity.class))).thenReturn(savedEntity);

        ResellerResponse response = resellerService.createSeller(this.request);

        assertNotNull(response, "A resposta não deve ser nula");
        assertEquals(1L, response.getId(), "O ID do revendedor deve ser 1");

        verify(repository, times(1)).existsByCnpj(Long.valueOf(this.request.getCnpj()));
        verify(repository, times(1)).save(any(ResellerEntity.class));
    }

    @Test
    public void testCreateSellerDuplicateCnpj() {

        when(repository.existsByCnpj(Long.valueOf(this.request.getCnpj()))).thenReturn(true);

        BusinessException exception =
                assertThrows(BusinessException.class, () -> resellerService.createSeller(this.request),
                        "Deve lançar exceção de CNPJ duplicado");

        assertEquals("CNPJ Duplicado", exception.getErrorResponse().getTitle());

        verify(repository, times(1)).existsByCnpj(Long.valueOf(this.request.getCnpj()));
        verify(repository, never()).save(any(ResellerEntity.class));
    }

    @Test
    public void testGetSellerById_Found() {

        Long resellerId = 1L;

        when(repository.findById(resellerId)).thenReturn(Optional.of(reseller1));

        ResellerResponse response = resellerService.getSellerById(resellerId);

        assertNotNull(response, "Deve retornar um ResellerResponse quando o revendedor for encontrado.");
        assertEquals(resellerId, response.getId(), "O ID do revendedor retornado deve ser igual ao ID buscado.");
    }

    @Test
    public void testGetSellerById_NotFound() {

        when(repository.findById(Mockito.any())).thenReturn(Optional.empty());

        ResellerResponse response = resellerService.getSellerById(1L);

        assertNull(response, "Deve retornar null quando nenhum revendedor for encontrado.");
    }

    @Test
    public void testGetAllSellers() {

        List<ResellerEntity> entityList = List.of(this.reseller1, this.reseller2);

        when(repository.findAll()).thenReturn(entityList);

        List<ResellerResponse> responses = resellerService.getAllSellers();

        assertNotNull(responses, "A lista de respostas não deve ser nula.");
        assertEquals(2, responses.size(), "A lista deve conter exatamente 2 elementos.");
        assertEquals(1L, responses.get(0).getId(), "O primeiro elemento deve ter ID igual a 1.");
        assertEquals(2L, responses.get(1).getId(), "O segundo elemento deve ter ID igual a 2.");
    }

    @Test
    void testCreateSellerInvalidCnpj() {
        ResellerRequest request = new ResellerRequest();
        request.setCnpj("12345678901234");

        BusinessException exception = assertThrows(BusinessException.class,
                () -> resellerService.createSeller(request));

        assertEquals("CNPJ inválido", exception.getErrorResponse().getTitle());
        assertEquals("O CNPJ informado não é válido", exception.getErrorResponse().getDescription());
        assertEquals(HttpStatus.CONFLICT.value(), exception.getErrorResponse().getStatus());
    }

    @Test
    void testUpdateSellerNotFound() {
        when(repository.existsById(anyLong())).thenReturn(false);

        assertThrows(BusinessException.class, () -> resellerService.updateSeller(1L, this.request),
                "Revendedor não encontrado");
    }

    @Test
    void testUpdateSellerCnpjDuplicated() {
        when(repository.existsById(anyLong())).thenReturn(true);
        when(repository.findById(anyLong())).thenReturn(Optional.of(this.reseller1));
        when(repository.existsByCnpj(anyLong())).thenReturn(true);

        assertThrows(BusinessException.class, () -> resellerService.updateSeller(1L, this.request), "CNPJ Duplicado");
    }

    @Test
    void testUpdateSellerSuccess() {
        when(repository.existsById(anyLong())).thenReturn(true);
        when(repository.findById(anyLong())).thenReturn(Optional.of(this.reseller1));
        when(phoneNumberRepository.existsById(anyLong())).thenReturn(true);
        when(addressRepository.existsById(anyLong())).thenReturn(true);
        when(contactRepository.existsById(anyLong())).thenReturn(true);

        resellerService.updateSeller(1L, this.request);

        verify(repository, times(1)).save(any(ResellerEntity.class));
    }

    @Test
    void testUpdateSellerInvalidCnpj() {
        ResellerRequest request = new ResellerRequest();
        request.setCnpj("12345678901234");

        BusinessException exception = assertThrows(BusinessException.class,
                () -> resellerService.updateSeller(1L, request));

        assertEquals("CNPJ inválido", exception.getErrorResponse().getTitle());
        assertEquals("O CNPJ informado não é válido", exception.getErrorResponse().getDescription());
        assertEquals(HttpStatus.CONFLICT.value(), exception.getErrorResponse().getStatus());
    }

    @Test
    void testUpdateSellerPhoneNotFound() {
        when(repository.existsById(anyLong())).thenReturn(true);
        when(repository.findById(anyLong())).thenReturn(Optional.of(reseller1));
        when(phoneNumberRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(BusinessException.class, () -> resellerService.updateSeller(1L, this.request),
                "Telefone não encontrado");
    }

    @Test
    void testUpdateSellerContactNotFound() {
        when(repository.existsById(anyLong())).thenReturn(true);
        when(repository.findById(anyLong())).thenReturn(Optional.of(reseller1));
        when(phoneNumberRepository.existsById(anyLong())).thenReturn(true);
        when(contactRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(BusinessException.class, () -> resellerService.updateSeller(1L, this.request),
                "Contato não encontrado");
    }

    @Test
    void testUpdateSellerAddressNotFound() {
        when(repository.existsById(anyLong())).thenReturn(true);
        when(repository.findById(anyLong())).thenReturn(Optional.of(reseller1));
        when(phoneNumberRepository.existsById(anyLong())).thenReturn(true);
        when(addressRepository.existsById(anyLong())).thenReturn(false);
        when(contactRepository.existsById(anyLong())).thenReturn(true);

        assertThrows(BusinessException.class, () -> resellerService.updateSeller(1L, this.request),
                "Endereço não encontrado");
    }

    @Test
    public void testDeleteSeller_Success() {

        Long sellerId = 1L;
        ResellerEntity entity = new ResellerEntity();
        entity.setId(sellerId);
        when(repository.findById(sellerId)).thenReturn(Optional.of(entity));

        resellerService.deleteSeller(sellerId);

        verify(repository, times(1)).delete(entity);
    }

    @Test
    public void testDeleteSeller_NotFound() {
        Long sellerId = 1L;
        when(repository.findById(sellerId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> resellerService.deleteSeller(sellerId),
                "Deve lançar BusinessException quando o revendedor não for encontrado"
        );
        assertEquals("Revendedor não encontrado", exception.getErrorResponse().getTitle());

        verify(repository, never()).delete(any(ResellerEntity.class));
    }
}