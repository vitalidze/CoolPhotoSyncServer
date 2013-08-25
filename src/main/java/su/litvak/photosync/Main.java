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
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class Main {
    private static Server server;
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        /**
         * Init logging bridge
         */
        initLogging();

        /**
         * Init tray
         */
        initTray();

        /**
         * Start jetty
         */
        start();
    }

    /**
     * Configures JUL to slf4j bridge
     */
    private static void initLogging() {
        java.util.logging.Logger rootLogger =
                java.util.logging.LogManager.getLogManager().getLogger("");
        java.util.logging.Handler[] handlers = rootLogger.getHandlers();
        for (int i = 0; i < handlers.length; i++) {
            rootLogger.removeHandler(handlers[i]);
        }
        SLF4JBridgeHandler.install();
    }

    private static void initTray() {
        if (!SystemTray.isSupported()) {
            logger.warn("System tray is not supported");
            return;
        }

        URL imageURL = Main.class.getResource("/disk_silver_sync.png");
        Image trayImage = null;

        if (imageURL == null) {
            logger.error("Tray icon image not found");
            return;
        } else {
            trayImage = (new ImageIcon(imageURL, "Tray Icon")).getImage();
        }

        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon = new TrayIcon(trayImage);
        final SystemTray tray = SystemTray.getSystemTray();

        final MenuItem changePath = new MenuItem("Change folder (" + Config.get().getPicturesFolder() + ")...");
        final MenuItem changePort = new MenuItem("Change port (" + Config.get().getPort() + ")...");
        MenuItem exitItem = new MenuItem("Exit");

        popup.add(changePath);
        popup.add(changePort);
        popup.addSeparator();
        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("Cool Photo Sync Server");

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            logger.error("TrayIcon could not be added.");
            return;
        }

        changePath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Config config = Config.get();

                JFileChooser fileChooser = new JFileChooser(config.getPicturesFolder());
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setDialogTitle("Select folder for synced photos");

                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    config.setPicturesPath(fileChooser.getSelectedFile().getAbsolutePath());
                    config.save();
                    changePath.setLabel("Change folder (" + config.getPicturesPath() + ")...");

                }
            }
        });

        changePort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Config config = Config.get();

                JLabel lblPort = new JLabel("Port to bind sync server on");
                JSpinner spPort = new JSpinner(new SpinnerNumberModel(config.getPort(), 80, 65535, 1));

                int dialogResult = JOptionPane.showOptionDialog(null,
                        new JComponent[] {lblPort, spPort},
                        "Set up sync server port",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        null);

                int enteredPort = ((Integer) spPort.getValue()).intValue();

                if (dialogResult == JOptionPane.OK_OPTION && enteredPort != config.getPort()) {
                    config.setPort(enteredPort);
                    config.save();
                    changePort.setLabel("Change port (" + config.getPort() + ")...");
                    try {
                        restart();
                    } catch (Exception ex) {
                        logger.error("Unable to restart server on port " + enteredPort, ex);
                    }
                }
            }
        });

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    stop();
                    System.exit(0);
                } catch (Exception ex) {
                    logger.error("Unable to stop application", ex);
                    System.exit(1);
                }
            }
        });
    }

    private static void start() throws Exception {
        logger.info("Starting server...");
        server = new Server(Config.get().getPort());
        ServletContextHandler context = new ServletContextHandler();
        ServletHolder servletHolder = new ServletHolder(new ServletContainer());
        servletHolder.setInitParameter("javax.ws.rs.Application", SyncApplication.class.getName());
        context.addServlet(servletHolder, "/*");
        server.setHandler(context);
        server.start();
    }

    private static void stop() throws Exception {
        logger.info("Stopping server...");
        server.stop();
        server = null;
    }

    private static void restart() throws Exception {
        stop();
        start();
    }
}
