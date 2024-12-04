package ezen.team.ezenbookstore.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {

    @Bean
    public Dotenv dotenv() {
        Dotenv dotenv = Dotenv.configure()
                .directory("./") // .env 파일의 경로
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();

        // .env 변수들을 시스템 프로퍼티로 등록
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        return dotenv;
    }

}
