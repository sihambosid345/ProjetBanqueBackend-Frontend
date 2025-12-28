package app.siham.banque.web;

import app.siham.banque.dtos.LoginRequest;
import app.siham.banque.dtos.LoginResponse;
import app.siham.banque.entity.User;
import app.siham.banque.repository.UserRepository;
import app.siham.banque.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        // 🔍 Vérification email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email ou mot de passe incorrect"));

        // 🔐 Vérification mot de passe
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }

        // 🔑 Génération du JWT (email + roles فقط)
        String token = jwtUtil.generateToken(
                user.getEmail(),
                new ArrayList<>(user.getRoles())
        );

        // 📦 Réponse
        LoginResponse response = new LoginResponse(
                token,
                user.getEmail(),
                user.getNom(),
                new ArrayList<>(user.getRoles())
        );

        return ResponseEntity.ok(response);
    }
}
