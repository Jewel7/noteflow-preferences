package noteflow.preferences.controller;


import lombok.extern.slf4j.Slf4j;
import noteflow.preferences.exception.UserPrefsGlobalExceptionHandler;
import noteflow.preferences.service.UserPrefsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserPrefsController.class)
@ExtendWith(MockitoExtension.class)
@Slf4j
public class UserPrefsControllerTest {
    private static final String TEST_DN = "userKey";
    MockMvc mockMvc;
    String mockPrefs = "{\n" +
            "  \"darkMode\": \"false\",\n" +
            "  \"fontSize\": 6\n" +
            "}";

    @MockBean
    private UserPrefsService mockUserPrefsService;
    //TODO: @MockBean private SecurityContextAuthenticationFacade mockAuthFacade;

    // here, we must mock the PrefsGlobalExceptionHandler because it is a @RestControllerAdvice
    @Mock
    private UserPrefsGlobalExceptionHandler mockUserPrefsGlobalExceptionHandler;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new UserPrefsController(mockUserPrefsService))
                .setControllerAdvice(mockUserPrefsGlobalExceptionHandler)
                .build();
    }

    @Test
    public void test_getPrefs_shouldReturn200() {
//        when(mockAuthFacade.getUserSubjectDn()).thenReturn(TEST_DN);
        when(mockUserPrefsService.retrievePrefs(TEST_DN)).thenReturn(mockPrefs);
        try {
            mockMvc
                    .perform(get("/"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(print());
        } catch (Exception e) {
            log.error("Error in test_getPrefs_shouldReturn200", e);
            throw new RuntimeException("Error in test_getPrefs_shouldReturn200", e);
        }
    }

    @Test
    public void test_getPrefs_shouldReturn204() {
        //        when(mockAuthFacade.getUserSubjectDn()).thenReturn(TEST_DN);
        when(mockUserPrefsService.retrievePrefs(TEST_DN)).thenReturn("");
        try {
            mockMvc
                    .perform(get("/"))
                    .andExpect(status().isNoContent())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(print());
        } catch (Exception e) {
            log.error("Error in test_getPrefs_shouldReturn204", e);
            throw new RuntimeException("Error in test_getPrefs_shouldReturn204", e);
        }
    }

    @Test
    public void test_postPrefs_shouldReturn204() {
        //        when(mockAuthFacade.getUserSubjectDn()).thenReturn(TEST_DN);
        try {
            mockMvc
                    .perform(post("/")
                            .content(mockPrefs)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNoContent())
                    .andExpect(jsonPath("$").doesNotExist());
        } catch (Exception e) {
            throw new RuntimeException("Error in test_postPrefs_shouldReturn204", e);
        }
    }

    @Test
    public void test_getUserKey_keyIsEmpty_throwsUSerKeyNotFoundException() throws Exception {

    }


}
