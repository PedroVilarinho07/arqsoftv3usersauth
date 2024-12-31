package pt.psoft.g1.psoftg1.genremanagement.dto.amqp;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Genre Data for AMQP Communication")
public class GenreDTOAmqp {
    @NotBlank
    @Size(min = 1, max = 100)
    private String genre;

    private String description;
}
