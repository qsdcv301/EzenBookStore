package ezen.team.ezenbookstore;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class EzenBookStoreApplication {

    public static void main(String[] args) {
        // .env 파일 로드
        Dotenv dotenv = Dotenv.configure()
                .directory("./")
                .load();

        // 시스템 프로퍼티로 설정
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        System.out.println("Current TimeZone: " + TimeZone.getDefault());
        SpringApplication.run(EzenBookStoreApplication.class, args);
    }

}
