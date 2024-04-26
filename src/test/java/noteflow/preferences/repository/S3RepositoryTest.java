package noteflow.preferences.repository;

import io.awspring.cloud.s3.S3Exception;
import io.awspring.cloud.s3.S3Template;
import noteflow.preferences.config.S3Properties;
import noteflow.preferences.exception.UnableToReadUserPrefsException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class S3RepositoryTest {

    private static final String TEST_KEY = "userKey";

    String mockPrefs = "{\n" +
            "  \"darkMode\": \"false\",\n" +
            "  \"fontSize\": 6\n" +
            "}";

    @Mock
    private S3Template mockS3Template;

    @Mock
    private S3Properties mockS3Properties;

    @InjectMocks
    private S3Repository mockS3Repository;

    @Test
    public void test_storeUserPrefsToS3() {
        assertDoesNotThrow(() -> mockS3Repository.storeUserPrefsToS3(mockPrefs, TEST_KEY));
    }

    @Test
    public void test_readUserPrefsFromS3() {
        assertDoesNotThrow(() -> mockS3Repository.readUserPrefsFromS3(TEST_KEY));
    }

    @Test
    public void test_readUserPrefsFromS3_returnsEmptyString() {
        NoSuchKeyException ex = NoSuchKeyException.builder().message("No key found").build();
        when(mockS3Properties.getBucketName()).thenReturn("bucket123");
        when(mockS3Template.read("bucket123", TEST_KEY, String.class))
                .thenThrow(new S3Exception("message", ex));
        assertDoesNotThrow(() -> mockS3Repository.readUserPrefsFromS3(TEST_KEY));
    }

    @Test
    public void test_readUserPrefsFromS3_throwsException() {
        when(mockS3Properties.getBucketName()).thenReturn("bucket123");
        when(mockS3Template.read("bucket123", TEST_KEY, String.class))
                .thenThrow(new S3Exception("message", new Exception()));
        assertThrows(UnableToReadUserPrefsException.class, () -> mockS3Repository.readUserPrefsFromS3(TEST_KEY));
    }
}
