package pt.psoft.g1.psoftg1.authormanagement.factories;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.bookmanagement.model.Isbn;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.model.LendingNumber;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderNumber;
import pt.psoft.g1.psoftg1.shared.factories.NameFactory;
import pt.psoft.g1.psoftg1.shared.factories.PhotoFactory;
import pt.psoft.g1.psoftg1.shared.model.domainService.IIdGenerator;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthorFactory {

    private final IIdGenerator idGeneratorService;
    private final AuthorNumberFactory authorNumberFactory;
    private final NameFactory nameFactory;
    private final BioFactory bioFactory;
    private final PhotoFactory photoFactory;

    public Author newAuthor(String name, String bio, String photoURI){
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Author name cannot be null or empty");
        }
        return new Author(idGeneratorService.generateId(name), name, bio, photoURI, authorNumberFactory, nameFactory, bioFactory, photoFactory);
    }
}
