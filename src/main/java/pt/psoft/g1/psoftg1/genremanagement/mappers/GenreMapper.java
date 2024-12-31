package pt.psoft.g1.psoftg1.genremanagement.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.genremanagement.dto.*;

import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.shared.api.MapperInterface;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class GenreMapper extends MapperInterface{

    @Mapping(target = "genre", source = "genre")
    public abstract GenreDTO toGenreView(Genre genre);

    public abstract GenreDTO mapStringToGenreView(String genre);




}
