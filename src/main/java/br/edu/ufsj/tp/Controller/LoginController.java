package br.edu.ufsj.tp.Controller;

import br.edu.ufsj.tp.Helpers.JwtTokenHelper;
import br.edu.ufsj.tp.Model.Login;
import br.edu.ufsj.tp.Model.Users;
import br.edu.ufsj.tp.Repository.UsersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("")
@Data
@Slf4j
public class LoginController {

    @Autowired
    UsersRepository repository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login login, HttpServletRequest request) {
        Optional<Users> user = repository.findByEmail(login.getEmail());

        if(user.isPresent() && passwordEncoder.matches(login.getPassword(), user.get().getPassword())) {
            List<String> authorities = new ArrayList<>();
            authorities.add(user.get().getCrm() == 0 ? "doctor" : "patient");

            Map<String, String> tokens = JwtTokenHelper.signTokens(
                    user.get().getEmail(),
                    request.getRequestURL().toString(),
                    authorities
            );


            return ResponseEntity.status(HttpStatus.OK).body(tokens);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

}
