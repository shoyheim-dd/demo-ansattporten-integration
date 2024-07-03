package no.idporten.ansattporten_integration.config;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class OAuth2SecurityConfig {
    
	@Autowired
	private ClientRegistrationRepository clientRegistrationRepository;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        try {
            http
                .authorizeHttpRequests(exchanges ->
                    exchanges
                        .requestMatchers("/", "/error", "/logout/callback").permitAll()
                        .anyRequest().authenticated()
                    )
                    .oauth2Login(request -> request.authorizationEndpoint(authorization -> authorization.authorizationRequestResolver(authorizationRequestResolver(clientRegistrationRepository))))
            		.oidcLogout(logout -> logout
            			.backChannel(Customizer.withDefaults()))
                    .logout(logout -> logout
                        .logoutSuccessHandler(oidcLogoutSuccessHandler()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return http.build();
    }    

	private OAuth2AuthorizationRequestResolver authorizationRequestResolver(
			ClientRegistrationRepository clientRegistrationRepository) {

        DefaultOAuth2AuthorizationRequestResolver authorizationRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization/");
		authorizationRequestResolver.setAuthorizationRequestCustomizer(authorizationRequestCustomizer());
		return  authorizationRequestResolver;
	}

	private Consumer<OAuth2AuthorizationRequest.Builder> authorizationRequestCustomizer() {
		return customizer -> customizer
					.additionalParameters(params -> 
                    params.put( "authorization_details", 
                            "[{\"type\":\"ansattporten:altinn:service\",\"resource\": \"urn:altinn:resource:2480:40\", \"allow_multiple_organizations\": \"true\"}]"));
	}

	private LogoutSuccessHandler oidcLogoutSuccessHandler() {
		OidcClientInitiatedLogoutSuccessHandler  oidcLogoutSuccessHandler =
				new OidcClientInitiatedLogoutSuccessHandler (this.clientRegistrationRepository);

		// Sets the location that the End-User's User Agent will be redirected to
		// after the logout has been performed at the Provider
		oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}/logout/callback");

		return oidcLogoutSuccessHandler;
	}
}
