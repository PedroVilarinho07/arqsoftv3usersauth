package pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.readermanagement.dataModel.postgres.ReaderDataModel;
import pt.psoft.g1.psoftg1.readermanagement.dto.request.ReaderBookCountDTO;
import pt.psoft.g1.psoftg1.readermanagement.dto.request.SearchReadersQuery;
import pt.psoft.g1.psoftg1.readermanagement.mappers.ReaderDetailsSQLMapper;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;
import pt.psoft.g1.psoftg1.usermanagement.model.Role;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("sql")
@AllArgsConstructor
public class ReaderDetailsSQLRepo implements ReaderRepository {
    
    @PersistenceContext
    private EntityManager entityManager;

    private UserRepository userRepository;

    @Override
    public ReaderDetails save(ReaderDetails readerDetails) {
        Optional<User> u = userRepository.findByUsername(readerDetails.getReader().getUsername());
        if (u.isPresent()) {
            ReaderDataModel readerDataModel = ReaderDetailsSQLMapper.convertToDataModelReader(readerDetails);
            readerDataModel.setUserId(u.get().getId());
            if (readerDataModel.getPk() == null) {
                entityManager.persist(readerDataModel);
            } else {
                readerDataModel = entityManager.merge(readerDataModel);
            }
            return ReaderDetailsSQLMapper.convertToDomainReader(readerDataModel, readerDetails.getReader());
        }
        return null;
    }

    @Override
    public Optional<ReaderDetails> findByReaderNumber(String readerNumber) {
        TypedQuery<ReaderDataModel> query = entityManager.createQuery(
                "SELECT r FROM ReaderDataModel r WHERE r.readerNumber = :readerNumber", ReaderDataModel.class);
        query.setParameter("readerNumber", readerNumber);

        Optional<ReaderDataModel> readerDataModelOpt = query.getResultStream().findFirst();

        return convertToDomainReader(readerDataModelOpt);
    }

    private Optional<ReaderDetails> convertToDomainReader(Optional<ReaderDataModel> readerDataModelOpt) {
        return readerDataModelOpt.flatMap(readerDataModel -> {
            Optional<User> readerOpt = userRepository.findById(readerDataModel.getUserId());

            if (readerOpt.isPresent()) {
                Reader r = new Reader(readerOpt.get().getUsername(), readerOpt.get().getPassword());
                r.addAuthority(new Role(Role.READER));

                return Optional.of(ReaderDetailsSQLMapper.convertToDomainReader(readerDataModel, r));
            } else {
                return Optional.empty();
            }
        });
    }


    @Override
    public List<ReaderDetails> findByPhoneNumber(String phoneNumber) {
        /*
        TypedQuery<ReaderDataModel> query = entityManager.createQuery(
                "SELECT r FROM ReaderDataModel r WHERE r.phoneNumber = :phoneNumber", ReaderDataModel.class);
        query.setParameter("phoneNumber", phoneNumber);
        return convertToDomainReader(readerDataModelOpt);
        return query.getResultStream().map(ReaderDetailsSQLMapper::convertToDomainReader).collect(Collectors.toList());
    */
        return null;
    }


    @Override
    public Optional<ReaderDetails> findByUsername(String username) {
        /*
        TypedQuery<ReaderDataModel> query = entityManager.createQuery(
                "SELECT r FROM ReaderDataModel r JOIN r.reader u WHERE u.username = :username", ReaderDataModel.class);
        query.setParameter("username", username);
        return query.getResultStream().findFirst().map(ReaderSQLMapper::convertToDomainReader);

         */
        return null;
    }

    @Override
    public Optional<ReaderDetails> findByUserId(Long userId) {
        /*
        TypedQuery<ReaderDataModel> query = entityManager.createQuery(
                "SELECT r FROM ReaderDataModel r JOIN r.reader u WHERE u.id = :userId", ReaderDataModel.class);
        query.setParameter("userId", userId);
        return query.getResultStream().findFirst().map(ReaderSQLMapper::convertToDomainReader);

         */
        return null;
    }

    @Override
    public int getCountFromCurrentYear() {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(r) FROM ReaderDataModel r WHERE YEAR(r.birthDate) = :currentYear", Long.class);
        query.setParameter("currentYear", LocalDate.now().getYear());
        return query.getSingleResult().intValue();
    }

    @Override
    public Iterable<ReaderDetails> findAll() {
        /*
        TypedQuery<ReaderDataModel> query = entityManager.createQuery("SELECT r FROM ReaderDataModel r", ReaderDataModel.class);
        return query.getResultStream().map(ReaderDetailsSQLMapper::convertToDomainReader).collect(Collectors.toList());

         */
        return null;
    }

    @Override
    public Page<ReaderDetails> findTopReaders(Pageable pageable) {
        /*
        TypedQuery<ReaderDataModel> query = entityManager.createQuery("SELECT r FROM ReaderDataModel r ORDER BY r.someMetric DESC", ReaderDataModel.class);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<ReaderDetails> readers = query.getResultStream().map(ReaderSQLMapper::convertToDomainReader).collect(Collectors.toList());
        long total = entityManager.createQuery("SELECT COUNT(r) FROM ReaderDataModel r", Long.class).getSingleResult();
        return new PageImpl<>(readers, pageable, total);

         */
        return null;
    }

    @Override
    public Page<ReaderBookCountDTO> findTopByGenre(Pageable pageable, String genre, LocalDate startDate, LocalDate endDate) {
        /*
        TypedQuery<ReaderBookCountDTO> query = entityManager.createQuery(

                "SELECT new pt.psoft.g1.psoftg1.readermanagement.dto.ReaderBookCountDTO(r.readerNumber, COUNT(b)) " +
                        "FROM ReaderDataModel r JOIN r.borrowedBooks b WHERE b.genre = :genre " +
                        "AND b.borrowDate BETWEEN :startDate AND :endDate GROUP BY r.readerNumber ORDER BY COUNT(b) DESC",
                ReaderBookCountDTO.class);
        query.setParameter("genre", genre);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<ReaderBookCountDTO> results = query.getResultList();
        long total = entityManager.createQuery(
                        "SELECT COUNT(DISTINCT r.readerNumber) FROM ReaderDataModel r JOIN r.borrowedBooks b " +
                                "WHERE b.genre = :genre AND b.borrowDate BETWEEN :startDate AND :endDate", Long.class)
                .setParameter("genre", genre)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getSingleResult();
        return new PageImpl<>(results, pageable, total);

         */
        return null;
    }

    @Override
    public void delete(ReaderDetails readerDetails) {
        ReaderDataModel readerDataModel = entityManager.find(ReaderDataModel.class, readerDetails.getReaderNumber());
        if (readerDataModel != null) {
            entityManager.remove(readerDataModel);
        }
    }

    @Override
    public List<ReaderDetails> searchReaderDetails(pt.psoft.g1.psoftg1.shared.services.Page page, SearchReadersQuery query) {
        /*
        // Build dynamic query based on search criteria
        TypedQuery<ReaderDataModel> query = buildDynamicQuery(query);
        query.setFirstResult(page.getOffset());
        query.setMaxResults(page.getSize());
        return query.getResultStream().map(ReaderSQLMapper::convertToDomainReader).collect(Collectors.toList());

         */
        return null;
    }

}
