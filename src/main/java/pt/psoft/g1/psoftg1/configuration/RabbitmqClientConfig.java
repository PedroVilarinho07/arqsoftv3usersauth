package pt.psoft.g1.psoftg1.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pt.psoft.g1.psoftg1.genremanagement.api.GenreEventsRabbitmqReceiver;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreService;
import pt.psoft.g1.psoftg1.usermanagement.api.UserEventsRabbitmqReceiver;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderEventsRabbitmqReceiver;
import pt.psoft.g1.psoftg1.usermanagement.model.events.UserEvents;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderService;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

@Profile("!test")
@Configuration
public class RabbitmqClientConfig {

    // Exchange principal
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("LMS.users");
    }

    // Configurações específicas para UserEvents
    private static class UserReceiverConfig {

        @Bean(name = "autoDeleteQueue_User_Created")
        public Queue autoDeleteQueue_User_Created() {
            return new AnonymousQueue();
        }

        @Bean(name = "autoDeleteQueue_User_Deleted")
        public Queue autoDeleteQueue_User_Deleted() {
            return new AnonymousQueue();
        }

        @Bean
        public Binding bindingUserCreated(DirectExchange directExchange,
                                          @Qualifier("autoDeleteQueue_User_Created") Queue autoDeleteQueue_User_Created) {
            return BindingBuilder.bind(autoDeleteQueue_User_Created)
                    .to(directExchange)
                    .with(UserEvents.USER_CREATED);
        }

        @Bean
        public Binding bindingUserDeleted(DirectExchange directExchange,
                                          @Qualifier("autoDeleteQueue_User_Deleted") Queue autoDeleteQueue_User_Deleted) {
            return BindingBuilder.bind(autoDeleteQueue_User_Deleted)
                    .to(directExchange)
                    .with("user.deleted"); // Ajuste para a routing key correta
        }

        @Bean
        public UserEventsRabbitmqReceiver userReceiver(UserService userService) {
            return new UserEventsRabbitmqReceiver(userService);
        }
    }

    // Configurações específicas para ReaderEvents
    private static class ReaderReceiverConfig {

        @Bean(name = "autoDeleteQueue_Reader_Created")
        public Queue autoDeleteQueue_Reader_Created() {
            return new AnonymousQueue();
        }

        @Bean(name = "autoDeleteQueue_Reader_Deleted")
        public Queue autoDeleteQueue_Reader_Deleted() {
            return new AnonymousQueue();
        }

        @Bean
        public Binding bindingReaderCreated(DirectExchange directExchange,
                                            @Qualifier("autoDeleteQueue_Reader_Created") Queue autoDeleteQueue_Reader_Created) {
            return BindingBuilder.bind(autoDeleteQueue_Reader_Created)
                    .to(directExchange)
                    .with("reader.created");
        }

        @Bean
        public Binding bindingReaderDeleted(DirectExchange directExchange,
                                            @Qualifier("autoDeleteQueue_Reader_Deleted") Queue autoDeleteQueue_Reader_Deleted) {
            return BindingBuilder.bind(autoDeleteQueue_Reader_Deleted)
                    .to(directExchange)
                    .with("reader.deleted");
        }

        @Bean
        public ReaderEventsRabbitmqReceiver readerReceiver(ReaderService readerService) {
            return new ReaderEventsRabbitmqReceiver(readerService);
        }
    }

    // Configurações específicas para GenreEvents
    private static class GenreReceiverConfig {

        @Bean(name = "autoDeleteQueue_Genre_Created")
        public Queue autoDeleteQueue_Genre_Created() {
            return new AnonymousQueue();
        }

        @Bean(name = "autoDeleteQueue_Genre_Deleted")
        public Queue autoDeleteQueue_Genre_Deleted() {
            return new AnonymousQueue();
        }

        @Bean
        public Binding bindingGenreCreated(DirectExchange directExchange,
                                           @Qualifier("autoDeleteQueue_Genre_Created") Queue autoDeleteQueue_Genre_Created) {
            return BindingBuilder.bind(autoDeleteQueue_Genre_Created)
                    .to(directExchange)
                    .with("genre.created");
        }

        @Bean
        public Binding bindingGenreDeleted(DirectExchange directExchange,
                                           @Qualifier("autoDeleteQueue_Genre_Deleted") Queue autoDeleteQueue_Genre_Deleted) {
            return BindingBuilder.bind(autoDeleteQueue_Genre_Deleted)
                    .to(directExchange)
                    .with("genre.deleted");
        }

        @Bean
        public GenreEventsRabbitmqReceiver genreReceiver(GenreService genreService) {
            return new GenreEventsRabbitmqReceiver(genreService);
        }
    }
}
