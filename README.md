# Nursultan Open Source (clean starter) — Minecraft 1.21.1

This repository is a **new, minimal Fabric client-mod project**, not a decompilation
or a cleaned/rebuilt copy of the uploaded proprietary JAR.

## Target
- Minecraft Java Edition: 1.21.1
- Java: 21
- Fabric Loader: 0.18.4
- Fabric API: 0.116.6+1.21.1 (required: the mod declares it as a dependency, so the
  matching Fabric API JAR must also be in the instance's `mods` folder)
- Gradle: 9.2.1 (used by the GitHub workflow; Fabric Loom 1.14 needs a recent Gradle)
- Client-side Fabric mod; designed to use the normal Fabric `mods/` directory.

## Mod support
Fabric Loader loads compatible Fabric mods from the instance's `mods` folder.
Install only mods that explicitly support Minecraft 1.21.1 and your Fabric Loader/API
versions. Check each mod's source and license. Do not put random JAR files into the
instance.

## Zalith Launcher
Use a Minecraft 1.21.1 Fabric instance with Java 21. Put this project's built JAR
and other compatible Fabric mod JARs in that instance's `mods` directory.
Zalith behavior depends on the device, renderer, launcher build and each mod.
This project has not been runtime-tested on the user's phone, so compatibility is
not guaranteed by this source archive.

## Build
This archive does not include the Gradle wrapper (`gradlew`), so use an installed
Gradle, or generate the wrapper once (recommended, see below).

On a computer with JDK 21, Gradle 9.2.1 and internet access:

```sh
git clone <your-repository-url>
cd <your-repository-folder>
gradle build
```

The finished mod is `build/libs/nursultan-opensource-1.21.1-0.1.0.jar`
(ignore the `-sources.jar` file).

Recommended: generate the wrapper once. Do it in an empty temporary folder, so the
project's plugins are not loaded by a possibly incompatible Gradle, then copy it in:

```sh
mkdir -p ~/wrapper-tmp && cd ~/wrapper-tmp
gradle wrapper --gradle-version 9.2.1
cp -r gradlew gradlew.bat gradle ~/<project-folder>/
cd ~/<project-folder>
./gradlew build
```

Commit `gradlew`, `gradlew.bat` and `gradle/` to your repository afterwards.

On Termux, install the required tools:

```sh
pkg update
pkg install openjdk-21 gradle git unzip zip
java -version
gradle --version
```

Create the wrapper once as shown above; after that always build with
`./gradlew build`, which downloads exactly Gradle 9.2.1 and avoids Gradle-version
problems with the Termux package. If you prefer not to use the wrapper, install Gradle
from its official distribution and configure `PATH`. Do not run an
unknown downloaded installer script. The first build downloads Minecraft, mappings and
Fabric dependencies, so it needs a stable connection and a few GB of free space.

### Build on GitHub instead of the phone
`.github/workflows/build.yml` builds the project on GitHub Actions for every push.
Download the JAR from the run's "Artifacts" section.

## Security design
- No shell command execution.
- No telemetry, Discord auto-open, remote config, token collection, or credential access.
- No embedded binary mods or native libraries.
- Third-party mods remain separate files in `mods/` and must be reviewed independently.

## Important
The uploaded JAR declares a proprietary license. This repository intentionally
contains only newly written starter code and does not redistribute or claim
ownership of that JAR or its bundled third-party components. Obtain permission
and review each dependency's license before publishing any recovered code/assets.

## In-game usage
The mod has a dark category menu, per-module settings, key binds and a saved config.
Everything is client-side (nothing is sent to the server).

**Menu:** press **Right Shift** (keyboard) to open or close it, or type `/nur gui`
(works on touch screens, e.g. Zalith Launcher).
- One panel per category (Combat, Movement, Player, Visual).
- Tap a module name to toggle it (purple = ON). Tap the dots on its right for its settings.
- Settings screen: `-` / `+` change numbers, other rows switch on/off or cycle options.
- `Bind: ...` row: tap it, then press a key to bind the module (Esc cancels, Delete clears).
  If the launcher does not support key polling, use `/nur bind <module> <key>` instead.

**Config:** saved automatically to `config/nursultan_opensource.properties`
(module states, binds and settings). Combat modules always start disabled.

**Commands:**
- `/nur gui` - open the menu
- `/nur list` - show all modules and whether they are ON or OFF
- `/nur toggle <module>` - toggle a module, e.g. `/nur toggle AutoSprint`
- `/nur settings <module>` and `/nur set <module> <setting> <value>` - change settings
- `/nur bind <module> <key>` - bind a key, e.g. `/nur bind AutoSprint R` (`none` removes it)
- `/nur coords` - print your coordinates in chat

**Combat:** `AttackAura`, `TriggerBot`, `AimAssist`, `AutoTotem`, `ElytraTarget`, `ElytraHelper`.
**Movement:** `AutoSprint`, `AutoWalk`, `AutoJump`.
**Player:** `AntiAFK`, `AutoRespawn`, `AutoEat`, `DeathCoords`.
**Visual:** `Fullbright`, `InfoHUD`, `TargetInfo`.

Only use the combat modules on servers where you have permission.

To add a module, extend `dev.share.nursultan.module.Module`, give it a `Category`,
add settings with `addSetting(...)` and register it in `NursultanClient`.
