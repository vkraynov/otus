package ru.otus.crm.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Vadim_Kraynov @email: vkrainov@vtb.ru
 * @since 30.12.2022
 */
@Data
public class ClientDto {
    private Long id;
    private String name;
    private AddressDto address;
    private List<PhoneDto> phones;
}
