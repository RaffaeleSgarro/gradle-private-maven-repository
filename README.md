# gradle-private-maven-repository

The purpose of this project is to demonstrate how to create a DSL `mavenContoso()` to be used like this

```groovy
// Inside vendor-html-theme/build.gradle
publishing {
    repositories {
        mavenContoso()
    }
}

// Inside app1/build.gradle
repositories {
    mavenContoso()
}
```

The `mavenContoso()` is a custom method that will be used to configure the repository. The password **is not**
a static string but an expensive network call, so it should be called only once and only if needed.

To create a CodeArtifact repository on AWS (you may want to set `AWS_PROFILE` or append `--profile=contoso` to the commands):
```shell
aws codeartifact create-domain --domain temporary
aws codeartifact create-repository --domain temporary --repository m2
```

To delete the repository:
```shell
aws codeartifact delete-repository --domain temporary --repository m2
aws codeartifact delete-domain --domain temporary
```