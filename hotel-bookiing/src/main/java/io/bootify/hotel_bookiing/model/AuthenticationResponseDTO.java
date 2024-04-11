package io.bootify.hotel_bookiing.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AuthenticationResponseDTO {

    @NotNull
    @Size(max = 255)
    private String token;

}
