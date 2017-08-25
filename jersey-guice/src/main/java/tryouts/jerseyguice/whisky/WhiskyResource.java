package tryouts.jerseyguice.whisky;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;
import java.util.Optional;

@Singleton
@Path("whiskies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WhiskyResource {

    @Inject
    private WhiskyDao whiskyDao;

    @GET
    public Response getAll() {
        List<Whisky> whiskies = whiskyDao.readAll();
        return Response.status(Status.OK).entity(whiskies).build();
    }

    @GET
    @Path("{id}")
    public Response getOne(@PathParam("id") String id) {
        if (id == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        Optional<Whisky> whisky = whiskyDao.read(id);
        if (!whisky.isPresent()) {
            return Response.status(Status.NOT_FOUND).build();
        }

        return Response.status(Status.OK).entity(whisky.get()).build();
    }

    @POST
    public Response addOne(Whisky whisky) {
        if (whisky == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        Optional<Whisky> created = whiskyDao.create(whisky);
        return Response.status(Status.OK).entity(created.get()).build();
    }

    @PUT
    @Path("{id}")
    public Response updateOne(@PathParam("id") String id, Whisky whisky) {
        if (id == null || whisky == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        Optional<Whisky> foundOpt = whiskyDao.read(id);
        if (!foundOpt.isPresent()) {
            return Response.status(Status.NOT_FOUND).build();
        }

        Whisky found = foundOpt.get();
        found.setName(whisky.getName());
        found.setOrigin(whisky.getOrigin());

        Optional<Whisky> updated = whiskyDao.update(found);
        return Response.status(Status.OK).entity(updated.get()).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteOne(@PathParam("id") String id) {
        if (id == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        whiskyDao.delete(id);
        return Response.status(Status.OK).build();
    }
}
