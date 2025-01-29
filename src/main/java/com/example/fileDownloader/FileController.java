package com.example.fileDownloader;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;


@Controller
public class FileController {

    private final Path rootLocation = Paths.get("uploads");

    public FileController() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage location", e);
        }
    }


    @GetMapping("/")
    public String home() {
        return "index";
    }


    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file,
                                                   @RequestParam("fileId") String fileId,@RequestParam("userId")String userId) {
        try {
            // Validate fileId is not empty
            if (fileId.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("File ID is required");
            }

            // Create directory for this fileId (in this case we'll use a single file per ID)
            Path fileIdPath = rootLocation.resolve(fileId);

            // Delete existing directory if it exists
            if (Files.exists(fileIdPath)) {
                Files.walk(fileIdPath) //Walks through folder path to find every file
                        .sorted(Comparator.reverseOrder())//Reverse so we can delete files before folder
                        .map(Path::toFile)
                        .forEach(File::delete);
            }

            Files.createDirectories(fileIdPath); //Create directory

            // Store the file
            Path destinationFile = fileIdPath.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), destinationFile);

            // Store user info
            String todayDate = LocalDate.now().toString();
            String userEntry = (userId == null || userId.trim().isEmpty() ? "NULL" : userId) +
                    ",\"" + fileId + "\",\"" + todayDate + "\"\n";



            // Append to user_records.txt
            Path recordsFile = rootLocation.resolve("user_records.txt");
            Files.write(recordsFile, userEntry.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);

            return ResponseEntity.ok("File uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Failed to upload file: " + e.getMessage());
        }
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("fileId") String fileId) {
        try {
            Path fileIdPath = rootLocation.resolve(fileId);

            if (!Files.exists(fileIdPath)) {
                return ResponseEntity.notFound().build();
            }

            // Get the first file in the directory
            Path file = Files.list(fileIdPath).findFirst()
                    .orElseThrow(() -> new RuntimeException("No file found"));

            Resource resource = new UrlResource(file.toUri());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }



        @GetMapping("/userFiles")
        @ResponseBody
        public ResponseEntity<String> getUserFiles(@RequestParam("userId") String userId) {
            try {
                Path recordsFile = rootLocation.resolve("user_records.txt");
                if (!Files.exists(recordsFile)) {
                    return ResponseEntity.ok("No records found.");
                }

                // Read all lines and filter by userId
                List<String> matchingFiles = Files.lines(recordsFile)
                        .filter(line -> line.startsWith(userId + ",") || line.startsWith("NULL,"))
                        .toList();

                if (matchingFiles.isEmpty()) {
                    return ResponseEntity.ok("No files found for this user.");
                }

                return ResponseEntity.ok(String.join("\n", matchingFiles));
            } catch (IOException e) {
                return ResponseEntity.internalServerError().body("Error reading records: " + e.getMessage());
            }
        }
    }




