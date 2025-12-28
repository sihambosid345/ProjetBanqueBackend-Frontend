package app.siham.banque;

import app.siham.banque.entity.User;
import app.siham.banque.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        userRepository.deleteAll();

        // ================= ADMIN =================
        User admin = new User();
        admin.setEmail("siham@gmail.com");
        admin.setPassword(passwordEncoder.encode("1234"));
        admin.setNom("Siham");
        admin.setRoles(Set.of("ROLE_ADMIN"));
        userRepository.save(admin);
        System.out.println("Admin créé : siham@gmail.com / 1234");

        // ================= USER SIMPLE =================
        User user = new User();
        user.setEmail("safaa@gmail.com");
        user.setPassword(passwordEncoder.encode("1234"));
        user.setNom("User Normal");
        user.setRoles(Set.of("ROLE_USER"));
        userRepository.save(user);
        System.out.println("User créé : safaa@gmail.com / 1234");
    }
}
