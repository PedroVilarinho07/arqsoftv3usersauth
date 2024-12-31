package pt.psoft.g1.psoftg1.readermanagement.publishers;

import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

public interface ReaderEventsPublisher {

    void sendReaderCreated(ReaderDetails readerDetails);

    void sendReaderUpdated(ReaderDetails readerDetails);

    void sendReaderDeleted(String readerNumber);
}
