package com.denistiago.controller;

import com.denistiago.domain.BookWatermark;
import com.denistiago.domain.JournalWatermark;
import com.denistiago.service.WatermarkService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(WatermarkController.class)
public class WatermarkControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private WatermarkService service;

    @Test
    public void shouldAcceptValidBookWatermark() throws Exception {
        this.mvc.perform(post("/documents/1/watermark")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"content\":\"book\", \"title\":\"The Dark Code\",\"author\":\"Bruce Wayne\",\"topic\":\"Science\"}"))
                .andExpect(status().isCreated());
        verify(service, times(1)).create(anyString(), any(BookWatermark.class));
    }

    @Test
    public void shouldAcceptValidJournalWatermark() throws Exception {
        this.mvc.perform(post("/documents/1/watermark")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"content\":\"journal\", \"title\":\"The Dark Code\",\"author\":\"Bruce Wayne\"}"))
                .andExpect(status().isCreated());
        verify(service, times(1)).create(anyString(), any(JournalWatermark.class));
    }

    @Test
    public void shouldNotAcceptBookWithoutTopic() throws Exception {
        this.mvc.perform(post("/documents/1/watermark")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"content\":\"book\", \"title\":\"The Dark Code\",\"author\":\"Bruce Wayne\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBookWatermark() throws Exception {
        when(service.get(anyString())).thenReturn(new BookWatermark("title", "author", "topic"));
        this.mvc.perform(get("/watermarks/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.author").value("author"))
                .andExpect(jsonPath("$.topic").value("topic"))
                .andExpect(jsonPath("$.content").value("book"));
    }

    @Test
    public void shouldReturnJournalWatermark() throws Exception {
        when(service.get(anyString())).thenReturn(new JournalWatermark("title", "author"));
        this.mvc.perform(get("/watermarks/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.author").value("author"))
                .andExpect(jsonPath("$.topic").doesNotExist())
                .andExpect(jsonPath("$.content").value("journal"));
    }

}
