package com.example.mandaprojet.Controller;

import com.example.mandaprojet.Repositoty.BaseImagesRepository;
import com.example.mandaprojet.entity.BaseImages;
import com.example.mandaprojet.Service.EmbeddingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/baseimage")
public class BaseImagesController {

    @Autowired
    private BaseImagesRepository baseImageRepository;

    @Autowired
    private EmbeddingService embeddingService;

    @PostMapping("/upload")
    public String uploadAndProcess(
            @RequestParam("image") MultipartFile image,
            @RequestParam("name") String name,
            @RequestParam("firstname") String firstname,
            @RequestParam("docType") String docType,
            @RequestParam("docId") String docId
    ) {
        try {
            String id = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            // Enregistrer l’image sur disque
            String savePath = "images/base/" + id + ".jpg";
            File dest = new File(savePath);
            dest.getParentFile().mkdirs();
            image.transferTo(dest);

            // Appel à l’API Python
            String embeddingPath = embeddingService.sendImageForEmbedding(image);

            // Enregistrement dans la BDD
            BaseImages baseImage = new BaseImages();
            baseImage.setId(id);
            baseImage.setNom(name);
            baseImage.setPath(savePath);
            baseImage.setEmbedding(embeddingPath);

            baseImageRepository.save(baseImage);

            return "redirect:/dashboard";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}
