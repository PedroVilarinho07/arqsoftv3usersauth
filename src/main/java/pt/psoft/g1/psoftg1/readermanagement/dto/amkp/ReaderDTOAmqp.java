package pt.psoft.g1.psoftg1.readermanagement.dto.amkp;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Schema(description = "A Reader form for AMQP communication")
public class ReaderDTOAmqp {

    @NotNull
    private String id;

    @NotNull
    private String username;

    @NotNull
    private String fullName;

    @NotNull
    private String password;

    @NotNull
    private String readerNumber;

    @NotNull
    private String birthDate;

    private String phoneNumber;

    private boolean gdprConsent;

    private boolean marketingConsent;

    private boolean thirdPartyConsent;

    private List<String> interestList;

    @Setter
    @Getter
    private Map<String, Object> _links = new HashMap<>();
}
