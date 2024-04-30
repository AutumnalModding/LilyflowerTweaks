This mod has one main purpose: to fix the DragonAPI/LOTR incompatibility preventing your game from launching.

It does so by removing `LOTRClassTransformer#patchBlockFire` via cursed transformer reflection; as such, with this mod installed, you will be able to launch the game with both DragonAPI (and thus also any of Reika's other mods), *and* the LOTR mod.

Additionally, however, it provides several 'opinionated' tweaks to the LOTR mod; all of which are configurable - and disabled by default.

Originally these tweaks were simple bytecode edits I made myself, but as I found more and more things I wanted to change it quickly became cumbersome to have to open Recaf every time - and I wouldn't be able to distribute my edited copy.

So instead I switched to using mixins and using a JSON-based config file to allow for maximal configurability.

CURRENTLY INCLUDED TWEAKS:
- Combat:
    - Disable the 1.9 style combat system
    - Disable NPCs attacking on sight
    - Make all NPCs attack on sight, regardless of alignment
    - Make any entity a valid NPC target - even other NPCs of the same faction!
    - Allow killing the Grey Wanderer
- Travel:
    - Disable alignment-based waypoint locking
    - Automatically unlock all waypoints
    - Disable fast travel entirely
- Misc:
    - Unlock all player-specific shields & titles
