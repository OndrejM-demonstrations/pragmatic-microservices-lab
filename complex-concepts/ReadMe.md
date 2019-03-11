Complex Microservices Concepts
==============================
This code demonstrates developing advanced/complex microservices including 
fat jars, health, configuration, containerized deployments as well as cloud 
deployments using Java EE, MicroProfile, Docker and AWS.

Part of this code is used as a demo for 
[this](https://speakerdeck.com/reza_rahman/down-to-earth-microservices-with-java-ee) talk. A
video for the talk can be found [here](https://www.youtube.com/watch?v=bS6zKgMb8So).
The code is derived from the [Cargo Tracker](https://m-reza-rahman.github.io/cargo-tracker/) project.

The overall application is broken up into two logical units - Cargo Tracker and 
Path Finder. The Cargo Tracker application is a larger Java EE war deployed to Payara. 
In microservices parlance the Cargo Tracker application is a so-called 
monolith. The Path Finder microservice is developed as a MicroProfile fat jar.
Cargo Tracker uses the smaller Path Finder microservice.

This code uses Payara Server 5. It should be possible to use any Java EE 8
compatible application server such as GlassFish, WildFly or WebSphere Liberty.

We use Payara Micro as our fat-jar solution. It should be possible to use any 
other MicroProfile compatible runtime such as Thorntail. We use NetBeans but 
you can use any Maven capable IDE.

Setup
-----
* Download this directory somewhere into your file system.
* Make sure you have the latest version of JDK 8 installed. Older and newer versions may cause unexpected issues.
* Please download and install NetBeans 8.2 from [here](https://netbeans.org/downloads/). Make sure to download the Java EE edition. Older and newer versions may cause unexpected issues.
* Download Payara Server 5 from [here](https://www.payara.fish/downloads).
* Please unzip the zip file anywhere in your file system.
* optionally, download and install Consul to try out Consul-based service discovery later: https://www.consul.io/
* You now need to setup Payara Server in NetBeans. You do that by going to 
Services -> Servers -> Add Server -> GlassFish Server. Enter the location of 
the Payara Server directory. Choose the defaults in the next few screens to register 
Payara Server as a GlassFish Server with NetBeans.
* build the project with `mvn install` - this will build all the subprojects

Instructions for running with discovery over Payara datagrid
------------

* You first need to start the cargo-tracker application. You will need to specify that 
the cargo-tracker project will run with Payara Server. You do that by going to 
Project -> Properties -> Run -> Server and choosing Payara Server. You will run the 
Cargo Tracker application by selecting Project -> Run. When the application 
finishes deploying, NetBeans should automatically open a browser window with the 
running application. NOTE: Don't use the application yet, it needs the pathfinder microservice to be available.

* Then run the path-finder microservice. You can run it through 
Project -> Run Maven -> Run with Payara Micro. This is a custom NetBeans action
that executes the payara-micro:start Maven goal. Please wait for the project to 
start. You can also run the path-finder application by going to it's target 
directory and executing:
```
java -jar path-finder-1.0-microbundle.jar
``` 
* The path-finder microservice should join the same Payara datagrid together with the cargo-tracker application. This is required for the JCache-based discovery to work. Note that if you start path-finder first, it won't connect to cargo-tracker immediately but it will take some time until it discovers that cargo-tracker is up and running. Therefore it's best if cargo-tracker is started first
* You need to book and route a cargo. Please take a look at the video for the 
Cargo Tracker application on how to do this or look through the readme of the 
original Cargo Tracker application. The Path Finder service is used for
routing by Cargo Tracker.
* The Path Finder microservice is equipped with some basic health check 
facilities that meet the MicroProfile Health specification. You can check them out by
navigating to <http://localhost:8888/health> and <http://localhost:8888/metrics>. The outcome of the health status is used to report to the discovery mechanism whether the service is healthy and can be accessed.
* Since Payara Server also supports MicroProfile, metrics are exposed also for the Cargo Tracker application at <http://localhost:8080/health> and <http://localhost:8080/metrics>.

Instructions for Running with Discovery using Consul
----------------------------------------------------
* You must start Consul:
```
consul agent -dev
```
* Please verify that Consul is running by going to <http://localhost:8500/>. 
There won't be much to see right now. As the microservice comes up, you'll see
it show up here. 
* Rebuild the project with the `consul-discovery` Maven profile:

```
mvn -Pconsul-discovery clean install
```

* Continue with the instructions for running with discovery over Payara datagrid
* You may observe that the pathfinder service is registered in Consul: http://localhost:8500

Advanced patterns using MicroProfile
------------------------------------

### Configuration across multiple services

With MicroProfile Config mechanism, it's simple to access configuration defined in various places. By default, configuration is provided from a config file on the classpath or by environment variables or system properties. 

MicroProfile allows reading configuration either via injection or programmatically. The first one is most suitable in MicroProfile containers like Payara Micro or Payara Server. In path-finder application, the `ServiceActiveHealthCheck` class uses it to read the time limit for inactivity (if a request isn't processed during that time, the service isn't considered healthy). The configuration is just injected like this:

```
    @Inject
    @ConfigProperty(name = "ServiceActiveHealthCheck.timeToBecomeInactiveInSeconds", defaultValue = "5")
    int limitForInactivityInSeconds;
```

In cargo-tracker, we use the programmatic approach. We get a `Config` interface and then ask for configuration values:

```
    Config config = ConfigProvider.getConfig();
    String urlString = config.getValue("discovery.service.pathfinder.url", String.class);
```

This approach doesn't require CDI and can be used in any application. In Payara Server it works out of the box. In Java EE servers that don't provide MicroProfile API you can just add a MicroProfile Config library into your application to make it work.

Both Payara Micro and Payara Server provide additional ways how to define configuration. They are called config sources. For example, a config source shared by all instances in the same datagrid (cluster). This type of configuration is changed once and instantly visible for example for both cargo-tracker and path-finder if they join the same datagrid. Another example of a config source provided by both Payara Micro and Payara Server is a directory source. This is very often used in cloud environments to provide secret data. 

It's very easy to implement custom config sources just by implementing a `ConfigSource` interface and register it via a Service Locator file. As an example, the `jcache-discovery`module contains `JCacheDiscoveryConfigSource` config source, registered with the `org.eclipse.microprofile.config.spi.ConfigSource` service locator file.

### Using config sources for service discovery

MicroProfile doesn't contain specific support for service discovery. However, information about services can be easily provided by a config source via the configuration API. As an example, to discover a path-finder service in cargo-tracker, the class `ExternalRoutingService` only reads a configuration value `discovery.service.pathfinder.url`, which is provided by either the `JCacheDiscoveryConfigSource` or `ConsulDiscoveryConfigSource`, based on the chosen discovery mechanism.

It's still necessary to register a service at startup in the chosen discovery mechanism. MicroProfile doesn't help here much but it's possible to use the Health Check endpoint to get information about the health of the service and pass it to a discovery mechanism. See the `SelfHealthService` class for an example how to access the health endpoint of the current service to find out the health status. In the future, it's planned to provide API to access this status directly without a REST request.

### Health status

MicroProfile runtimes expose the health status as a REST resource available at the `/health` endpoint. You can access this endpoint for path-finder at <http://localhost:8888/health>. A healthy status yields HTTP OK code (200), an unhealthy status yields an HTTP ERROR code (503 - Service Unavailable). The response body is in JSON format and provides more information aobut individual health checks. By default, the endpoint either always gives a healthy status or gives a status based on some generic checks. It's possible to provide application-specific checks to provide better insight into when a service is considered unhealthy. The path-finder service contains an example of such health check in the `ServiceActiveHealthCheck` class. It considers the service healthy only if it processed a request in past 10 seconds. An unhealthy service can be restarted to make it healthy. It also becomes healthy when issuing an HTTP request to <http://localhost:8888/rest> or any REST request.

### Metrics

Metrics are exposed at the `/metrics` endpoint. You can access this endpoint for path-finder at <http://localhost:8888/metrics>. The body of the response is by default in Prometheus format or in JSON format if requested with the `Accept` HTTP header. The response contains information about various metrics collected either by the MicroProfile container or provided by the application. Application level metrics are displayed only when the metric collected some data, otherwise they are hidden. An example of a metric provided by an application is `findShortestPath`, which is the number of invocations of the `GraphTraversalService` REST resource. This is caused by the `@Counted` annotation placed on the `findShortestPath` method on the class. An application can also provide metrics indirectly via other components. For example, Fault Tolerance interceptors collect metrics to monitor what's going on in the interceptors. An example is the set of metrics for the `GraphDao.listLocations` method, which are enabled by the `@Retry` annotation.

It's also possible to select only a subgroup of metrics, e.g. only those provided by an application:

* <http://localhost:8888/metrics/application>

It's also possible to select a specific metric, e.g. the metric `findShortestPath`:

* <http://localhost:8888/metrics/application/findShortestPath>

### Description of REST endpoints

Documentation about all REST endpoints specified via JAX-RS API is available via the `/openapi` endpoint, e.g. <http://localhost:8888/openapi> for path-finder. The body in OpenAPI JSON format describes REST resources and the way to access them. This is provided completely out of the box. If the default JSON isn't accurate enough, it's possible to annotate REST endpoints in the code to provide more accurate information.
