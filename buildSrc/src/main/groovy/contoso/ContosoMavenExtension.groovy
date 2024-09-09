package contoso

import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.provider.Provider

abstract class ContosoMavenExtension {

    private final RepositoryHandler repositories
    private final Project project
    private final Provider<ContosoAwsCredentialsService> contosoAwsCredentials

    ContosoMavenExtension(
            RepositoryHandler repositories,
            Project project,
            Provider<ContosoAwsCredentialsService> contosoAwsCredentials) {
        this.repositories = repositories
        this.project = project
        this.contosoAwsCredentials = contosoAwsCredentials
    }

    def codeArtifact() {
        def contosoAwsCredentials = contosoAwsCredentials
        def endpoint = project.property('contoso.m2.endpoint')
        repositories.maven {
            url endpoint
            credentials {
                username = 'aws'
                password = contosoAwsCredentials.get().accessToken
            }
        }
    }
}
