package contoso

import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.provider.Property
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.codeartifact.CodeartifactClient
import software.amazon.awssdk.services.codeartifact.model.GetAuthorizationTokenResponse

import java.time.Instant

abstract class ContosoAwsCredentialsService implements BuildService<Parameters> {

    private static final Logger logger = Logging.getLogger(ContosoAwsCredentialsService)

    private GetAuthorizationTokenResponse cachedResponse

    synchronized String getAccessToken() {
        if (cachedResponse == null || cachedResponse.expiration().isBefore(Instant.now())) {
            refreshAccessTokenWithAwsSdk()
        }
        return cachedResponse.authorizationToken()
    }

    private void refreshAccessTokenWithAwsSdk() {
        logger.quiet("Requesting fresh AWS credentials...")

        cachedResponse = CodeartifactClient.builder()
                .region(Region.of(parameters.awsRegion.get()))
                .credentialsProvider(ProfileCredentialsProvider.create(parameters.awsProfile.get()))
                .build()
                .getAuthorizationToken { req ->
                    req.domain(parameters.codeArtifactDomain.get())
                }
    }

    interface Parameters extends BuildServiceParameters {
        Property<String> getAwsProfile()
        Property<String> getAwsRegion()
        Property<String> getCodeArtifactDomain()
    }
}
