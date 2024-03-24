package noteflow.preferences.repository;

import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noteflow.preferences.config.S3Properties;
import noteflow.preferences.exception.UnableToReadPrefsException;
import noteflow.preferences.exception.UnableToStorePrefsException;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

@Slf4j
@Repository
@RequiredArgsConstructor
//TODO @Timed annotation
public class S3Repository {

    private final S3Template s3Template;
    private final S3Properties s3Properties;


    /**
     * Store the preferences to S3
     *
     * @param prefs the user's preferences
     * @param key   the unique ID used to store the preferences
     * @throws UnableToStorePrefsException if the preferences could not be stored
     */
    public void storePrefsToS3(String prefs, final String key) {
        try {
            log.info("Storing prefs to S3");
            s3Template.store(s3Properties.getBucketName(), key, prefs);
        } catch (Exception e) {
            log.error("Error storing prefs to S3", e);
            throw new UnableToStorePrefsException();
        }
    }

    /**
     * Read the preferences from S3
     *
     * @param key the unique ID used to store the preferences
     * @return the user's preferences
     * @throws UnableToReadPrefsException if the preferences could not be read
     */
    public String readUserPrefsFromS3(final String key) {
        try {
            log.info("Reading prefs from S3");
            return s3Template.read(s3Properties.getBucketName(), key, String.class);
        } catch (Exception e) {
            //if no preferences were found in the repo for the user, return an empty string
            if (e.getCause().toString().contains(NoSuchKeyException.class.getSimpleName())) {
                return "";
            } else {
                log.error("Error reading prefs from S3", e);
                throw new UnableToStorePrefsException();
            }
        }
    }
}