package io.bootify.hotel_bookiing.room.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import io.bootify.hotel_bookiing.room.service.RoomService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import org.springframework.web.servlet.HandlerMapping;


/**
 * Validate that the roomNumber value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = RoomRoomNumberUnique.RoomRoomNumberUniqueValidator.class
)
public @interface RoomRoomNumberUnique {

    String message() default "{Exists.room.roomNumber}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class RoomRoomNumberUniqueValidator implements ConstraintValidator<RoomRoomNumberUnique, String> {

        private final RoomService roomService;
        private final HttpServletRequest request;

        public RoomRoomNumberUniqueValidator(final RoomService roomService,
                final HttpServletRequest request) {
            this.roomService = roomService;
            this.request = request;
        }

        @Override
        public boolean isValid(final String value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equalsIgnoreCase(roomService.get(Long.parseLong(currentId)).getRoomNumber())) {
                // value hasn't changed
                return true;
            }
            return !roomService.roomNumberExists(value);
        }

    }

}
