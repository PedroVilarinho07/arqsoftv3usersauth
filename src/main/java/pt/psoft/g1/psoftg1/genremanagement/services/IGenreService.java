package pt.psoft.g1.psoftg1.genremanagement.services;


import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.util.List;
import java.util.Optional;

public interface IGenreService {
    Iterable<Genre> findAll();
    Genre save(Genre genre);


}
