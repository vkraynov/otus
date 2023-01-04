package ru.otus.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * PhoneDto.
 *
 * @author Vadim_Kraynov
 * @since 30.12.2022
 */
@Data
@AllArgsConstructor
public class PhoneDto {
    private Long id;
    private String number;
}
