package com.example.jwt.controller;

import com.example.jwt.entitiy.User;
import com.example.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RestApiController {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/")
    public String home() {
        return "<h1>home</h1>";
    }

    @PostMapping("/")
    public String token() {
        return "<h1>token</h1>";
    }

    @PostMapping("/join")
    public String join(@RequestBody User user) {
        user.setRoles("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user); // 그냥 이대로 하면 시큐리티로 로그인 할 수 없음. 이유는 패스워드가 암호화가 안되어있기 때문.
        return "회원가입 완료";
    }
}
