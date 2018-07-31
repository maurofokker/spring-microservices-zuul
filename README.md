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
* Enable `@EnableZuulProxy` annotation
* Modify `application.properties` file
  ```properties
    ribbon.eureka.enabled=false
    server.port=8080
    
    # Zuul configurations
    zuul.routes.somePath.url=http://localhost:7777
    zuul.routes.somePath.path=/somePath/**
    zuul.prefix=/v1
  ```
  * `ribbon.eureka.enabled=false` don't use eureka because is going to be manually configured zuul
  * `server.port=8080` server run in port 8080
  * `zuul.routes.somePath.url=http://localhost:7777` zuul will proxy from `http://localhost:8080/somePath/service` to `http://localhost:7777/service`
    * `somePath` pattern that must be found within the URL in order for the proxying to occur to the micro service
    * to make above more explicit add `zuul.routes.somePath.path=/somePath/**` property in order to avoid confusion
    * if above is configured with different path (i.e. _/otherPath/**_) then routing will be from `http://localhost:8080/otherPath/service` to `http://localhost:7777/service`
  * `zuul.prefix=/v1` add a prefix to the Rest API -> `http://localhost:8080/v1/somePath/service`

* Zuul filters
  * routing filters that control the flow of requests into the gateway API and eventually
  to micro services
  * using filtes allow to develop custom filters that perform different operations 
  on requests as they come in and are routed through the zuul edge service
  * to create a custom filter extend `ZuulFilter` abstract class and implement methods
  ```java
    public class MyZuulFilter extends ZuulFilter {
    
        // there are different stages in the routing of a request that enters the gateway API or edge service
        // this filter can ran at various points within those stages in the process, options are:
        // pre: filters are executed before the request is routed
        // route: filters can handle the actual routing of the request to impact how the request is built and sent ribbon
        // post: filters are executed after the request has been routed allows to add headers to response or modify response after the fact
        // error: filters execute if an error occurs in the course of handling the request.
        @Override
        public String filterType() {
            return "pre";
        }
    
        // determine what order this filter is in within the series of filters
        // registered within zuul. If there are 3 filters then the order is 1, 2 or 3
        // in this case there is just 1 filter
        @Override
        public int filterOrder() {
            return 1;
        }
    
        // determine if whether or not filter should run
        @Override
        public boolean shouldFilter() {
            // RequestContext can be used here to add some logic to allow
            // to inspect current request to make some sort of determination
            // about whether or not this filter should run
            return true;
        }
    
        // is where logic of the filter is executed
        @Override
        public Object run() throws ZuulException {
            System.out.println("request passed through custom zuul pre filter");
            RequestContext ctx = RequestContext.getCurrentContext();
            HttpServletRequest request = ctx.getRequest();
            System.out.println(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));
            return null;
        }
    }
  ```
  * register custom class to IoC container and Zuul server
  ```java
    @Bean
    public MyZuulFilter filter() {
        return new MyZuulFilter();
    }
  ```
  * hit `http://localhost:8080/v1/somePath/service` and test results
  ```
  2018-07-31 00:00:14.348  INFO 12187 --- [nio-8080-exec-1] o.s.c.n.zuul.web.ZuulHandlerMapping      : Mapped URL path [/v1/somePath/**] onto handler of type [class org.springframework.cloud.netflix.zuul.web.ZuulController]
  request passed through custom zuul pre filter
  GET request to http://localhost:8080/v1/somePath/service
  ```