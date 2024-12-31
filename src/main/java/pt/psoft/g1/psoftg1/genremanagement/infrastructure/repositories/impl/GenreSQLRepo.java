package pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.genremanagement.dataModel.sql.GenreDataModel;

import pt.psoft.g1.psoftg1.genremanagement.mappers.DataModalSQLGenreMapper;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Profile("sql")
@Repository
public class GenreSQLRepo implements GenreRepository {


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Iterable<Genre> findAll() {
        TypedQuery<GenreDataModel> query = entityManager.createQuery("SELECT g FROM GenreDataModel g", GenreDataModel.class);
        List<GenreDataModel> genreDataModels = query.getResultList();
        return genreDataModels.stream().map(DataModalSQLGenreMapper::convertToDomainGenre).collect(Collectors.toList());
    }

    @Override
    public Optional<Genre> findByString(String genreName) {
        TypedQuery<GenreDataModel> query = entityManager.createQuery("SELECT g FROM GenreDataModel g WHERE g.genre = :genreName", GenreDataModel.class);
        query.setParameter("genreName", genreName);
        return query.getResultStream().findFirst().map(DataModalSQLGenreMapper::convertToDomainGenre);
    }

    @Override
    public Genre save(Genre genre) {
        GenreDataModel genreDataModel = DataModalSQLGenreMapper.convertToDataModelGenre(genre);
        entityManager.persist(genreDataModel);
        return DataModalSQLGenreMapper.convertToDomainGenre(genreDataModel);
    }



    @Override
    public void delete(Genre genre) {
        GenreDataModel genreDataModel = entityManager.find(GenreDataModel.class, genre.getGenre());
        if (genreDataModel != null) {
            entityManager.remove(genreDataModel);
        }
    }
}


