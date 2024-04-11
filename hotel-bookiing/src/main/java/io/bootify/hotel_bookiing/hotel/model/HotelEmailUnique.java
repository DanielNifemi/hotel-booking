package io.bootify.hotel_bookiing.hotel.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import io.bootify.hotel_bookiing.hotel.service.HotelService;
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
 * Validate that the email value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = HotelEmailUnique.HotelEmailUniqueValidator.class
)
public @interface HotelEmailUnique {

    String message() default "{Exists.hotel.email}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class HotelEmailUniqueValidator implements ConstraintValidator<HotelEmailUnique, String> {

        private final HotelService hotelService;
        private final HttpServletRequest request;

        public HotelEmailUniqueValidator(final HotelService hotelService,
                final HttpServletRequest request) {
            this.hotelService = hotelService;
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
            if (currentId != null && value.equalsIgnoreCase(hotelService.get(Long.parseLong(currentId)).getEmail())) {
                // value hasn't changed
                return true;
            }
            return !hotelService.emailExists(value);
        }

    }

}
