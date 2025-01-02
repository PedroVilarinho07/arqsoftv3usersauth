package pt.psoft.g1.psoftg1.readermanagement.mappers;

import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.readermanagement.dataModel.postgres.ReaderDataModel;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;

import java.util.List;
import java.util.stream.Collectors;

public class ReaderDetailsSQLMapper {

    public static ReaderDetails convertToDomainReader(ReaderDataModel readerDataModel, Reader reader) {
        if (readerDataModel == null) {
            return null;
        }

        return new ReaderDetails(
                readerDataModel.getReaderNumber(),
                reader,
                readerDataModel.getBirthDate().toString(),
                readerDataModel.getPhoneNumber(),
                readerDataModel.isGdprConsent(),
                readerDataModel.isMarketingConsent(),
                readerDataModel.isThirdPartySharingConsent(),
                readerDataModel.getPhotoURI(),
                readerDataModel.getInterestList().stream()
                        .map(Genre::new)
                        .collect(Collectors.toList())
        );
    }

    public static ReaderDataModel convertToDataModelReader(ReaderDetails readerDetails) {
        if (readerDetails == null) {
            return null;
        }

        String photo;
        try {
            photo = readerDetails.getPhoto().getPhotoFile();
        } catch (Exception ignored) {photo = null;}

        List<String> interests = readerDetails.getInterestList() != null
                ? readerDetails.getInterestList().stream()
                .map(Genre::getGenre)
                .toList()
                : List.of();

        return new ReaderDataModel(
                readerDetails.getReader().getName().getName(),
                readerDetails.getReaderNumber(),
                readerDetails.getBirthDate().getBirthDate(),
                readerDetails.getPhoneNumber(),
                readerDetails.isGdprConsent(),
                readerDetails.isMarketingConsent(),
                readerDetails.isThirdPartySharingConsent(),
                photo,
                interests
        );
    }


}
