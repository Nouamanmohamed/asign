package ma.octo.assignement.web;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ma.octo.assignement.domain.Compte;
import ma.octo.assignement.domain.Utilisateur;
import ma.octo.assignement.domain.Virement;
import ma.octo.assignement.domain.util.EventType;
import ma.octo.assignement.dto.VirementDto;
import ma.octo.assignement.exceptions.CompteNonExistantException;
import ma.octo.assignement.exceptions.SoldeDisponibleInsuffisantException;
import ma.octo.assignement.exceptions.TransactionException;
import ma.octo.assignement.repository.CompteRepository;
import ma.octo.assignement.repository.UtilisateurRepository;
import ma.octo.assignement.repository.VirementRepository;
import ma.octo.assignement.service.IAutiService;
import ma.octo.assignement.service.IServiceVirement;

@RestController
@RequestMapping("/virements")//added by me
class VirementController {



    Logger LOGGER = LoggerFactory.getLogger(VirementController.class);

    @Autowired
    private VirementRepository   virementRepository;
    @Autowired
    private IAutiService autiService;
    @Autowired
    private IServiceVirement  serviceVirement;
    @Autowired
    private CompteRepository compteRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
  

    @GetMapping("/lister_virements")
    List<Virement> loadAll() {
        List<Virement> all = virementRepository.findAll();
        
        System.out.println("appel au lister virements");
        if (CollectionUtils.isEmpty(all)) {
            return null;
        } else {
            return all;
        }
    }

    @GetMapping("/lister_comptes")
    List<Compte> loadAllCompte() {
        List<Compte> all = compteRepository.findAll();

        if (CollectionUtils.isEmpty(all)) {
            return null;
        } else {
            return all;
        }
    }

    @GetMapping("/lister_utilisateurs")
    List<Utilisateur> loadAllUtilisateur() {
        List<Utilisateur> all = utilisateurRepository.findAll();

        if (CollectionUtils.isEmpty(all)) {
            return null;
        } else {
            return all;
        }
    }

    @PostMapping("/executerVirements")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTransaction(@RequestBody VirementDto virementDto)
            throws SoldeDisponibleInsuffisantException, CompteNonExistantException, TransactionException {

    	     serviceVirement.operationVirement(virementDto);     
    	
             autiService.auditOperation("Virement depuis " + virementDto.getNrCompteEmetteur() + " vers " + virementDto
                        .getNrCompteBeneficiaire() + " d'un montant de " + virementDto.getMontantVirement()
                        .doubleValue(),EventType.VIREMENT);
    }

}
