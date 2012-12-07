package su.litvak.photosync;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Admin
 * Date: 8/12/12
 * Time: 12:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class SyncConfiguration extends Configuration {
    @NotEmpty
    @JsonProperty
    private String folder = System.getProperty("user.home") + File.separator + "SuperCoolPhotoSync Photos";

    public String getFolder() {
        return folder;
    }
}
