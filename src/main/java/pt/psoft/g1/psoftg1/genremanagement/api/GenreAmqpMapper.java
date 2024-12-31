package pt.psoft.g1.psoftg1.genremanagement.api;

import org.mapstruct.Mapper;
import pt.psoft.g1.psoftg1.genremanagement.dto.amqp.GenreDTOAmqp;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import java.util.List;

@Mapper(componentModel = "spring")
public interface GenreAmqpMapper {

    GenreDTOAmqp toGenreAmqpDTO(Genre genre);

    List<GenreDTOAmqp> toGenreAmqpDTOList(List<Genre> genres);

    Genre toGenre(GenreDTOAmqp genreDTOAmqp);
}
