package pt.psoft.g1.psoftg1.bootstrapping;


import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.shared.factories.NameFactory;
import pt.psoft.g1.psoftg1.shared.factories.PhotoFactory;

import java.util.ArrayList;
import java.util.List;

public class Entities {


    public static List<Genre> getGenres() {
        List<Genre> genres = new ArrayList<>();

        genres.add(new Genre("Juvenile"));
        genres.add(new Genre("Science Fiction"));
        genres.add(new Genre("Children"));
        genres.add(new Genre("Fantasy"));
        genres.add(new Genre("Mystery"));
        genres.add(new Genre("Thriller"));
        genres.add(new Genre("Romance"));
        genres.add(new Genre("Horror"));
        genres.add(new Genre("Historical"));
        genres.add(new Genre("Biography"));
        genres.add(new Genre("Health"));
        genres.add(new Genre("Travel"));
        genres.add(new Genre("Children"));
        genres.add(new Genre("Young Adult"));
        genres.add(new Genre("Adventure"));
        genres.add(new Genre("Comics"));

        return genres;
    }
}
