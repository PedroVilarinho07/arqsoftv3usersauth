package pt.psoft.g1.psoftg1.readermanagement.infrastructure.repositories.impl.publishers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderAmqpMapper;
import pt.psoft.g1.psoftg1.readermanagement.dto.amkp.ReaderDTOAmqp;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.publishers.ReaderEventsPublisher;

@Service
@RequiredArgsConstructor
public class ReaderEventsRabbitmqPublisherImpl implements ReaderEventsPublisher {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private DirectExchange direct;

    private final ReaderAmqpMapper readerAmqpMapper;

    @Override
    public void sendReaderCreated(ReaderDetails readerDetails) {
        sendReaderEvent(readerDetails, "READER_CREATED");
    }

    @Override
    public void sendReaderUpdated(ReaderDetails readerDetails) {
        sendReaderEvent(readerDetails, "READER_UPDATED");
    }

    @Override
    public void sendReaderDeleted(String readerNumber) {
        this.template.convertAndSend(direct.getName(), "READER_DELETED", readerNumber);
    }

    private void sendReaderEvent(ReaderDetails readerDetails, String eventType) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ReaderDTOAmqp readerDTOAmqp = readerAmqpMapper.toReaderViewAMQP(readerDetails);
            String jsonString = objectMapper.writeValueAsString(readerDTOAmqp);

            this.template.convertAndSend(direct.getName(), eventType, jsonString);

            System.out.println(" [x] Sent '" + jsonString + "'");
        } catch (Exception ex) {
            System.out.println(" [x] Exception sending reader event: '" + ex.getMessage() + "'");
        }
    }
}
