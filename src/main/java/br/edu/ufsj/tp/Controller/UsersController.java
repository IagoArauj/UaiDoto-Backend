package br.edu.ufsj.tp.Controller;

import br.edu.ufsj.tp.Helpers.JwtTokenHelper;
import br.edu.ufsj.tp.Model.Users;
import br.edu.ufsj.tp.Repository.UsersRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("users")
@Data
@Slf4j
public class UsersController implements UserDetailsService {
    @Autowired
    private UsersRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Users> opUser = repository.findByEmail(email);

        if(opUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        Users user = opUser.get();

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        if (user.getCrm() != 0) {
            authorities.add(new SimpleGrantedAuthority("doctor"));
        } else {
            authorities.add(new SimpleGrantedAuthority("patient"));
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> findById(@PathVariable String id) {
        Optional<Users> user = this.repository.findById(id);

        if(user.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(user.get());
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PostMapping("")
    public ResponseEntity<Users> create(@RequestBody Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(new Date(System.currentTimeMillis()));
        user.setUpdatedAt(new Date(System.currentTimeMillis()));
        return ResponseEntity.status(HttpStatus.CREATED).body(
                this.repository.save(user)
        );
    }

    @GetMapping("list/doctors")
    public ResponseEntity<List<Users>> listDoctors() {
        List<Users> users = repository.findAllByCrmIsNot(0);

        if(!users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(users);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping("refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                DecodedJWT decodedJWT = JwtTokenHelper.verify(refreshToken);

                Optional<Users> opUser = repository.findByEmail(decodedJWT.getSubject());

                if(opUser.isEmpty()) {
                    throw new Error();
                }

                Users user = opUser.get();
                List<String> authorities = new ArrayList<>();
                authorities.add(user.getCrm() == 0 ? "doctor" : "patient");

                Map<String, String> tokens = JwtTokenHelper.signTokens(
                        user.getEmail(),
                        request.getRequestURL().toString(),
                        authorities
                );

                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception e) {
                Map<String, String> error = new HashMap<>();
                error.put("error", e.getMessage());

                response.setContentType(APPLICATION_JSON_VALUE);
                response.setStatus(FORBIDDEN.value());
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}
