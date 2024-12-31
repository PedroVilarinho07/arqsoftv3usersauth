package pt.psoft.g1.psoftg1.usermanagement.infrastructure.repositories.impl.publishers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.usermanagement.api.UserAmqpMapper;
import pt.psoft.g1.psoftg1.usermanagement.dto.UserDTOAmqp;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.model.events.UserEvents;
import pt.psoft.g1.psoftg1.usermanagement.publishers.UserEventsPublisher;

@Service
@RequiredArgsConstructor
public class UserEventsRabbitmqPublisherImpl implements UserEventsPublisher {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private DirectExchange direct;

    private final UserAmqpMapper userAmqpMapper;

    @Override
    public void sendUserCreated(User user) {
        sendUserEvent(user, UserEvents.USER_CREATED);
    }

    @Override
    public void sendUserUpdated(User user) {
        sendUserEvent(user, UserEvents.USER_UPDATED);
    }

    private void sendUserEvent(User user, String userEventType) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // Map User to UserDTOAmqp
            UserDTOAmqp userDTOAmqp = userAmqpMapper.toUserDTOAMQP(user);

            // Serialize UserDTOAmqp to JSON
            String jsonString = objectMapper.writeValueAsString(userDTOAmqp);

            // Send message to RabbitMQ
            template.convertAndSend(direct.getName(), userEventType, jsonString);

            System.out.println("[x] Sent User Event: '" + jsonString + "'");
        } catch (Exception ex) {
            System.err.println("[x] Exception sending user event: '" + ex.getMessage() + "'");
        }
    }
}
