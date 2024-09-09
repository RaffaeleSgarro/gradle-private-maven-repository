package contoso

import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.provider.Property
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters

abstract class ContosoAwsCredentialsService implements BuildService<Parameters> {

    private static final Logger logger = Logging.getLogger(ContosoAwsCredentialsService)

    private String cachedToken

    synchronized String getAccessToken() {
        if ((cachedToken == null) || cachedAccessTokenExpired) {
            cachedToken = freshAccessTokenWithAwsSdk
        }
        return cachedToken
    }

    private boolean isCachedAccessTokenExpired() {
        return false
    }

    private String getFreshAccessTokenWithAwsSdk() {
        def awsProfile = parameters.awsProfile.get()
        logger.quiet("Requesting fresh AWS credentials with profile ${awsProfile}...")
        return "aws codeartifact get-authorization-token --domain temporary --query authorizationToken --output text --profile ${awsProfile}".execute().getText()
    }

    interface Parameters extends BuildServiceParameters {
        Property<String> getAwsProfile()
    }
}
