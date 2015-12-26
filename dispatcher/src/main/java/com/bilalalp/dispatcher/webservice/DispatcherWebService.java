package com.bilalalp.dispatcher.webservice;

import com.bilalalp.dispatcher.dto.LinkSearchRequest;
import com.bilalalp.dispatcher.dto.LinkSearchResponse;
import com.bilalalp.dispatcher.dto.StopWordCreateRequest;
import com.bilalalp.dispatcher.dto.StopWordCreateResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/dispatcher")
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public interface DispatcherWebService {

    @POST
    @Path("/search")
    LinkSearchResponse linkSearch(final LinkSearchRequest linkSearchRequest);

    @POST
    @Path("/createStopWord")
    StopWordCreateResponse createStopWord(StopWordCreateRequest stopWordCreateRequest);

    @GET
    String state();
}