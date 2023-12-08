# SimpleTP
A simple teleport manager plugin for Minecraft, minimalistic and functional.

## Commands
SimpleTP manages three kinds of teleports: **home**, **warp** and **player**, each having their own cooldown.
### Home
Players can set a single location as their personal home. Homes are private and exclusively accessible to the owner.
- `/sethome` set the current location as home
- `/home` teleport to home
### Warp
Admins can set locations as warps, assigning each a unique name. Warps are public and can be directly accessed by all players using their warp names.
- `/setwarp <warp>`: **Admin Only** set the current location as a warp with name `<warp>`
- `/delwarp <warp>`: **Admin Only** delete the warp with name `<warp>`
- `/warp <warp>`: teleport to the warp with name `<warp>`
### Player
Players can send a teleport request to another player. The recipient can either accept or deny the request. A player can only have one incoming one outgoing request at a time. Pending requests expire after a set duration.
- `/tpr <player>`: send a teleport request to the player with name `<player>`
- `/tpa`: accept an incoming teleport request
- `/tpd`: deny an incoming teleport request

## Installation
Make sure you have a spigot server ready. Download the jar file from release and place it in the `plugins/` folder. Alternatively, you can also clone the repo and build the jar from source.

## Configuration
After having started the server at with once with SimpleTP installed, the directory `SimpleTeleport/` containing `config.yml` and `SimpleTP.db` will appear under `plugins/`. All locations, requests and cooldowns are stored in `SimpleTP.db`, an SQLite database. Cooldowns and messages can be configured inside `config.yml`.
