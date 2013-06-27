/*
 * (C) Copyright 2013 Vitaly Litvak (http://litvak.su/).
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package su.litvak.photosync.resources;

import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Path("/sync")
@Produces(MediaType.APPLICATION_JSON)
public class SyncResource {
    private Logger logger = LoggerFactory.getLogger(SyncResource.class);

    @POST
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
            }, new File(fileDetail.getFileName()));
        } catch (IOException ioex) {
            logger.error("Error while copying file", ioex);
        }

        return Response.status(200).entity("Received file: " + fileDetail.getFileName() + " of type: " + fileDetail.getType()).build();
    }
}
