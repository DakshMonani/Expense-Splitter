package com.daksh.expense_splitter.config;

import com.daksh.expense_splitter.model.User;
import com.daksh.expense_splitter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DummyDataLoader {

    private final UserRepository userRepository;

    @Bean
    public CommandLineRunner loadData() {
        return args -> {

            // If database already has users → don’t insert dummy data again
            if (userRepository.count() == 0) {

                User u1 = User.builder()
                        .name("Daksh Monani")
                        .email("daksh@example.com")
                        .phone("9876543210")
                        .build();

                User u2 = User.builder()
                        .name("Rahul Shah")
                        .email("rahul@example.com")
                        .phone("9123456780")
                        .build();

                User u3 = User.builder()
                        .name("Amit Patel")
                        .email("amit@example.com")
                        .phone("9988776655")
                        .build();

                userRepository.save(u1);
                userRepository.save(u2);
                userRepository.save(u3);

                System.out.println("Dummy data inserted ✔");
            }
        };
    }
}
