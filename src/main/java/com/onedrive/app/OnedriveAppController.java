package com.onedrive.app;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class OnedriveAppController {

  private final OnedriveAppService service;

  @Autowired
  private OnedriveAppController(OnedriveAppService service) {
    this.service = service;
  }

  @Operation(summary = "Basic MS authentication producing access code used for another requests")
  @GetMapping(value = "/authorize")
  public ResponseEntity<Object> authorize() throws URISyntaxException {
    URI authorizeURL =
        new URI(
            "https://login.microsoftonline.com/common/oauth2/v2.0/authorize"
                + "?client_id=c4efbbca-0eac-4639-9291-3ad44804c1a7"
                + "&scope=files.read"
                + "&response_type=code"
                + "&redirect_uri=http://localhost:8080");
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setLocation(authorizeURL);
    return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
  }

  @Operation(summary = "Hidden root endpoint redirecting to authorize page", hidden = true)
  @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
  public Object accessCode(
      @RequestParam(name = "code", required = false) String code, ModelMap model) {
    if (code == null) {
      return new ModelAndView("redirect:/authorize", model);
    }
    return Collections.singletonMap("access_code", code);
  }

  @Operation(
      summary =
          "Get all files from user's OneDrive root directory. If additional name param is provided, return files containing the criteria")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Items found"),
        @ApiResponse(responseCode = "500", description = "Invalid access code", content = @Content),
        @ApiResponse(responseCode = "404", description = "Item not found", content = @Content)
      })
  @GetMapping(value = "/getFiles")
  @ResponseBody
  public List<String> getRootDirectory(
      @RequestParam(name = "name", required = false) String filename,
      @RequestParam(name = "access_code") String code) {
    if (filename == null) {
      return service.listFiles(code);
    } else {
      var list = service.findFilesByName(filename, code);
      if (list.isEmpty()) {
        throw new NotFoundException();
      } else {
        return list;
      }
    }
  }
}

@ResponseStatus(
    value = HttpStatus.NOT_FOUND,
    reason = "Files matching the criteria not found in root directory!")
class NotFoundException extends RuntimeException {}
