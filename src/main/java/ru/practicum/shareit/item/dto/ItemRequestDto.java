package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class ItemRequestDto {
    @NotBlank(message = "Необходимо указать название предмета.")
    @Size(max = 255)
    private String name;
    @NotBlank(message = "Необходимо указать описание предмета.")
    @Size(max = 255)
    private String description;
    @NotNull
    private Boolean available;
    private Long requestId;
}
