package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Valid
@Getter
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id", nullable = false)
    private Long id;

    @NotBlank(message = "Необходимо указать название предмета.")
    @Size(min = 3, max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Необходимо указать описание предмета.")
    @Size(min = 3, max = 255)
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "is_available", nullable = false)
    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "owner_id")
//    @ToString.Exclude
    private User owner;

//    private ItemRequest request;
}
