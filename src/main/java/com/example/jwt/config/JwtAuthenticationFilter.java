package com.example.jwt.config;

import com.example.jwt.config.auth.PrincipalDetails;
import com.example.jwt.entitiy.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있음.
// /login 요청해서 username, password 전송하면 (post)
// UsernamePasswordAuthenticationFilter 동작을 함.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter: 로그인 시도중");

        Authentication authentication = null;
        // 1. username, password를 받아서
        try {
//            BufferedReader br = request.getReader();
//            String input = null;
//            while ((input = br.readLine()) != null) {
//                System.out.println(input);
//            }
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);
            System.out.println(user);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getPassword());
            System.out.println(
                "username: " + user.getUsername() + " password: " + user.getPassword());
            // PrincipalDeatisService의 loadByUsername() 함수가 실행 됨
            authentication = authenticationManager.authenticate(authenticationToken);

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println(principalDetails.getUser().getUsername());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("==================================");
        // 2. 정상인지 로그인 시도를 해봄 authenticationManager로 로그인 시도를 하면 PrincipalDetailsService가 호출이 됨.
        // 3. loadUserByUsername가 자동으로 시작이 됨.
        // 4. PrincipalDetails를 세션에 담고 (권한 관리를 위해서)
        // 5. JWT 토큰을 만들어서 응답해주면 됨.

        // 리턴 할 때 authentication 객체가 세션 영역에 저장됨.
        // 리턴의 이유는 권한 관리를 security가 대신 해주기 때문에 편하려고 하는거임.
        // 굳이 JWT 토큰을 사용하면서 세션을 만들 이유가 없음. 근데 단지 권한 처리 때문에 session에 넣어 준다.
        return authentication;
    }

    // attemptAuthentication 실행 후 인증이 정상적으로 되었다면 successfulAuthentication 함수가 실행 됨
    // JWT 토큰을 만들어서 request 요청을 한 사용자에게 JWT 토큰을 response 해주면 됨.
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException, ServletException {
        System.out.println("successfulAuthentication이 실행 됨(인증이 완료되었다는 뜻임)");
        super.successfulAuthentication(request, response, chain, authResult);
    }
}
