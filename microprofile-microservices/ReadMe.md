Java EE Microservices using Fat Jars and MicroProfile
=====================================================
This code demonstrates developing basic fat-jar based microservices using Java EE 
and MicroProfile.

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

We use Payara Micro 5 as our fat-jar solution. It should be possible to use any 
other MicroProfile compatible runtime such as Thorntail. We use NetBeans but 
you can use any Maven capable IDE.

Setup
-----
* Download this directory somewhere into your file system.
* Make sure you have the latest version of JDK 8 installed. Older and newer 
versions may cause unexpected issues.
* Please download and install NetBeans 8.2 from [here](https://netbeans.org/downloads/). 
Make sure to download the Java EE edition. Older and newer versions may cause unexpected issues.
* Download Payara Server 5 from [here](https://www.payara.fish/downloads).
* Please unzip the zip file anywhere in your file system.
* Start NetBeans. You now need to setup Payara Server in NetBeans. You do that by going to 
Services -> Servers -> Add Server -> GlassFish Server. Enter the location of 
the Payara Server directory. Choose the defaults in the next few screens to register 
Payara Server with NetBeans.

Instructions
------------
* There are two separate Maven projects in the directory - cargo-tracker and 
path-finder. You need to open and build both projects in NetBeans.
* You first need to run the path-finder microservice. You can run it through 
Project -> Run Maven -> Run with Payara Micro. This is a custom NetBeans action
that executes the `clean install payara-micro:start` Maven goal. Please wait for the project to 
start. You can also run the path-finder application by going to it's target 
directory and executing (once you have done a normal Maven build):
```
java -jar path-finder-1.0-microbundle.jar
```
* Next you will run the cargo-tracker application. You will need to specify that 
the cargo-tracker project will run with Payara Server. You do that by going to 
Project -> Properties -> Run -> Server and choosing Payara Server. You will run the 
Cargo Tracker application by selecting Project -> Run. When the application 
finishes deploying, NetBeans should automatically open a browser window with the 
running application.
* You need to book and route a cargo. Please take a look at the video for the 
Cargo Tracker application on how to do this or look through the readme of the 
original Cargo Tracker application. The Path Finder service is used for
routing by Cargo Tracker.

Running with Docker (Optional)
------------------------------
You can optionally run the path finder application using Docker.

You can first build a Docker image by issuing:
```
mvn clean package -Pdocker
```
To run path finder using Docker you will need to run:
```
docker run -it -p 8888:8080 pragmatic-microservices-lab/path-finder:1.0
```
