// From GymManagementBackend/src/main/java/com/example/GymManagementBackend/GymManagementBackendApplication.java

package com.example.GymManagementBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // <-- ADD THIS ANNOTATION
public class GymManagementBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymManagementBackendApplication.class, args);
    }

}