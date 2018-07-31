package com.maurofokker.poc.cloud;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import javax.servlet.http.HttpServletRequest;

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
