package com.onedrive.app;

import com.azure.identity.AuthorizationCodeCredentialBuilder;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.requests.GraphServiceClient;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphInitializer {
  private static final Logger logger = LoggerFactory.getLogger(GraphInitializer.class);

  private GraphInitializer() {}

  public static GraphServiceClient<Request> setupGraph(String accessCode) {
    var properties = new Properties();
    try {
      properties.load(
          OnedriveAppService.class.getClassLoader().getResourceAsStream("application.properties"));
    } catch (IOException e) {
      logger.error("Cannot read properties file! Aborting");
      return null;
    }

    var scopes = Arrays.asList(properties.getProperty("scopes").split(","));

    var authCodeCredential =
        new AuthorizationCodeCredentialBuilder()
            .clientId(properties.getProperty("appId"))
            .tenantId("common")
            .authorizationCode(accessCode)
            .redirectUrl(properties.getProperty("redirectUri"))
            .build();

    var authProvider = new TokenCredentialAuthProvider(scopes, authCodeCredential);

    return GraphServiceClient.builder().authenticationProvider(authProvider).buildClient();
  }
}
