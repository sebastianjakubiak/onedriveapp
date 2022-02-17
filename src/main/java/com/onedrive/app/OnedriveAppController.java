package com.onedrive.app;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OnedriveAppController {

  private final OnedriveAppService service;

  @Autowired
  private OnedriveAppController(OnedriveAppService service) {
    this.service = service;
  }

  @RequestMapping(value="/showAll",
      method = RequestMethod.GET)
  @ResponseBody
  public String getDirectory() {
//    return Arrays.asList("Something");
      return service.listFiles();
  }

}
