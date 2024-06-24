![gameimg](https://cloud.githubusercontent.com/assets/20020612/21897521/d11b18d2-d8b7-11e6-8659-4012fe720614.png)

## Game Of Life
A small simulator of the [Conway's Game](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life) coded in Java.

## Technologies :fire:

- [JavaFX 21.0.3](https://openjfx.io/) - as GUI
- [Maven 3.9.6](https://maven.apache.org) - as a template

## Run it! :rocket:
First, this app uses this [Maven plugin](https://github.com/openjfx/javafx-maven-plugin) to manage all related to JavaFX:

- I recommend use **IntelliJ IDE** in case you have problems to download all the dependencies with **Eclipse IDE**.
- Install Maven dependencies.
- Use `mvn clean javafx:run` to run in localhost.
- Use `mvn clean javafx:jlink` to generate a executable (read the docs of the plugin if you aren't sure).

## How to play?
- Use left-click to place a cell (green cell).
- Once you think it's ready just press down the "Start Game" button.

## Demo :sunglasses:
- Simple demonstration in [YouTube](https://www.youtube.com/watch?v=Kh5HhEx9gj0)

## References :books:
- [JavaFX Official Doc](https://openjfx.io/openjfx-docs/)
- [Epic patterns in Conway's game](https://www.youtube.com/watch?v=C2vgICfQawE)
- [Run JavaFX app with Maven plugin](https://github.com/openjfx/javafx-maven-plugin?tab=readme-ov-file#javafxrun-options)
- [Create executable file with Maven plugin](https://github.com/openjfx/javafx-maven-plugin?tab=readme-ov-file#javafxjlink-options)