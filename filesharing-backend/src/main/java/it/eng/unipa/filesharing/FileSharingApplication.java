package it.eng.unipa.filesharing;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@Configuration
public class FileSharingApplication{

	public static void main(String[] args) {
		// Add BouncyCastle as an algorithm provider
		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastleProvider());
		}


		SpringApplication.run(FileSharingApplication.class, args);
	}

	/*@Bean
	public KeycloakConfigResolver KeycloakConfigResolver() {
	    return new KeycloakSpringBootConfigResolver();
	}*/

	@Bean
	public KeycloakSpringBootConfigResolver KeycloakSpringBootConfigResolver() {
		return new KeycloakSpringBootConfigResolver();
	}

}

@Configuration
@EnableSwagger2
class SwaggerConfig {

	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String DEFAULT_INCLUDE_PATTERN = "/.*";


	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.securityContexts(securityContext())
				.securitySchemes(apiKey())
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())

				.build();
	}
	private List<ApiKey> apiKey() {
		List<ApiKey> l = new ArrayList<>();
		l.add(new ApiKey("JWT", AUTHORIZATION_HEADER, "header"));
		return l;
	}

	private List<SecurityContext> securityContext() {
		List<SecurityContext> l = new ArrayList<>();
		l.add(SecurityContext.builder()
				.securityReferences(defaultAuth())
				.forPaths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN))
				.build());
		return l;
	}

	List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope
				= new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		List<SecurityReference> list = new ArrayList<>();
		list.add(new SecurityReference("JWT", authorizationScopes));
		return list;
	}

}
