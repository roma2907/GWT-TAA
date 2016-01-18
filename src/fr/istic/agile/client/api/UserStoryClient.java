package fr.istic.agile.client.api;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import fr.istic.agile.client.domain.UserStory;

@Path("/rest/userstory")
public interface UserStoryClient extends RestService {

    @PUT
    public void putUserStory(final UserStory userStory, final MethodCallback<Void> callback);

    @POST
    @Path("/{idSprint}")
    public void addUserStory(final UserStory userStory, @PathParam("idSprint") Long idSprint,
            final MethodCallback<Void> callback);
}
