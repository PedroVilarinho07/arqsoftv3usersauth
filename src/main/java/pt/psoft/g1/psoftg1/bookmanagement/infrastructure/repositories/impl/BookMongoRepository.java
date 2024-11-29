package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.authormanagement.factories.AuthorFactory;
import pt.psoft.g1.psoftg1.bookmanagement.dto.request.BookCountDTO;
import pt.psoft.g1.psoftg1.bookmanagement.dto.request.SearchBooksQuery;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Profile("mongo")
public class BookMongoRepository implements BookRepository {

    private final AuthorFactory authorFactory;
    private final List<Book> staticBooks = new ArrayList<>();

    public BookMongoRepository(AuthorFactory authorFactory) {
        this.authorFactory = authorFactory;

        // Utilizao AuthorFactory para criar instâncias completas de Author
        staticBooks.add(new Book("9789720706386", "O País das Pessoas de Pernas Para o Ar",
                "Histórias divertidas e com múltiplos significados",
                new Genre("Infantil"),
                List.of(authorFactory.newAuthor("Manuel Antonio Pina",
                        "Manuel António Pina foi um jornalista e escritor português, premiado em 2011 com o Prémio Camões", null)),
                null));

        staticBooks.add(new Book("9789723716160", "Como se Desenha Uma Casa",
                "Poesia e reflexão sobre a vida",
                new Genre("Infantil"),
                List.of(authorFactory.newAuthor("Manuel Antonio Pina",
                        "Manuel António Pina foi um jornalista e escritor português, premiado em 2011 com o Prémio Camões", null)),
                null));

        staticBooks.add(new Book("9789895612864", "C e Algoritmos",
                "Linguagem C e estruturas de dados",
                new Genre("Informação"),
                List.of(authorFactory.newAuthor("Alexandre Pereira",
                        "Alexandre Pereira é licenciado e mestre em Engenharia Electrotécnica e de Computadores...", null)),
                null));

        staticBooks.add(new Book("9782722203402", "Introdução ao Desenvolvimento Moderno para a Web",
                "Desenvolvimento moderno de aplicações Web",
                new Genre("Informação"),
                List.of(
                        authorFactory.newAuthor("Filipe Portela",
                                "Docente convidado na Escola de Engenharia da Universidade do Minho. Investigador integrado do Centro Algoritmi...", null),
                        authorFactory.newAuthor("Ricardo Queirós",
                                "Docente na Escola Superior de Media Artes e Design do Politécnico do Porto...", null)
                ),
                null));
    }

    @Override
    public List<Book> findByGenre(String genre) {
        return staticBooks.stream()
                .filter(book -> book.getGenre().toString().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findXBooksByGenre(String genre, Integer numberOfBooks) {
        return staticBooks.stream()
                .filter(book -> book.getGenre().toString().equalsIgnoreCase(genre))
                .limit(numberOfBooks)
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findByTitle(String title) {
        return staticBooks.stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findByAuthorName(String authorName) {
        return staticBooks.stream()
                .filter(book -> book.getAuthors().stream().anyMatch(author -> author.getName().equalsIgnoreCase(authorName)))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return staticBooks.stream()
                .filter(book -> book.getIsbn().equals(isbn))
                .findFirst();
    }

    @Override
    public Page<BookCountDTO> findTop5BooksLent(LocalDate oneYearAgo, Pageable pageable) {
        List<BookCountDTO> topBooks = List.of(
                new BookCountDTO(staticBooks.get(0), 10),
                new BookCountDTO(staticBooks.get(1), 8),
                new BookCountDTO(staticBooks.get(2), 5),
                new BookCountDTO(staticBooks.get(3), 3)
        );

        return new PageImpl<>(topBooks.stream().limit(5).collect(Collectors.toList()), pageable, 5);
    }

    @Override
    public Book save(Book book) {
        staticBooks.add(book);
        return book;
    }

    @Override
    public void delete(Book book) {
        staticBooks.removeIf(b -> b.getIsbn().equals(book.getIsbn()));
    }

    @Override
    public List<Book> findBooksByAuthorNumber(String authorNumber) {
        return staticBooks.stream()
                .filter(book -> book.getAuthors().stream()
                        .anyMatch(author -> author.getAuthorNumber().toString().equals(authorNumber)))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> searchBooks(pt.psoft.g1.psoftg1.shared.services.Page page, SearchBooksQuery query) {
        return staticBooks;
    }

    @Override
    public List<Book> recommendBooksBasedOnAge(int age, int numberOfBooks, String readerNumber) {
        String genre;
        if (age < 10) {
            genre = "children";
        } else if (age >= 10 && age < 18) {
            genre = "juvenile";
        } else {
            genre = getMostLentGenreByReader(readerNumber); // método auxiliar para gênero mais emprestado
            if (genre == null) {
                throw new NotFoundException("No genre found for the reader's lending history.");
            }
        }

        // Retorna os livros recomendados com base no género identificado
        return findXBooksByGenre(genre, numberOfBooks);
    }

    // Método auxiliar para identificar o género mais emprestado de um leitor específico
    public String getMostLentGenreByReader(String readerNumber) {
        // Simula a contagem de empréstimos por género usando streams e coletores
        return staticBooks.stream()
                .filter(book -> book.getGenre() != null && book.getAuthors() != null)
                .flatMap(book -> book.getAuthors().stream()
                        .filter(author -> author.getAuthorNumber().equals(readerNumber))
                        .map(author -> book.getGenre().toString()))
                .collect(Collectors.groupingBy(g -> g, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

}
