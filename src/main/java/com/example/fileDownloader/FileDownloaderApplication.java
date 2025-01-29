package com.example.fileDownloader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class FileDownloaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileDownloaderApplication.class, args);
		FileController server = new FileController();

	}

}

