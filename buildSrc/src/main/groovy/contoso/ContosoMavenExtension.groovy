package contoso

import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler

abstract class ContosoMavenExtension {

    private final RepositoryHandler repositories
    private final Project project

    ContosoMavenExtension(RepositoryHandler repositories, Project project) {
        this.repositories = repositories
        this.project = project
    }

    def codeArtifact() {
        def endpoint = project.property("contoso.m2.endpoint")
        def awsProfile = project.property('contoso.aws.profile')
        repositories.maven {
            url endpoint
            credentials {
                username = 'aws'
                password = "aws codeartifact get-authorization-token --domain temporary --query authorizationToken --output text --profile ${awsProfile}".execute().getText()
            }
        }
    }
}
