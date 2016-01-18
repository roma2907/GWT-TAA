package fr.istic.agile.client.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import fr.istic.agile.client.domain.Developper;

@Path("/rest/developpeur")
public interface DevelopperClient extends RestService {

    @GET
    @Path("/userstory/{idUserStory}")
    public void getDevelopperInUserStory(@PathParam("idUserStory") Long idUserStory,
            MethodCallback<List<Developper>> callback);

    @GET
    @Path("/notInProjet/{idProject}")
    public void getDeveloppeurAvailable(@PathParam("idProject") Long idProject,
            MethodCallback<List<Developper>> callback);

    @POST
    public void addDevelopper(final Developper developper,
            final MethodCallback<Void> callback);

    @GET
    public void getDeveloppers(MethodCallback<List<Developper>> callback);
}
