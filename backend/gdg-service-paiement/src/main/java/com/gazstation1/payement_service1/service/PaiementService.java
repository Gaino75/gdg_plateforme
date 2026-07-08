package com.gazstation1.payement_service1.service;

import com.gazstation1.payement_service1.dto.PaiementDTO;

import java.util.List;

public interface PaiementService {

    PaiementDTO.Response creerPaiement(PaiementDTO.CreationRequest request);

    PaiementDTO.Response initierPaiement(PaiementDTO.InitierRequest request);

    PaiementDTO.Response traiterCallbackOrange(PaiementDTO.CallbackRequest request);

    PaiementDTO.Response traiterCallbackMtn(PaiementDTO.CallbackRequest request);

    PaiementDTO.Response getPaiementById(Long id);

    PaiementDTO.Response getPaiementByReference(String reference);

    List<PaiementDTO.Response> getAllPaiements();

    List<PaiementDTO.Response> getPaiementsByClient(Long clientId);

    PaiementDTO.Response validerPaiement(Long id);

    PaiementDTO.Response annulerPaiement(Long id);

    List<PaiementDTO.Response>getPaiementsAVerifier();

    PaiementDTO.Response resoudrePaiementAVerifier(Long id,boolean confirmerQuandMeme);

}
