Cargo Tracker Monolith
======================
This is a vanilla Java EE application of moderate complexity. In microservices 
parlance, the application can be considered a monolith.

Part of this code is used as a demo for 
[this](https://speakerdeck.com/reza_rahman/down-to-earth-microservices-with-java-ee) talk. A
video for the talk can be found [here](https://www.youtube.com/watch?v=bS6zKgMb8So).
The code is derived from the [Cargo Tracker](https://m-reza-rahman.github.io/cargo-tracker/) project.

The application uses Payara 5. It should be possible to use any Java EE 8 
compatible application server such as GlassFish, WildFly or WebSphere Liberty. 
We use NetBeans here but you can use any Maven capable IDE.

Setup
-----
* Download the code in this directory somewhere into your file system.
* Make sure you have the latest version of JDK 8 installed. Older and newer versions may cause unexpected issues.
* Please download and install NetBeans 8.2 from [here](https://netbeans.org/downloads/). Make sure to download the Java EE edition. Older and newer versions may cause unexpected issues.
* Download Payara Server 5 from [here](https://www.payara.fish/downloads).
* Please unzip the zip file anywhere in your file system.
* You need to setup Payara in NetBeans. You do that by going to 
Services -> Servers -> Add Server -> GlassFish Server. Enter the location of 
the Payara directory. Choose the defaults in the next few screens to register Payara with NetBeans.

Instructions
------------
* Start NetBeans. There is a Maven project in the directory - cargo-tracker. You 
need to open and build the project in NetBeans.
* You will need to specify that the project will run with Payara. You do that 
by going to Project -> Properties -> Run -> Server and choosing Payara.
* When ready, you need to run the cargo-tracker application (Project -> Run). 
Wait for the project to deploy and run. NetBeans should automatically open a 
browser window with the running application.
* You need to book and route a cargo. Please take a look at the video for the 
Cargo Tracker application on how to do this or look through the readme of the 
Cargo Tracker application.
