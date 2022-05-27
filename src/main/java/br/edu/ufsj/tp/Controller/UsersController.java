package br.edu.ufsj.tp.Controller;

import br.edu.ufsj.tp.Helper.JwtTokenHelper;
import br.edu.ufsj.tp.Model.Specialty;
import br.edu.ufsj.tp.Model.User;
import br.edu.ufsj.tp.Repository.SpecialtyRepository;
import br.edu.ufsj.tp.Repository.UsersRepository;
import br.edu.ufsj.tp.Utils.SearchDoctors;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("users")
@Data
@Slf4j
public class UsersController implements UserDetailsService {
    @Autowired
    private UsersRepository repository;
    @Autowired
    private SpecialtyRepository specialtyRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> opUser = repository.findByEmail(email);

        if(opUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        User user = opUser.get();

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        if (user.getCrm() != null) {
            authorities.add(new SimpleGrantedAuthority("doctor"));
        } else {
            authorities.add(new SimpleGrantedAuthority("patient"));
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable String id) {
        Optional<User> user = this.repository.findById(id);

        if(user.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(user.get());
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PostMapping("")
    public ResponseEntity<User> create(@RequestBody User user) {
        log.info(user.toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(new Date(System.currentTimeMillis()));
        user.setUpdatedAt(new Date(System.currentTimeMillis()));
        return ResponseEntity.status(HttpStatus.CREATED).body(
                this.repository.save(user)
        );
    }

    @GetMapping("doctors")
    public ResponseEntity<Page<User>> listDoctors(
            @RequestParam(value = "specialty", required = false, defaultValue = "0") String specialty,
            @RequestParam(value = "name", required = false, defaultValue = "0") String name,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "20") int size
    ) {
        Page<User> users;

        if(!name.equals("0") && !specialty.equals("0")) {
            users = repository.findAllByNameContainingIgnoreCaseAndSpecialtyContainingIgnoreCaseAndCrmIsNotNull(
                    name,
                    specialty,
                    PageRequest.of(page, size)
            );
        }
        else if(!name.equals("0")) {
            users = repository.findAllByNameContainingIgnoreCaseAndCrmIsNotNull(
                    name,
                    PageRequest.of(page, size)
            );
        }
        else if(!specialty.equals("0")) {
            users = repository.findAllBySpecialtyContainingIgnoreCaseAndCrmIsNotNull(
                    specialty,
                    PageRequest.of(page, size)
            );
        } else {
            users = repository.findAllByCrmIsNotNull(PageRequest.of(page, size));
        }

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

                Optional<User> opUser = repository.findByEmail(decodedJWT.getSubject());

                if(opUser.isEmpty()) {
                    throw new Error();
                }

                User user = opUser.get();
                List<String> authorities = new ArrayList<>();
                authorities.add(!user.getCrm().equals("") ? "doctor" : "patient");

                Map<String, String> tokens = JwtTokenHelper.signTokens(
                        user.getId(),
                        request.getRequestURL().toString(),
                        authorities
                );

                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception e) {
                Map<String, String> error = new HashMap<>();
                error.put("error", e.getMessage());

                response.setContentType(APPLICATION_JSON_VALUE);
                response.setStatus(UNAUTHORIZED.value());
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

    @GetMapping("doctors/specialties")
    public ResponseEntity<List<Specialty>> showSpecialties() {
        List<Specialty> specialties = specialtyRepository.findAll();

        if(specialties.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(specialties);
    }
}
