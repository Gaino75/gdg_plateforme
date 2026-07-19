package com.gdg.service_paiement.service;

import java.util.List;

import com.gdg.service_paiement.dto.PaiementDTO;

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
