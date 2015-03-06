Flux JSF 2 Managed Bean Example
===============================

Flux can be implemented as a JSF 2 Managed bean, which allows users to schedule or manage workflows from their UI component.

Deploy flux.jar to your local maven repository

```
mvn install:install-file -DgroupId=flux -DartifactId=flux -Dversion=8.0.10 -Dpackaging=jar -Dfile=flux.jar
```

Build the war using maven

```
mvn war:war
```

Deploy the war file from target/flux-jsf.war to your application server. Here is a sample UI deployed to Tomcat.

![Alt text](/flux-jsf.png?raw=true "Flux JSF UI")


