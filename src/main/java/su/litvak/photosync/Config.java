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

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class Config {
    private final static String CONFIG_FILE = System.getProperty("user.dir") + File.separatorChar + "config.json";
    private Logger logger = LoggerFactory.getLogger(Config.class);

    /**
     * Path to the folder, where synced photos will be saved to
     */
    private String picturesPath;

    /**
     * Port, where to bind HTTP server
     */
    private int port;

    private final static Config INSTANCE = new Config();

    public static Config get() {
        return INSTANCE;
    }

    private Config() {
        if (new File(CONFIG_FILE).exists()) {
            load();
        } else {
            initDefaultValues();
            save();
        }
    }

    public String getPicturesPath() {
        return picturesPath;
    }

    public void setPicturesPath(String picturesPath) {
        this.picturesPath = picturesPath;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Will create folder set up in config if it doesn't exist
     *
     * @return  file descriptor of the folder to save synced photos to, never null
     */
    @JsonIgnore
    public File getPicturesFolder() {
        File picturesDir = new File(getPicturesPath());
        if (!picturesDir.exists())  {
            picturesDir.mkdir();
        }
        return picturesDir;
    }

    public void load() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.readerForUpdating(this).readValue(new File(CONFIG_FILE));
        } catch (Exception ex) {
            logger.error("Error occurred while loading config file", ex);
        }
    }

    public void save() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(CONFIG_FILE), this);
        } catch (Exception ex) {
            logger.error("Error occurred while saving config file", ex);
        }
    }

    private void initDefaultValues() {
        File picturesDir = new File(System.getProperty("user.home") + File.separatorChar + "Pictures");
        /**
         * Create picture directory named 'Synced Pictures' if 'Pictures' is not a directory
         */
        if (picturesDir.exists() && !picturesDir.isDirectory()) {
            picturesDir = new File(picturesDir.getParentFile(), "Synced Pictures");
        }
        /**
         * Set up picturesPath
         */
        setPicturesPath(picturesDir.getAbsolutePath());

        /**
         * Set port
         */
        setPort(8081);
    }
}
