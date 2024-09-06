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

**IMPORTANT** Get the repository endpoint like this (the `contoso.aws.profile` property is used to get the
authentication token in `build.gradle` files):
```shell
 aws codeartifact get-repository-endpoint --domain temporary --repository m2 --format maven
```
and put in `GRADLE_USER_HOME/gradle.properties`
```properties
contoso.m2.endpoint=https://temporary-123456789.d.codeartifact.eu-central-1.amazonaws.com/maven/m2/
contoso.aws.profile=contoso
```


To delete the repository:
```shell
aws codeartifact delete-repository --domain temporary --repository m2
aws codeartifact delete-domain --domain temporary
```