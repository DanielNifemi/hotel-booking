package io.bootify.hotel_bookiing.reservation.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReservationDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String room;

    @NotNull
    @Size(max = 255)
    private String user;

    @NotNull
    private LocalDate checkInDate;

    @NotNull
    private LocalDate checkOutDate;

    @NotNull
    private Integer numberOfGuests;

    @NotNull
    private Double totalPrice;

    @NotNull
    private ReservationStatus status;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime updatedAt;

    private Long roomId;

    private Long rooms;

    private Long users;

    private Long usersId;

}
