package contoso;

import org.gradle.api.Plugin;
import org.gradle.api.Project
import org.gradle.api.services.BuildServiceRegistry

import javax.inject.Inject;

abstract class ContosoGradlePlugin implements Plugin<Project> {

    @Inject
    abstract BuildServiceRegistry getSharedServices()

    @Override
    void apply(Project project) {
        def contosoAwsCredentials =  sharedServices.registerIfAbsent("contosoAwsCredentials", ContosoAwsCredentialsService) {
            parameters {
                awsProfile = project.property('contoso.aws.profile')
            }
        }
        project.repositories.extensions.create("contoso", ContosoMavenExtension, project.repositories, project, contosoAwsCredentials)
        project.pluginManager.withPlugin("maven-publish") {
            project.publishing.repositories.extensions.create("contoso", ContosoMavenExtension, project.publishing.repositories, project, contosoAwsCredentials)
        }
    }
}
