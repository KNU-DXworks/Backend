package project.DxWorks.GeminiAI.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.DxWorks.GeminiAI.entity.Inbody;
import project.DxWorks.GeminiAI.service.InbodyService;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ScanController {

    private final InbodyService inbodyService;

    @PostMapping("/upload")
    public ResponseEntity<Inbody> uploadInbodyImage(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("파일 이름: " + file.getOriginalFilename());
            Inbody saved = inbodyService.analyzeAndSave(file);
            return ResponseEntity.ok(saved);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/getupload")
    public ResponseEntity<List<Inbody>> getAll() {
        return ResponseEntity.ok(inbodyService.getAll());
    }
}
