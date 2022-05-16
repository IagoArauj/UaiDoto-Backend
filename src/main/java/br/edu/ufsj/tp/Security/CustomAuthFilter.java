package br.edu.ufsj.tp.Security;


/*
@Slf4j
@RequiredArgsConstructor
public class CustomAuthFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            if (!request.getContentType().equals(APPLICATION_JSON_VALUE)) {
                response.sendError(400, "Bad Request");
                return null;
            }

            Map <?, ?> requestBody;
            requestBody = new ObjectMapper().readValue(
                    StreamUtils.copyToByteArray(request.getInputStream()),
                    Map.class
            );

            Object username = requestBody.get("email");
            Object password = requestBody.get("password");
            log.info("Username {}:{} tried to login", username, password);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException {
        User user = (User) authResult.getPrincipal();

        Map<String, String> tokens = JwtTokenHelper.signTokens(
                user.getUsername(),
                request.getRequestURL().toString(),
                user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
        );

        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);

    }
}*/
