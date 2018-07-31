# Spring Boot micro service Zuul demo

* Part of [micro services demo](https://github.com/maurofokker/microservices-demo)
* Will consume [spring-microservices-simple-service-3 project](https://github.com/maurofokker/spring-microservices-simple-service-3)
* Zuul implementation will accept external requests and then pass them off to our micro services

## Zuul description

* Terms associated to zuul
  * edge service 
  * gatekeeper
  * API gateway
* Zuul's purpose is to act as a simple routing layer that sits in front of the
  micro services within the distributed system
* Encapsulates the micro service implementation from clients
* Relieves clients of the need to be aware of the latest micro service instances
  and ther locations
* Provides some support for CORS and the same origin policy that are often restrictions
  found on the browser serving as the front door for the distributed system
* Provides a single point of failure even configuring multiple Zuul proxies
* Is a great place to establish dynamic routing, monitoring resiliency and security
  within the distributed system

## Implementation

* This is a form of a reverse proxy using `Zuul` server and the `simple-service-3` represents a micro service that is going
  to be proxied by `Zuul` gatekeeper edge service
