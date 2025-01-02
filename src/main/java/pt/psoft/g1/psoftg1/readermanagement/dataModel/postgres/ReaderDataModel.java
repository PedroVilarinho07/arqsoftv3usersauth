package pt.psoft.g1.psoftg1.readermanagement.dataModel.postgres;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "READER_DETAILS")
public class ReaderDataModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE) // Use SEQUENCE for PostgreSQL
    @SequenceGenerator(name = "reader_seq", sequenceName = "reader_seq", allocationSize = 1)
    @Column(name = "PK", updatable = false, nullable = false)
    private Long pk;

    @Column(name = "READER_ID", unique = true, nullable = false)
    private Long userId;

    @Column(name = "READER_NUMBER", nullable = false, length = 32, unique = true)
    @NotNull
    private String readerNumber;

    @Column(name = "READER_NAME", nullable = false, length = 120, unique = true)
    @NotNull
    private String readerName;

    @Column(name = "BIRTH_DATE", nullable = false)
    private LocalDate birthDate;

    @Column(name = "PHONE_NUMBER", length = 15)
    private String phoneNumber;

    @Column(name = "PHOTO_URI", length = 1024)
    private String photoURI;

    @Column(name = "GDPR_CONSENT", nullable = false)
    private boolean gdprConsent;

    @Column(name = "MARKETING_CONSENT")
    private boolean marketingConsent;

    @Column(name = "THIRD_PARTY_SHARING_CONSENT")
    private boolean thirdPartySharingConsent;

    @ElementCollection
    @CollectionTable(name = "READER_INTERESTS", joinColumns = @JoinColumn(name = "PK", referencedColumnName = "PK"))
    @Column(name = "INTEREST_GENRE")
    private List<String> interestList;

    public ReaderDataModel() {}

    public ReaderDataModel(String readerName, String readerNumber, LocalDate birthDate, String phoneNumber, boolean gdprConsent, boolean marketingConsent, boolean thirdPartySharingConsent, String photoURI, List<String> interestList) {
        this.readerName = readerName;
        this.readerNumber = readerNumber;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.gdprConsent = gdprConsent;
        this.marketingConsent = marketingConsent;
        this.thirdPartySharingConsent = thirdPartySharingConsent;
        this.photoURI = photoURI;
        this.interestList = interestList;
    }
}
