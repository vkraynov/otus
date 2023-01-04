package ru.otus.crm.dto;

import lombok.Data;

import java.util.List;

/**
 * ClientDto.
 *
 * @author Vadim_Kraynov
 * @since 30.12.2022
 */
@Data
public class ClientDto {
    private Long id;
    private String name;
    private AddressDto address;
    private List<PhoneDto> phones;
}
