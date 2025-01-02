package pt.psoft.g1.psoftg1.genremanagement.mappers;

import pt.psoft.g1.psoftg1.genremanagement.dataModel.postgres.GenreDataModel;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.util.ArrayList;
import java.util.List;

public class DataModalSQLGenreMapper {

    public static GenreDataModel convertToDataModelGenre(Genre genre) {
        GenreDataModel genreDataModel = new GenreDataModel();
        genreDataModel.setGenre(genre.getGenre());
        return genreDataModel;
    }
    public static Genre convertToDomainGenre(GenreDataModel genreDataModel) {
        return new Genre(genreDataModel.getGenre());
    }

    public static List<Genre> convertListToDomainGenre(List<GenreDataModel> genres) {
        List<Genre> newGenres = new ArrayList<>();
        for (GenreDataModel genreDataModel : genres) {
            Genre genre = convertToDomainGenre(genreDataModel);  // Assuming newGenre method accepts GenreDataModel and returns Genre
            newGenres.add(genre);
        }
        return newGenres;
    }
}
