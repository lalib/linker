package com.bilalalp.dispatcher.webservice;

import com.bilalalp.dispatcher.dto.LinkSearchRequest;
import com.bilalalp.dispatcher.dto.LinkSearchResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/dispatcher")
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public interface DispatcherWebService {

    @POST
    @Path("/search")
    LinkSearchResponse linkSearch(final LinkSearchRequest linkSearchRequest);

    @GET
    String state();
}