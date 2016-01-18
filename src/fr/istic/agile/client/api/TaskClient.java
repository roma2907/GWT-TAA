package fr.istic.agile.client.api;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import fr.istic.agile.client.domain.Task;

@Path("/rest/task")
public interface TaskClient extends RestService {

    @PUT
    public void putTask(final Task task, final MethodCallback<Void> callback);

    @POST
    @Path("/{idUserStory}")
    public void addTask(final Task task, @PathParam("idUserStory") Long idUserStory,
            final MethodCallback<Void> callback);

    @DELETE
    @Path("/{idTask}")
    public void deleteTask(@PathParam("idTask") Long idTask, final MethodCallback<Void> callback);

}
