Pragmatic Microservices with Java EE and WildFly Swarm
======================================================
This hands-on lab demonstrates developing reasonable microservices appropriate 
for most ordinary blue collar IT organizations in a step-by-step fashion. We 
initially focus on nothing but vanilla Java EE and simple, fast deploying thin 
war files. We then also cover concepts such as fat-jars, configuration, 
circuit-breakers, metrics and health checks using MicroProfile and Payara Micro. Lastly we 
will also discuss container solutions like Docker and cloud platforms such as
AWS.

Part of this code is used as a demo for 
[this](https://speakerdeck.com/reza_rahman/down-to-earth-microservices-with-java-ee) talk. A
video for the talk can be found [here](https://www.youtube.com/watch?v=bS6zKgMb8So).
The code is derived from the [Cargo Tracker](https://m-reza-rahman.github.io/cargo-tracker/) project. 

This lab uses Payara 5. It should be possible to use any Java EE 8 compatible application server such as 
GlassFish, WildFly or WebSphere Liberty. We use Payara Micro as our fat-jar solution. It should be possible 
to use any other MicroProfile compatible runtime such as Thorntail. We use NetBeans but you can use any Maven 
capable IDE.

Setup
-----
* Download this project somewhere into your file system, probably as a zip file 
(and extract it).
* Make sure you have the latest version of JDK 8 installed. Older and newer versions may cause unexpected issues.
* Please download and install NetBeans 8.2 from [here](https://netbeans.org/downloads/). Make sure to download the Java EE edition. Older and newer versions may cause unexpected issues.
* Download Payara Server 5 from [here](https://www.payara.fish/downloads).
* Please unzip the zip file anywhere in your file system.
* You now need to setup Payara in NetBeans. You do that by going to 
Services -> Servers -> Add Server -> GlassFish Server. Enter the location of 
the Payara directory. Choose the defaults in the next few screens to register Payara with NetBeans.

Instructions
------------
* There are three distinct sections of the lab separated into separate directories
of the zip - [monolith](monolith/), [javaee-microservices](javaee-microservices/) 
and [microprofile-microservices](microprofile-microservices/). We will explore each in turn.
* After discussing the basic concepts behind microservices, we will explore the
code under [monolith](monolith/). The directory contains a single vanilla 
Java EE application named Cargo Tracker. In microservices parlance, the 
application can be considered a monolith. Please follow the instructions in the 
directory to get the application running.
* Having explored the Cargo Tracker Java EE application, we will discuss how the 
routing service can be separated into a separate REST service. We will call this
separate service the path-finder application. In microservices parlance, Path 
Finder can be considered a microservice. We will develop path-finder as a 
separate Maven war project using JAX-RS, JSON-B, EJB, CDI and Bean Validation. The 
Cargo Tracker application will use the JAX-RS client API to access the 
Path Finder application along with JSON-B. Cargo Tracker and Path Finder can be deployed to the 
same Payara instance or different Payara instances.
* The [javaee-microservices](javaee-microservices/) directory has a possible 
solution for breaking up the Cargo Tracker application into simple, practical 
vanilla Java EE based microservices. You should follow the instructions in the 
directory to get the solution running and explore it. We will discuss this 
solution in detail.
* Having discussed how we can develop simple microservices using vanilla Java EE
war files, we are ready to introduce Payara Micro as a fat jar solution. 
Introducing Payara Micro also enables us to add other MicroProfile features besides fat jars
such as configuration, health-check, circuit-breakers/bulkheads and 
client-side load-balancing. After discussing these concepts, we will convert 
path-finder from a war file to a self-contained fat-jar.
* The [swarm-microservices](swarm-microservices/) directory has a possible 
solution for converting path-finder into a WildFly Swarm based fat jar.
You should follow the instructions in the directory to get the solution running 
and explore it. We will discuss this solution in detail.
* The [complex-concepts](complex-concepts/) directory contains some examples of 
more advanced concepts such as health-check, dynamic discovery, containers 
and cloud deployment. We will discuss these concepts as time permits.

To Do
-----
* Use AWS or Jelastic.
* Move from GlassFish to WildFly.
