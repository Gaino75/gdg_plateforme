package com.gdg.admin.service;

import com.gdg.admin.model.StatistiqueJournaliere;
import com.gdg.admin.repository.StatistiqueJournaliereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class StatistiqueService {

    @Autowired
    private StatistiqueJournaliereRepository statistiqueRepository;

    public List<StatistiqueJournaliere> getStatsGlobales() {
        return statistiqueRepository.findByAgenceIdIsNull();
    }

    public List<StatistiqueJournaliere> getStatsByAgence(
            Long agenceId) {
        return statistiqueRepository.findByAgenceId(agenceId);
    }

    public List<StatistiqueJournaliere> getStatsByPeriode(
            LocalDate debut, LocalDate fin) {
        return statistiqueRepository
            .findByDateStatBetween(debut, fin);
    }

    public Double getMontantTotalGlobal() {
        return statistiqueRepository.getTotalMontantGlobal();
    }
}
