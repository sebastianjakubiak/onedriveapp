package com.onedrive.app;

import com.microsoft.graph.requests.GraphServiceClient;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class OnedriveAppService {
  private GraphInitializer graphInitializer;
  private GraphServiceClient graphClient;

  public OnedriveAppService() {}

  public OnedriveAppService(GraphInitializer graphInitializer) {
    this.graphInitializer = graphInitializer;
  }

  public List<String> listFiles(String accessCode) {
    if (graphClient == null) {
      graphClient = graphInitializer.setupGraph(accessCode);
    }

    var drive = graphClient.me().drive().root().children().buildRequest().get();
    var items = new ArrayList<String>();
    for (var file : drive.getCurrentPage()) {
      items.add(file.name);
    }
    return items;
  }

  public List<String> findFilesByName(String name, String accessCode) {
    var listFiles = listFiles(accessCode);
    return listFiles.stream().filter(f -> f.contains(name)).collect(Collectors.toList());
  }
}
