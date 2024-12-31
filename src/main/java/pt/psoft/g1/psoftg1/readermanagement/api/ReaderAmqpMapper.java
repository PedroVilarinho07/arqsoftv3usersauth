package pt.psoft.g1.psoftg1.readermanagement.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.readermanagement.dto.amkp.ReaderDTOAmqp;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.shared.api.MapperInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class ReaderAmqpMapper extends MapperInterface {

    @Mapping(target = "readerNumber", source = "readerNumber")
    @Mapping(target = "fullName", source = "reader.name.name")
    @Mapping(target = "username", source = "reader.username")
    @Mapping(target = "birthDate", source = "birthDate.birthDate")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "gdprConsent", source = "gdprConsent")
    @Mapping(target = "marketingConsent", source = "marketingConsent")
    @Mapping(target = "thirdPartyConsent", source = "thirdPartySharingConsent")
    @Mapping(target = "interestList", expression = "java(mapInterestList(readerDetails.getInterestList()))")
    public abstract ReaderDTOAmqp toReaderViewAMQP(ReaderDetails readerDetails);

    public abstract List<ReaderDTOAmqp> toReaderViewAMQP(List<ReaderDetails> readerDetailsList);

    protected List<String> mapInterestList(List<Genre> interestList) {
        if (interestList == null) {
            return new ArrayList<>();
        }
        return interestList.stream()
                .map(Genre::getGenre) // Converte cada objeto Genre para o atributo String `genre`.
                .toList();
    }

}
