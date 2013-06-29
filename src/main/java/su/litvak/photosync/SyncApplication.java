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

import org.glassfish.hk2.api.DynamicConfiguration;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.internal.inject.Injections;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.inject.Inject;

public class SyncApplication extends ResourceConfig {

    @Inject
    public SyncApplication(ServiceLocator serviceLocator) {
        packages(false, getClass().getPackage().getName() + ".resources");
        register(MultiPartFeature.class);
        register(JacksonFeature.class);
        register(LoggingFilter.class);

        /**
         * Bind injections
         */
        DynamicConfiguration dc = Injections.getConfiguration(serviceLocator);
        Injections.addBinding(
                Injections.newBinder(Config.get())
                        .to(Config.class),
                dc);

        dc.commit();
    }
}
