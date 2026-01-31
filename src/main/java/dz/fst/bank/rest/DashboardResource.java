package dz.fst.bank.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

/**
 * REST API pour le Dashboard FSTBANK
 */
@Path("/dashboard")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DashboardResource {
    
    // EJB injections disabled due to Java 17 proxy module access issues
    // This REST resource provides basic endpoints with mock data
    
    @GET
    @Path("/status")
    public Response getStatus() {
        Map<String, Object> status = new LinkedHashMap<>();
        status.put("application", "FSTBANK");
        status.put("version", "1.0.0");
        status.put("status", "RUNNING");
        status.put("timestamp", System.currentTimeMillis());
        return Response.ok(status).build();
    }
    
    @GET
    @Path("/stats")
    public Response getStats() {
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totalClients", 0);
        stats.put("totalAccounts", 0);
        stats.put("totalTransactions", 0);
        stats.put("totalBalance", 0.0);
        stats.put("validatedTransactions", 0);
        stats.put("rejectedTransactions", 0);
        stats.put("successRate", 0.0);
        return Response.ok(stats).build();
    }
    
    @GET
    @Path("/clients")
    public Response getClients() {
        return Response.ok(Collections.emptyList()).build();
    }
    
    @GET
    @Path("/accounts")
    public Response getAccounts() {
        return Response.ok(Collections.emptyList()).build();
    }
    
    @GET
    @Path("/transactions")
    public Response getTransactions() {
        return Response.ok(Collections.emptyList()).build();
    }
}

