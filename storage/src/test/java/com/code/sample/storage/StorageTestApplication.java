package com.code.sample.storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**  테스트용 - 임시 시작 포인트용 클래스 */
@ConfigurationPropertiesScan  // 프로젝트 내의 클래스를 스캔하여 @ConfigurationProperties 애노테이션을 가진 빈들을 등록하는 데 사용
@SpringBootApplication
public class StorageTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(StorageTestApplication.class, args);
    }

}
