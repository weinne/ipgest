package br.com.ipgest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "br.com.ipgest")
public class IpgestApplication {
    public static void main(String[] args) {
        SpringApplication.run(IpgestApplication.class, args);
    }
}
