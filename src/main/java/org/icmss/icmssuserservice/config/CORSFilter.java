package org.icmss.icmssuserservice.config;


import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;

public class CORSFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext cresp, ContainerResponseContext containerResponseContext) throws IOException {
        cresp.getHeaders().putSingle("Access-Control-Allow-Origin", "*");
        cresp.getHeaders().putSingle("Access-Control-Allow-Credentials", "true");
        cresp.getHeaders().putSingle("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS, HEAD");
        cresp.getHeaders().putSingle("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With");

    }
}