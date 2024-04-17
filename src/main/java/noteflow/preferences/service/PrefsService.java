package noteflow.preferences.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noteflow.preferences.exception.InvalidUserPrefsException;
import noteflow.preferences.exception.UnableToReadPrefsException;
import noteflow.preferences.exception.UnableToStorePrefsException;
import noteflow.preferences.repository.S3Repository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
// TODO:@Timed annotation
public class PrefsService {
    private final ObjectMapper objectMapper;
    private final S3Repository s3Repository;


    /**
     * Store the preferences as a string to S3
     *
     * @param jsonMap a map of the JSON object obtained from the request body
     * @param key     the unique ID used to store the preferences
     * @throws InvalidUserPrefsException   if the preferences are invalid
     * @throws UnableToStorePrefsException if the preferences could not be stored
     */

    public void storePrefs(final Map<String, Object> jsonMap, final String key) {
        try {
            String prefs = objectMapper.writeValueAsString(jsonMap);
            s3Repository.storePrefsToS3(prefs, key);
        } catch (JsonProcessingException e) {
            log.error("When storing preferences for " + key + ", a JSON Processing exception occured", e);
            throw new InvalidUserPrefsException();
        }
    }

    /**
     * Retrieve the preferences from S3
     *
     * @param key
     * @return the preferences
     * @throws UnableToReadPrefsException if the repository call throws an exception
     */
    public String retrievePrefs(final String key) {
        return s3Repository.readUserPrefsFromS3(key);
    }
}
