# MyShelfie
The project assigned was to implement the board game **"MyShelfie"** in Java with a Model-View-Controller pattern.  
![](src/main/resources/gui/myShelfieImages/publisher_material/Display_1.jpg)
## Development
The software was developed using:
- Oracle OpenJDK version 17.0.6
- Maven version 3.8.1
- JavaFX version 19.0.2.1
- IntelliJ - IDE
- SceneBuilder - GUI

## Requirements
The application requires at least Java 8 or later versions to run.

## How to run it

### Server
` java -jar MyShelfie.jar server [{hostName} {rmiPort} {socketPort}] `

If the user omits the RMI and the Socket port the default ports will be automatically used.

### Client
` java -jar MyShelfie.jar client {cli | gui} {tcp | rmi} {hostName} {numberPort} ` 

If the user omits the hostname and the number port the default settings will be automatically used.

## Objectives Achieved
### Basic functionalities:
- [x] Complete rules
- [x] CLI
- [x] GUI
- [x] RMI
- [x] Socket

### Advanced functionalities:
- [x] Chat
- [X] Multiple games
- [ ] Persistence
- [ ] Resilience to disconnections

## Authors
- Lorenzo Benedetti
- Vincenzo De Masi
- Nicola Bomben
- Pietro Massari

## License
The graphic material was licenced by Cranio Games.
