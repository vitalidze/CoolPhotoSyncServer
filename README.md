CoolPhotoSyncServer
===================

Server side for [iPhone photo sync app](https://github.com/vitalidze/osx-project-2)

### Roadmap

#### v 0.0.1

* RESTful web service with following resources

     * is_synced - checks whether file with specified name already exists in destination folder
     * sync - receives uploaded multiparty stream and saves it to appropriate sub folder in destination folder

* tray icon with options:

     * change folder - opens the directory opening dialog, where the destination folder for assets must be selected
     * change port - opens dialog with port selection. Restarts server along with port configuration saving.
     * exit - stops application

* package into single jar. Distribute either via web start or executable jar