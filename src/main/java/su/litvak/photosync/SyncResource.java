package su.litvak.photosync;

import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import com.yammer.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Admin
 * Date: 8/12/12
 * Time: 10:06 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/sync")
@Produces(MediaType.APPLICATION_JSON)
public class SyncResource {
    private String folder;
    private Logger logger = LoggerFactory.getLogger(SyncResource.class);

    public SyncResource(String folder) {
        this.folder = folder;
    }

    @POST
    @Timed
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response sync(
            @FormDataParam("file") final InputStream stream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) {
        logger.info("Received file: " + fileDetail);

        try {
            Files.copy(new InputSupplier<InputStream>() {
                @Override
                public InputStream getInput() throws IOException {
                    return stream;
                }
            }, new File(folder, fileDetail.getFileName()));
        } catch (IOException ioex) {
            logger.error("Error while copying file", ioex);
        }

        return Response.status(200).entity("Received file: " + fileDetail.getFileName() + " of type: " + fileDetail.getType()).build();
    }
}
