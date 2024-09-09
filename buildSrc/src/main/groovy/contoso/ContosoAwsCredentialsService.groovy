package contoso

import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.codeartifact.AWSCodeArtifactAsyncClient
import com.amazonaws.services.codeartifact.model.GetAuthorizationTokenRequest
import com.amazonaws.services.codeartifact.model.GetAuthorizationTokenResult
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.provider.Property
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters

abstract class ContosoAwsCredentialsService implements BuildService<Parameters> {

    private static final Logger logger = Logging.getLogger(ContosoAwsCredentialsService)

    private GetAuthorizationTokenResult cachedResponse

    synchronized String getAccessToken() {
        if (cachedResponse == null || cachedResponse.expiration.before(new Date())) {
            refreshAccessTokenWithAwsSdk()
        }
        return cachedResponse.authorizationToken
    }

    private void refreshAccessTokenWithAwsSdk() {
        logger.quiet("Requesting fresh AWS credentials...")
        cachedResponse = AWSCodeArtifactAsyncClient.builder()
                .withRegion(parameters.awsRegion.get())
                .withCredentials(new ProfileCredentialsProvider(parameters.awsProfile.get()))
                .build()
                .getAuthorizationToken(new GetAuthorizationTokenRequest()
                        .withDomain(parameters.codeArtifactDomain.get()))
    }

    interface Parameters extends BuildServiceParameters {
        Property<String> getAwsProfile()
        Property<String> getAwsRegion()
        Property<String> getCodeArtifactDomain()
    }
}
