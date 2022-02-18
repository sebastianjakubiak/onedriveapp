package com.onedrive.app;

import static org.assertj.core.util.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest
public class OnedriveAppControllerTest {
  @Autowired private MockMvc mockMvc;

  @MockBean private OnedriveAppService onedriveAppService;

  @Test
  public void providesRestEndpointAndReturnsFilesBasedOnServiceLayerAllFiles() throws Exception {
    String name = "sampleFileName";
    given(onedriveAppService.listFiles("accessCode")).willReturn(newArrayList(name));
    this.mockMvc
        .perform(get("/getFiles?code=accessCode"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(name)));
  }

  @Test
  public void providesRestEndpointAndReturnsFilesBasedOnServiceLayerGivenKeyword()
      throws Exception {
    String name = "sampleFileName";
    given(onedriveAppService.findFilesByName("sample", "accessCode"))
        .willReturn(newArrayList(name));
    this.mockMvc
        .perform(get("/getFiles?name=sample&code=accessCode"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(name)));
  }
}
