package su.litvak.photosync;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

/**
 * Created with IntelliJ IDEA.
 * User: Admin
 * Date: 8/12/12
 * Time: 12:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class SyncService extends Service<SyncConfiguration> {

    public static void main(String[] args) throws Exception {
        new SyncService().run(args);
    }

    @Override
    public void initialize(Bootstrap<SyncConfiguration> syncConfigurationBootstrap) {
        syncConfigurationBootstrap.setName("sync");
    }

    @Override
    public void run(SyncConfiguration syncConfiguration, Environment environment) throws Exception {
        environment.addResource(new IsSyncedResource(syncConfiguration.getFolder()));
    }
}
