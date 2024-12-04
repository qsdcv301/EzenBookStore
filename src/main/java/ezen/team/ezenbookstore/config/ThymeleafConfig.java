package ezen.team.ezenbookstore.config;

import ezen.team.ezenbookstore.service.FileUploadService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ThymeleafConfig {

    private final FileUploadService fileUploadService;

    public ThymeleafConfig(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @Bean
    public FileUploadService thymeleafFileUploadService() {
        return fileUploadService;
    }
}
