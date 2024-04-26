package noteflow.preferences.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import noteflow.preferences.exception.InvalidUserPrefsException;
import noteflow.preferences.exception.UnableToReadUserPrefsException;
import noteflow.preferences.repository.S3Repository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class UserPrefsServiceTest {

    private static final String TEST_KEY = "userKey";
    private final Map<String, Object> mockMap = new HashMap<>();

    @InjectMocks
    UserPrefsService mockUserPrefsService;

    @Mock
    private ObjectMapper mockObjectMapper;

    @Mock
    private S3Repository mockS3Repository;

    @BeforeEach
    public void init() {
        log.info("Initializing UserPrefsServiceTest");
        mockMap.put("darkMode", "false");
    }

    @AfterEach
    void tearDown() {
        log.info("Tearing down UserPrefsServiceTest");
        mockMap.clear();
    }

    @Test
    public void test_storePrefs_returnsException() throws InvalidUserPrefsException, JsonProcessingException {
        when(mockObjectMapper.writeValueAsString(mockMap)).thenThrow(JsonProcessingException.class);
        assertThrows(InvalidUserPrefsException.class, () -> mockUserPrefsService.storePrefs(mockMap, TEST_KEY));
    }

    @Test
    public void test_retrievePrefs() {
        when(mockS3Repository.readUserPrefsFromS3(TEST_KEY)).thenReturn("mockPrefs");
        assertDoesNotThrow(() -> mockUserPrefsService.retrievePrefs(TEST_KEY));
    }

    @Test
    public void test_retrievePrefs_throwsException() {
        when(mockS3Repository.readUserPrefsFromS3(TEST_KEY))
                .thenThrow(UnableToReadUserPrefsException.class);
        assertThrows(UnableToReadUserPrefsException.class, () -> mockUserPrefsService.retrievePrefs(TEST_KEY));
    }
}
