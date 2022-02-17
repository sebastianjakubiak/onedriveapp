package com.onedrive.app;

import com.azure.identity.InteractiveBrowserCredentialBuilder;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.aad.msal4j.InteractiveRequestParameters;
import com.microsoft.aad.msal4j.PublicClientApplication;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.GraphServiceClient;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import okhttp3.Request;
import org.springframework.stereotype.Service;

@Service
public class OnedriveAppService {
  private static GraphServiceClient<Request> graphClient = null;
  private static TokenCredentialAuthProvider authProvider = null;

  private static final String CLIENT_ID = "c4efbbca-0eac-4639-9291-3ad44804c1a7";
  private static final String AUTHORITY = "https://login.microsoftonline.com/common/";
  private static String ACCESS_TOKEN;

  static {
    try {
      setupGraph();
    } catch (MalformedURLException | URISyntaxException e) {
      e.printStackTrace();
    }
  }

  private static void setupGraph() throws MalformedURLException, URISyntaxException {
    var properties = new Properties();
    try {
      properties.load(
          OnedriveAppService.class.getClassLoader().getResourceAsStream("application.properties"));
    } catch (IOException e) {
      return;
    }

    var scopes =
        Arrays.asList(properties.getProperty("scopes").split(",")).stream()
            .collect(Collectors.toSet());

    PublicClientApplication pca =
        PublicClientApplication.builder(CLIENT_ID).authority(AUTHORITY).build();

    InteractiveRequestParameters parameters =
        InteractiveRequestParameters.builder(new URI("http://localhost")).scopes(scopes).build();

    IAuthenticationResult result = pca.acquireToken(parameters).join();
    ACCESS_TOKEN = result.accessToken();
    System.out.println(result.accessToken());
  }

  public String listFiles() {
    return ACCESS_TOKEN;
  }

  public static void main(String[] args) {
    new OnedriveAppService();
  }
}
