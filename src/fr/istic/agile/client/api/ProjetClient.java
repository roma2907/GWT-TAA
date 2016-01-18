package fr.istic.agile.client.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import fr.istic.agile.client.domain.Project;

@Path("/rest/projet")
public interface ProjetClient extends RestService {

    @GET
    public void getProjets(MethodCallback<List<Project>> callback);

    @POST
    public void addProject(Project project, MethodCallback<Void> callback);

    @PUT
    public void updateProject(Project newProject, MethodCallback<Void> callback);

    @GET
    @Path("/exist/{name}")
    public void existName(@PathParam("name") String name, MethodCallback<Boolean> callback);

    @GET
    @Path("/exist/{name}/{id}")
    public void existNameWithIdentifiant(@PathParam("name") String name, @PathParam("id") Long idProject,
            MethodCallback<Boolean> callback);

    @GET
    @Path("/{idProject}")
    public void getProject(@PathParam("idProject") Long idProject, MethodCallback<Project> callback);
}