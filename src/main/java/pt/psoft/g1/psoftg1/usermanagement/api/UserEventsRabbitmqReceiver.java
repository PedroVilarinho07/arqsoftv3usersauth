package pt.psoft.g1.psoftg1.usermanagement.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.usermanagement.dto.UserDTOAmqp;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;
import pt.psoft.g1.psoftg1.usermanagement.services.CreateUserRequest;
import org.springframework.amqp.core.Message;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class UserEventsRabbitmqReceiver {

    private final UserService userService;

    @RabbitListener(queues = "#{autoDeleteQueue_User_Created.name}")
    public void receiveUserCreated(Message msg) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);
            UserDTOAmqp userDTOAmqp = objectMapper.readValue(jsonReceived, UserDTOAmqp.class);

            System.out.println(" [x] Received User Created by AMQP: " + msg + ".");


            try {
                userService.create(userDTOAmqp);
                System.out.println(" [x] New user inserted from AMQP: " + msg + ".");
            } catch (Exception e) {
                System.out.println(" [x] User already exists. No need to store it.");
            }
        } catch (Exception ex) {
            System.out.println(" [x] Exception receiving user event from AMQP: '" + ex.getMessage() + "'");
        }
    }


    @RabbitListener(queues = "#{autoDeleteQueue_User_Deleted.name}")
    public void receiveUserDeleted(String in) {
        System.out.println(" [x] Received User Deleted '" + in + "'");
    }
}
