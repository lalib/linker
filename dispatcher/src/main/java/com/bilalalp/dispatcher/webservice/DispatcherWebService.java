package com.bilalalp.dispatcher.webservice;

import com.bilalalp.dispatcher.dto.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

    @POST
    @Path("/createWordSummary")
    WordSummaryCreateResponse createWordSummary(WordSummaryCreateRequest wordSummaryCreateRequest);

    @POST
    @Path("/selectKeyword")
    KeywordSelectionResponseDto selectKeyword(KeywordSelectionRequestDto keywordSelectionRequestDto);

    @GET
    @Path("/eliminate")
    Response eliminateKeywords(@QueryParam("lsrId") Long lsrId, @QueryParam("threshold") Long threshold);

    @GET
    String state();
}