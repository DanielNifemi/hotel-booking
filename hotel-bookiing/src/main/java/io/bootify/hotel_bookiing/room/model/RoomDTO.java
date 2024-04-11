package io.bootify.hotel_bookiing.room.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RoomDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String hotel;

    @NotNull
    @Size(max = 255)
    @RoomRoomNumberUnique
    private String roomNumber;

    @NotNull
    private RoomType type;

    @NotNull
    private Double price;

    @NotNull
    private Integer capacity;

    @Size(max = 255)
    private String description;

    @Size(max = 255)
    private String imageUrls;

    private Boolean available;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long hotels;

    private Long hotelId;

}
