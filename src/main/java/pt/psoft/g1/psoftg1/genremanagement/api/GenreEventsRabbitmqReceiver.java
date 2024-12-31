package pt.psoft.g1.psoftg1.genremanagement.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.genremanagement.dto.amqp.GenreDTOAmqp;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreService;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GenreEventsRabbitmqReceiver {

    private final GenreService genreService;

    @RabbitListener(queues = "#{autoDeleteQueue_Genre_Created.name}")
    public void receiveGenreCreated(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            GenreDTOAmqp genreDTOAmqp = objectMapper.readValue(message, GenreDTOAmqp.class);
            genreService.save(new Genre(genreDTOAmqp.getGenre()));
        } catch (Exception ex) {
            System.out.println("Error processing genre created event: " + ex.getMessage());
        }
    }



    @RabbitListener(queues = "#{autoDeleteQueue_Genre_Deleted.name}")
    public void receiveGenreDeleted(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            GenreDTOAmqp genreDTOAmqp = objectMapper.readValue(message, GenreDTOAmqp.class);
            genreService.findByString(genreDTOAmqp.getGenre()).ifPresent(genreService::delete);
        } catch (Exception ex) {
            System.out.println("Error processing genre deleted event: " + ex.getMessage());
        }
    }
}
