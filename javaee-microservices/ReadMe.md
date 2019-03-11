Simple Microservices using Vanilla Java EE
==========================================
This code demonstrates developing reasonable microservices appropriate for
most ordinary blue collar IT organizations using nothing but vanilla Java EE 
and simple, fast deploying thin war files. The code is used as a demo for 
[this](https://speakerdeck.com/reza_rahman/down-to-earth-microservices-with-java-ee) talk. A
video for the talk can be found [here](https://www.youtube.com/watch?v=bS6zKgMb8So).

The code is derived from the [Cargo Tracker](https://m-reza-rahman.github.io/cargo-tracker/) project. 
Although logically two separate applications that 
project is deliberately structured as a single war for simplicity. This code
breaks the application up into two separate wars. The larger war represents the 
Cargo Tracker application. In microservices parlance the Cargo Tracker 
application is a so-called monolith. The smaller war file represents the 
Path Finder microservice. Cargo Tracker uses the smaller Path Finder service.

This code uses Payara 5. It should be possible to use any Java EE 8 compatible application server such as 
GlassFish, WildFly or WebSphere Liberty. The code uses NetBeans but you can use any Maven 
capable IDE. 

Setup
-----
* Download this directory somewhere into your file system.
* Make sure you have the latest version of JDK 8 installed. Older and newer versions may cause unexpected issues.
* Please download and install NetBeans 8.2 from [here](https://netbeans.org/downloads/). Make sure to download the Java EE edition. Older and newer versions may cause unexpected issues.
* Download Payara Server 5 from [here](https://www.payara.fish/downloads).
* Please unzip the zip file anywhere in your file system.
* Start NetBeans. You need to setup Payara in NetBeans. You do that by going to 
Services -> Servers -> Add Server -> GlassFish Server. Enter the location of 
the Payara directory. Choose the defaults in the next few screens to register Payara with NetBeans.

Instructions
------------
* There are two separate Maven projects in the directory - 
cargo-tracker and path-finder. You need to open and build both projects in NetBeans.
* You will need to specify that both projects will run with Payara. You do 
that by going to Project -> Properties -> Run -> Server and choosing Payara.
* When ready, you will first run the path-finder application (Project -> Run). 
Wait for the project to deploy and run. Then you will similarly run the 
Cargo Tracker application. In both cases, NetBeans should automatically open a 
browser window with the running application.
* You need to book and route a cargo. Please take a look at the video for the 
Cargo Tracker application on how to do this or look through the readme of the 
original Cargo Tracker application. The Path Finder service is used for
routing by Cargo Tracker.
* In this demo both Cargo Tracker and Path Finder run on the same Payara 
instance. If you want you can run the two wars on two different servers. Just 
make the appropriate changes in [ejb-jar.xml for Cargo Tracker](cargo-tracker/src/main/webapp/WEB-INF/ejb-jar.xml) to point it to the location 
of the Path Finder service. Most servers will also allow you to change the JNDI 
entry value at runtime through GUI administrative tools without any changes to 
the war. You can also use load balancers and DNS with the two applications if 
you like to add greater flexibility or fault tolerance.
