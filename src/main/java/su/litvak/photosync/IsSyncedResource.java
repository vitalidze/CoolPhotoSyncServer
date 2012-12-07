package su.litvak.photosync;

import com.yammer.metrics.annotation.Timed;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Admin
 * Date: 8/12/12
 * Time: 1:05 AM
 * To change this template use File | Settings | File Templates.
 */
@Path("/is_synced")
@Produces(MediaType.APPLICATION_JSON)
public class IsSyncedResource {

    private String folder;

    public IsSyncedResource(String folder) {
        this.folder = folder;
    }

    @GET
    @Timed
    public boolean isSynced(@QueryParam("fileName") String fileName) {
        return new File(folder, fileName).exists();
    }
}
