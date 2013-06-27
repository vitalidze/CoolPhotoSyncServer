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
package su.litvak.photosync;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8081);
        ServletContextHandler context = new ServletContextHandler();
        ServletContainer jerseyContainer = new ServletContainer(new ResourceConfig()
                .packages(false, "su.litvak.photosync.resources")
                .register(MultiPartFeature.class)
                .register(LoggingFilter.class)
        );
        ServletHolder servletHolder = new ServletHolder(jerseyContainer);
        context.addServlet(servletHolder, "/*");
        server.setHandler(context);
        server.start();
        server.join();
    }
}
