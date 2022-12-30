package ru.otus.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Vadim_Kraynov @email: vkrainov@vtb.ru
 * @since 30.12.2022
 */
@Data
@AllArgsConstructor
public class AddressDto {
    private Long id;
    private String street;
}
