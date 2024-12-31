package pt.psoft.g1.psoftg1.usermanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Data
@Schema(description = "A User form for AMQP communication")
public class UserDTOAmqp {

    @NotNull
    private String id;

    @NotNull
    private String username;

    @NotNull
    private String fullName;

    @NotNull
    private String password;

    @NotNull
    private String role;

    @Setter
    @Getter
    private Map<String, Object> _links = new HashMap<>();
}
