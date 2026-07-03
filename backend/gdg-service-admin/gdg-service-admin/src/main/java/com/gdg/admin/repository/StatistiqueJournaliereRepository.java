package com.gdg.admin.repository;

import com.gdg.admin.model.StatistiqueJournaliere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StatistiqueJournaliereRepository
        extends JpaRepository<StatistiqueJournaliere, Long> {

    List<StatistiqueJournaliere> findByAgenceId(Long agenceId);
    List<StatistiqueJournaliere> findByAgenceIdIsNull();
    List<StatistiqueJournaliere> findByDateStatBetween(
        LocalDate debut, LocalDate fin);
    Optional<StatistiqueJournaliere> findByDateStatAndAgenceId(
        LocalDate dateStat, Long agenceId);

    @Query("SELECT SUM(s.nbVentes) FROM StatistiqueJournaliere s " +
           "WHERE s.dateStat = :date")
    Integer getTotalVentesParDate(LocalDate date);

    @Query("SELECT SUM(s.montantTotalVentes) " +
           "FROM StatistiqueJournaliere s")
    Double getTotalMontantGlobal();
}
