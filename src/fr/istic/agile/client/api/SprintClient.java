package fr.istic.agile.client.api;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import fr.istic.agile.client.domain.Sprint;

@Path("/rest/sprint")
public interface SprintClient extends RestService {

    @PUT
    public void putSprint(final Sprint sprint, final MethodCallback<Void> callback);

    @POST
    @Path("/{idProjet}")
    public void addSprint(final Sprint sprint, @PathParam("idProjet") Long idProjet,
            final MethodCallback<Void> callback);
}
