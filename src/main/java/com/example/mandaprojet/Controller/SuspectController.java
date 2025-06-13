package com.example.mandaprojet.Controller;

import ch.qos.logback.core.model.*;
import com.example.mandaprojet.Repositoty.SuspectRepository;
import com.example.mandaprojet.entity.Suspect;
//import jakarta.annotation.Resource;
//import jakarta.persistence.criteria.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;






@Controller
@RequestMapping("/suspects")
public class SuspectController {



/// /////////////////////////////////////////// VERSION AVANT FACe++///////////////////////////////
//    @Autowired
//    private SuspectRepository suspectRepository;
//    @GetMapping
//    public String listerSuspects(Model model) {
//        List<Suspect> suspects = suspectRepository.findAllByOrderByStatutAscEvtDateDesc();
//        model.addAttribute("suspects", suspects);
//        return "suspects/liste";
//    }
//
//    @GetMapping("/{id}")
//    public String afficherSuspect(@PathVariable Long id, Model model) {
//        return suspectRepository.findById(id)
//                .map(suspect -> {
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//                    String formattedDate = suspect.getEvtDate() != null ? suspect.getEvtDate().format(formatter) : "";
//                    model.addAttribute("suspect", suspect);
//                    model.addAttribute("formattedDate", formattedDate);
//                    return "suspects/detail";
//                })
//                .orElse("redirect:/suspects");
//    }
//
//    @GetMapping("/add")
//    public String showAddSuspectForm(Model model) {
//        model.addAttribute("suspect", new Suspect());
//        return "suspects/add-suspect";
//    }
//
//
//    //    ANIMATION
//    @PostMapping("/{id}/analyser")
//    public String analyserSuspect(@PathVariable Long id, RedirectAttributes redirectAttributes) {
//        suspectRepository.findById(id).ifPresent(suspect -> {
//            try {
//                String relativePath = suspect.getImagePath(); // Chemin dynamique de l'image
//                File imageFile = Paths.get("src/main/resources/static", relativePath).toFile();
//
//                if (!imageFile.exists()) {
//                    throw new FileNotFoundException("Image introuvable : " + imageFile.getAbsolutePath());
//                }
//
//                HttpHeaders headers = new HttpHeaders();
//                headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//                MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//                body.add("file", new FileSystemResource(imageFile));
//
//                HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
//                RestTemplate restTemplate = new RestTemplate();
//                ResponseEntity<Map> response = restTemplate.postForEntity(
//                        "http://localhost:8000/predict/", requestEntity, Map.class
//                );
//
//                String predictedName = (String) response.getBody().get("message");
//
//                suspect.setStatut(1);
//                suspect.setPrediction(" " + predictedName);
//                suspectRepository.save(suspect);
//
//                redirectAttributes.addFlashAttribute("successMessage", "Analyse terminée avec succès !");
//            } catch (Exception e) {
//                redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de l'analyse : " + e.getMessage());
//            }
//        });
//
//        return "redirect:/suspects/" + id;
//    }
//
//


//---------TEST COMPLIQUE--------
//    private final RestTemplate restTemplate;
//
//    @Value("${fastapi.url}")  // Exemple: http://127.0.0.1:8000/predict
//    private String fastApiUrl;
//
//    public SuspectController(SuspectRepository suspectRepository) {
//        this.suspectRepository = suspectRepository;
//        this.restTemplate = new RestTemplate();
//    }
//
//    // Analyser un suspect (appel à FastAPI)
//    @PostMapping("/analyser")
//    public String analyserSuspect(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
//        Optional<Suspect> optionalSuspect = suspectRepository.findById(id);
//        if (optionalSuspect.isPresent()) {
//            Suspect suspect = optionalSuspect.get();
//
//            String imagePath = suspect.getImagePath();
//            String prediction = appelerAPIReconnaissance(imagePath);
//
//            suspect.setPrediction(prediction);
//            suspect.setStatut(1);
//            suspectRepository.save(suspect);
//
//            redirectAttributes.addAttribute("id", suspect.getId());
//            return "redirect:/suspect/{id}"; // Redirige proprement vers la fiche du suspect
//        } else {
//            return "redirect:/suspects?error=notfound";
//        }
//    }
//
//    // Appel vers FastAPI
//    private String appelerAPIReconnaissance(String imagePath) {
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//
//            String body = "{\"image_path\": \"" + imagePath + "\"}";
//            HttpEntity<String> request = new HttpEntity<>(body, headers);
//
//            ResponseEntity<String> response = restTemplate.postForEntity(fastApiUrl, request, String.class);
//
//            if (response.getStatusCode() == HttpStatus.OK) {
//                return response.getBody(); // Contenu retourné par FastAPI (le nom par ex.)
//            } else {
//                return "Erreur API (" + response.getStatusCode() + ")";
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Erreur lors de l'appel API";
//        }
//    }
//}







    /// ////////////////////////// VERSION FACE++/////////////////////////////////////\




    private static final Logger logger = LoggerFactory.getLogger(SuspectController.class);

    @Autowired
    private SuspectRepository suspectRepository;


//    //////////////////////////////////// Sans groupage ////////////////////////////////////////
//    @GetMapping
//    public String listerSuspects(Model model) {
//        List<Suspect> suspects = suspectRepository.findAllByOrderByStatutAscEvtDateDesc();
//        model.addAttribute("suspects", suspects);
//        return "suspects/liste";
//    }

    @GetMapping
    public String listerSuspects(Model model) {
        List<Suspect> suspects = suspectRepository.findAllByOrderByStatutAscEvtDateDesc();

        // Puisque getEvtDate() retourne un LocalDate, pas besoin de .toLocalDate()
//        Map<LocalDate, List<Suspect>> groupedByDate = suspects.stream()
//                .collect(Collectors.groupingBy(Suspect::getEvtDate, TreeMap::new, Collectors.toList()));
        Map<LocalDate, List<Suspect>> groupedByDate = suspects.stream()
                .collect(Collectors.groupingBy(
                        Suspect::getEvtDate,
                        () -> new TreeMap<>(Comparator.reverseOrder()), // trie décroissant
                        Collectors.toList()
                ));

        model.addAttribute("groupedSuspects", groupedByDate);
        return "suspects/liste";
    }


    @GetMapping("/{id}")
    public String afficherSuspect(@PathVariable Long id, Model model) {
        return suspectRepository.findById(id)
                .map(suspect -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    String formattedDate = suspect.getEvtDate() != null ? suspect.getEvtDate().format(formatter) : "";
                    model.addAttribute("suspect", suspect);
                    model.addAttribute("formattedDate", formattedDate);
                    return "suspects/detail";
                })
                .orElse("redirect:/suspects");
    }

    @PostMapping("/{id}/analyser")
    public String analyserSuspect(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Suspect> optionalSuspect = suspectRepository.findById(id);
        if (optionalSuspect.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Suspect introuvable.");
            return "redirect:/suspects";
        }

        Suspect suspect = optionalSuspect.get();

        try {
            String relativePath = suspect.getImagePath();
            File imageFile = Paths.get("src/main/resources/static", relativePath).toFile();

            if (!imageFile.exists()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Image introuvable : " + imageFile.getAbsolutePath());
                return "redirect:/suspects/" + id;
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(imageFile));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<Map> response = restTemplate.postForEntity("http://localhost:8000/predict/", requestEntity, Map.class);
            Map<String, Object> responseBody = response.getBody();

            if (responseBody == null || !responseBody.containsKey("recognized")) {
                redirectAttributes.addFlashAttribute("errorMessage", "Réponse invalide de l'API.");
                return "redirect:/suspects/" + id;
            }

            boolean isRecognized = Boolean.parseBoolean(String.valueOf(responseBody.get("recognized")));
            String predictedName = isRecognized
                    ? String.valueOf(responseBody.get("user_id"))
                    : "inconnu";

            suspect.setStatut(1); // 1 = analysé
            suspect.setPrediction(predictedName);
            suspectRepository.save(suspect);

            redirectAttributes.addFlashAttribute("successMessage", "Analyse terminée avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de l'analyse : " + e.getMessage());
        }

        return "redirect:/suspects/" + id;
    }

    @PostMapping("/{id}/enregistrer")
    public String enregistrerSuspect(@PathVariable Long id, @RequestParam String nomComplet, RedirectAttributes redirectAttributes) {
        Optional<Suspect> optionalSuspect = suspectRepository.findById(id);
        if (optionalSuspect.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Suspect introuvable.");
            return "redirect:/suspects";
        }

        Suspect suspect = optionalSuspect.get();

        try {
            String relativePath = suspect.getImagePath();
            File imageFile = Paths.get("src/main/resources/static", relativePath).toFile();

            if (!imageFile.exists()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Image introuvable : " + imageFile.getAbsolutePath());
                return "redirect:/suspects/" + id;
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(imageFile));
            body.add("name", nomComplet);  // Le champ attendu par FastAPI

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<Map> response = restTemplate.postForEntity("http://localhost:8000/register/", requestEntity, Map.class);
            Map<String, Object> responseBody = response.getBody();

            if (responseBody != null && "success".equals(responseBody.get("status"))) {
                suspect.setPrediction(nomComplet);
                suspect.setStatut(2); // 2 = enregistré
                suspectRepository.save(suspect);
                redirectAttributes.addFlashAttribute("successMessage", "Suspect enregistré avec succès !");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de l'enregistrement.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur technique : " + e.getMessage());
        }

        return "redirect:/suspects/" + id;
    }



/// /////////////////////////////////////////// pour plusieurs visages//////////////////////////////////////


@PostMapping("/{id}/analyse-complete")
public String analyseComplete(@PathVariable("id") Long id,
                              RedirectAttributes redirectAttributes) throws IOException {
    Suspect suspect = suspectRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid suspect Id:" + id));

    // 1. Charger l'image d'origine
    Path imagePath = Paths.get(suspect.getImagePath());
    File imageFile = imagePath.toFile();

    // 2. Appeler /detect_and_crop_faces/
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("file", new FileSystemResource(imageFile));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
    RestTemplate restTemplate = new RestTemplate();

    ResponseEntity<Map> response = restTemplate.postForEntity("http://127.0.0.1:8000/detect_and_crop_faces/", requestEntity, Map.class);
    Map<String, Object> responseBody = response.getBody();

    if (responseBody == null || !responseBody.containsKey("faces")) {
        redirectAttributes.addFlashAttribute("errorMessage", "Aucun visage détecté.");
        return "redirect:/suspects/" + id;
    }

    // 3. Extraire les visages et appeler /predict/ pour chacun
    List<Map<String, Object>> faces = (List<Map<String, Object>>) responseBody.get("faces");
    StringBuilder result = new StringBuilder("Prédictions : ");

    for (Map<String, Object> face : faces) {
        String imageUrl = (String) face.get("image_url");
        String croppedImagePath = "cropped_faces/" + Paths.get(imageUrl).getFileName(); // e.g., face_0_filename.jpg

        // Appeler /predict/ sur ce fichier
        MultiValueMap<String, Object> predictBody = new LinkedMultiValueMap<>();
        predictBody.add("file", new FileSystemResource(new File("C:/Users/EliteBook/Desktop/Face++Api/cropped_faces/" + croppedImagePath)));

        HttpEntity<MultiValueMap<String, Object>> predictRequest = new HttpEntity<>(predictBody, headers);
        ResponseEntity<Map> predictResponse = restTemplate.postForEntity("http://127.0.0.1:8000/predict/", predictRequest, Map.class);
        Map<String, Object> predictBodyMap = predictResponse.getBody();

        String prediction = (String) predictBodyMap.get("prediction");
        result.append("[Face: ").append(croppedImagePath).append(" → ").append(prediction).append("] ");
    }

    // 4. Sauvegarder la prédiction dans la base
    suspect.setPrediction(result.toString());
    suspectRepository.save(suspect);

    return "redirect:/suspects";
}

    @GetMapping("/api/suspects/{id}/image")
    public ResponseEntity<Resource> getImage(@PathVariable Long id) throws MalformedURLException {
        Suspect suspect = suspectRepository.findById(id).orElseThrow();
        Path path = Paths.get("src/main/resources/static").resolve(suspect.getImagePath()); // Assurez-vous que le chemin est correct
        Resource resource = new UrlResource(path.toUri());

        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok().body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}



