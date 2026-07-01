# Visor Plugin

_Crafted by VR enthusiasts for players and developers alike_

- - - - - - -

The **server-side plugin** to the [Visor](https://github.com/VisorModStudio/Visor)

> Network protocol: **v4**<p>
> Plugin messaging channel: **`visor:channel`**

- - - - - - -

## Requirements
- A **Visor**-modded client to connect in VR
- A server on a supported core (see below)
- Java version chosen by your Minecraft version (1.20.1-1.20.4 → Java 17, 1.20.5-1.21.11 → Java 21). Plugin itself is compiled to Java 17 bytecode, so a single jar loads on Java 17, 21 and 25

## Supported platforms
| Core | Support    |
|------|------------|
| **Paper** | ✅ Works    |
| **Spigot / CraftBukkit** | ✅ Works    |
| **Folia** | ✅ Works    |
| BungeeCord / Velocity | 🚧 Planned |

## Installation
1. Download / build `VisorPlugin-<version>.jar`
2. Put it in your server's `plugins/` folder
3. Restart the server
4. On first run it generates `plugins/VisorPlugin/server_settings.yml`

## Commands & permissions
| Command | Description |
|---------|-------------|
| `/visor reload` | Reload `server_settings.yml` and push the new settings to connected clients |
| `/visor status` | Show how many sessions are connected and how many are in VR |

Permission: **`visor.admin`** (default: ops)

## Configuration
`plugins/VisorPlugin/server_settings.yml`:

| Key                     | Default      | Description                                                             |
|-------------------------|--------------|-------------------------------------------------------------------------|
| `serverDebug`           | `false`      | Verbose logging of Visor                                                |
| `vrOnly`                | `false`      | Kick non-VR players (ops exempt)                                        |
| `twoHandedVR`           | `false`      | Enable the off-hand slot for two-handed VR                              |
| `betterSwinging`        | `false`      | VR-accurate melee attacks and block breaking                            |
| `swingingRepairDelay`   | `400`        | Ticks before an unfinished VR block break "heals"                       |
| `roomCrawlingSupported` | `false`      | Allow room-scale crawling (forced crawl/swim pose)                      |
| `roomClimbingSupported` | `false`      | Allow room-scale climbing (resets fall distance)                        |
| `pvpVRvsVanilla`        | `true`       | Allow PvP between a VR player and a non-VR player                       |
| `pvpVRvsVR`             | `true`       | Allow PvP between two VR players                                        |
| `notifyPvpBlocked`      | `false`      | Message the attacker when PvP is blocked                                |
| `creeperSwellDistance`  | `1.75`       | Creepers swell within this distance of a VR player's head. (`0` disables) |
| `supportedMovement`     | `CONTROLLER` | `CONTROLLER`, `TELEPORT`, or `BOTH`                                     |
| `teleportUpLimit`       | `1`          | Teleport-move vertical-up limit (blocks)                                |
| `teleportDownLimit`     | `4`          | Teleport-move vertical-down limit (blocks)                              |
| `teleportForwardLimit`  | `16`         | Teleport-move forward limit (blocks)                                    |
| `trackersSupported`      | `false`       | Allow to use trackers                                                   |

```bash
./gradlew build
# → build/libs/VisorPlugin-<version>.jar
```

### Adding support for another Minecraft version
1. Create an `nms-vXXX` module applying `paperweight-userdev` with that version's dev bundle
2. Implement `VisorVersionAdapter` (copy an existing leaf; fix any API differences)
3. Register the class in `VersionAdapterRegistry.NMS_LEAVES` and add `runtimeOnly project(path: ':versions:nms-vXXX', configuration: 'reobf')` to the root build

## Notes
- **ViaVersion** is supported for cross-version play: `visor:channel` carries a version-independent byte
  format, so a 1.20.1 Visor client can play on a newer server via ViaVersion
- ...

- - - - - - -

Join our community: [Discord](https://discord.gg/wJX8sTDEdx)
