package io.bootify.hotel_bookiing.hotel.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class HotelDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    @HotelNameUnique
    private String name;

    @NotNull
    @Size(max = 255)
    private String adress;

    @NotNull
    private Double rating;

    @Size(max = 255)
    private String description;

    @NotNull
    @Size(max = 255)
    private String contactNumber;

    @Size(max = 255)
    @HotelEmailUnique
    private String email;

    @Size(max = 255)
    private String website;

    @Size(max = 255)
    private String checkInTime;

    @Size(max = 255)
    private String checkOutTime;

    @Size(max = 255)
    private String imageUrls;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
