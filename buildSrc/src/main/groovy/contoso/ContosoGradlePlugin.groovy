package contoso;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

class ContosoGradlePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.getExtensions().create("contoso", ContosoMavenExtension)
    }
}