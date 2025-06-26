# Angry Birds

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and an `ApplicationAdapter` extension that draws libGDX logo.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.

# HOW TO RUN THE PROGRAM
# STATIC GUI
To be able to run the program, open the Lwjgl3Launcher class, which can be found in the lwjgl3 folder, within src -> main -> java -> io.Pookies.fairies.lwjgl3.
When the program will be run, the main screen will appear, where the user has to click on the 'Play' button to move to the levels screen.
In the levels screen, only the first level will be unlocked, which when clicked will take the user to the Level 1 screen. In that screen,
since the game functionalities haven't been programmed yet, the user will see 2 buttons, 'Success!' and 'Failure!', which can be clicked to view
the success and failure screens. In the success screen menu, the user will have the option to exit the game, return to the levels screen, or directly move
on to the next level. Based on what the user chooses, they will be directed to that respective page. Similarly, in the failure screen menu, the user will have
the option to exit the game, return to the levels screen, or retry the level. Based on their choice, they will be directed to that screen accordingly.
Furthermore, in all the level screens, on the top right corner there will be a pause button, which when clicked will take the user to pause menu, where the user
will have the choice to continue the game where they left off, restart, go back to the levels screen, or exit the game entirely. Again, based on their choice,
the user will get directed to that page. By doing this, the program can be tested and ensured that all the screens and the transitions between them through the
use of buttons are working properly or not.

# FUNCTIONING GAME
# STATIC GUI
To be able to run the program, open the Lwjgl3Launcher class, which can be found in the lwjgl3 folder, within src -> main -> java -> io.Pookies.fairies.lwjgl3.
When the program will be run, the main screen will appear, where the user has to click on the 'Play' button to move to the levels screen.
In the levels screen, only the first level will be unlocked, which when clicked will take the user to the Level 1 screen. In that screen,
since the game functionalities haven't been programmed yet, the user will see 2 buttons, 'Success!' and 'Failure!', which can be clicked to view
the success and failure screens. In the success screen menu, the user will have the option to exit the game, return to the levels screen, or directly move
on to the next level. Based on what the user chooses, they will be directed to that respective page. Similarly, in the failure screen menu, the user will have
the option to exit the game, return to the levels screen, or retry the level. Based on their choice, they will be directed to that screen accordingly.
Furthermore, in all the level screens, on the top right corner there will be a pause button, which when clicked will take the user to pause menu, where the user
will have the choice to continue the game where they left off, restart, go back to the levels screen, or exit the game entirely. Again, based on their choice,
the user will get directed to that page. By doing this, the program can be tested and ensured that all the screens and the transitions between them through the
use of buttons are working properly or not.

# FUNCTIONING GAME
To be able to run the program, open the Lwjgl3Launcher class, which can be found in the lwjgl3 folder, within src -> main -> java -> io.Pookies.fairies.lwjgl3.
When the program will be run, the main screen will appear, where the user has to click on the 'Play' button to move to the levels screen. Here the user will have
the first level be unlocked and the remaining two levels will be locked which can only be unlocked if the previous level before it has been completed. The goal
of the game is to kill all the pigs before all the birds available to you in that level are exhausted. Level 1 has only 1 bird who has to kill 1 pig, level 2 has
3 birds who have to kill 2 pigs, and in level 3, there are 3 available birds who have to kill 2 pigs. Other than these 3 levels, we have added a bonus component
called the special level. Here the user can play any randomly chosen level, hence enabling infinite gameplay. Furthermore, the pause, success, and failure screens
will also appear based on whether the user wins or loses the game, and the pause screen will appear whenever the user presses the pause button. The user also has
the option to exit the game whenever they want by pressing the exit button. 

# ONLINE REFERENCES
https://angrybirds.fandom.com/wiki/Stone
https://angrybirds.fandom.com/wiki/Minion_Pigs/Angryverse/Angry_Birds_Space
https://ar.pinterest.com/pin/955748352143825885/
https://angrybirds.fandom.com/wiki/Star
https://www.pikpng.com/transpng/TmRhhJ/
https://www.youtube.com/watch?v=NuZM5_4xjrU
https://www.spriters-resource.com/mobile/angrybirdspace/sheet/166300/

