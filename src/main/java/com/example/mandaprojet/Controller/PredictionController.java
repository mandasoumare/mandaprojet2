package com.example.mandaprojet.Controller;

import org.springframework.ui.Model;

import com.example.mandaprojet.Repositoty.SuspectRepository;
import com.example.mandaprojet.entity.Suspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Nouveau controller pour afficher les prédictions groupées
@Controller
@RequestMapping("/predictions")
public class PredictionController {

    @Autowired
    private SuspectRepository suspectRepository;

    // Page principale : regroupement des prédictions par nom prédit
    @GetMapping("/predictions")
    public String afficherGroupesDePredictions(Model model) {
        Map<String, List<Suspect>> groupedPredictions = new HashMap<>();

        List<Suspect> allSuspects = suspectRepository.findAll();
        for (Suspect suspect : allSuspects) {
            String prediction = suspect.getPrediction();
            // --- C'est ici que nous ajoutons la condition pour filtrer ---
            if (prediction != null && !prediction.trim().isEmpty() && !prediction.equals("inconnu")) {
                groupedPredictions.computeIfAbsent(prediction, k -> new ArrayList<>()).add(suspect);
            }
            // -----------------------------------------------------------
        }

        model.addAttribute("groupedPredictions", groupedPredictions);
        return "predictions/liste_groupes";
    }


//    // Détail pour un nom de prediction : afficher tous les suspects avec cette prediction
//    @GetMapping("/details/{prediction}")
//    public String afficherDetailsParPrediction(@PathVariable String prediction, Model model) {
//        List<Suspect> suspects = suspectRepository.findAll()
//                .stream()
//                .filter(s -> prediction.equals(s.getPrediction()))
//                .collect(Collectors.toList());
//
//        model.addAttribute("suspects", suspects);
//        model.addAttribute("prediction", prediction);
//        return "predictions/detail_prediction";
//    }


//    // --- Nouvelle méthode pour les détails d'une identité prédite ---
//    @GetMapping("/details/{predictedName}")
//    public String afficherDetailsPredictionsSuspect(@PathVariable("predictedName") String predictedName, Model model) {
//        // Récupérer tous les suspects qui ont cette "predictedName"
//        // Nous utilisons stream().filter() pour filtrer la liste des suspects
//        List<Suspect> predictionsForName = suspectRepository.findAll().stream()
//                .filter(suspect -> predictedName.equals(suspect.getPrediction()))
//                .collect(Collectors.toList());
//
//        model.addAttribute("predictedName", predictedName); // Pour afficher le nom en titre
//        model.addAttribute("predictions", predictionsForName); // La liste des prédictions détaillées
//
//        return "predictions/details_predictions_suspect"; // Le nom de votre nouvelle page HTML
//    }


    @GetMapping("/details/{predictedName}")
    public String afficherDetailsPredictionsSuspect(@PathVariable("predictedName") String predictedName, Model model) {
        // Récupérer tous les suspects qui ont cette "predictedName"
        List<Suspect> predictionsForName = suspectRepository.findAll().stream()
                .filter(suspect -> predictedName.equals(suspect.getPrediction()))
                .collect(Collectors.toList());

        model.addAttribute("predictedName", predictedName); // Pour afficher le nom en titre
        model.addAttribute("predictions", predictionsForName); // La liste des prédictions détaillées

        // --- Ajout des coordonnées pour la carte ---
        List<String> locations = predictionsForName.stream()
                .filter(suspect -> suspect.getLat() != null && suspect.getLng() != null)
                .map(suspect -> suspect.getLat() + "," + suspect.getLng())
                .collect(Collectors.toList());
// --- AJOUTEZ CETTE LIGNE POUR LE DÉBOGAGE ---
//     System.out.println("DEBUG - Locations for " + predictedName + ": " + locations);
//         ---------------------------------------------

        model.addAttribute("locations", locations);
        // ----------------------------------------

        return "predictions/details_predictions_suspect"; // Le nom de votre page HTML
    }
}


