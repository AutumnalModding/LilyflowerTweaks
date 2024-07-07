This mod includes many tweaks - primarily for the LOTR mod, but other mods are included as well.
Additionally included are some cross-mod bugfixes.
Any changes that would modify the vanilla experience of any mod are disabled by default.

# LOTR features

### Format: `featureFlag - featureName`

## Combat
- `removeNewCombat` - Disable 1.9-style combat. Off by default.
- `enableGWDeath` - Enable Grey Wanderer death. Off by default.
- `enableWarCrimes` - All factions support war crimes. Off by default.
- `enableOmnitarget` - LOTR NPCs attack anything. Off by default.
- `additionalCombatItems` - Items to add to the LOTR combat system. Empty by default.

## Generic
- `unlockCosmetics` - Unlocks all 'exclusive' LOTR titles & shields. Off by default.
- `fixOreDictionary` - Adds Mithril to the oredict. On by default.
- `timeMultiplier` - Day length multiplier for LOTR days. Default is 1x (no change).
- `allowOddmentScreenshots` - Makes the Oddment Collector show up in screenshots. Off by default.

## Integration
### All of these are on by default.
- `enableThaumIntegration` - Self-explanatory. Adds:
  - Mithril caps (15% better than void metal)
  - Morgul caps (same as void metal; but Perditio and Ignis 35% better)
  - Mallorn rods (slightly better silverwood - 150 vs 100)
  - Charred rods (same as silverwood, regen Ignis vis)
  - Mirk-oak rods (ditto, but Terra)
  - Mallorn staff cores (a whopping 280 vis!)
  - Mithril-trimmed thaumaturge's robes (7% vis discount; boots 3%)
  - NOTE: all items are missing textures! Robes use the regular ones, for now.

- `enableWitcheryIntegration` - Self-explanatory. This toggle is useless, for now.
- `fixVampireRitual` - Makes Witchery's vampire ritual work in the LOTR dim. Go take Elle to Mount Doom.
- `safeBiomes` - Allows specified LOTR biomes to count as "not in sunlight" (like Mordor, which has no sun.) Empty by default.

## Fast Travel
- `disabledWaypoints` - List of waypoints to disable. Run `/ltdebug dumpWaypoints` for a list. Empty by default.
- `unlockAllWaypoints` - Self-explanatory. Off by default.
- `disableWaypointLocking` - Disables alignment-based waypoint locking. Off by default.

## Bugfixes
### None of these can be turned off. Why would you want to??
- Fix Witchery bat form being a black screen in first-person.
- Fix DragonAPI incompatibility. Why does LOTR even patch `BlockFire`???

## Custom Content
### Factions
File: File: `config/lotweakr/lotr/factions/<NAME>.txt`
```
CoolFac
FF00FF
WEST
10 10 50
ELF
true
false
true

# Lines, in order:
# Name
# Colour
# Alignment region
# Map region
# Faction type (FREE, ELF, MAN, DWARF, ORC, TROLL, TREE)
# War crimes true/false
# Isolationist true/false
# Player ranks true/false

# Lines after the first 8 are ignored if they aren't either of:
# Control zones: CONTROL <waypoint> <region>
# Ranks: RANK <title> <alignment> <pledge> <chat title> <achievement>
# (Achievement creation is unimplemented due to a crash bug)
# The first eight lines MUST BE as above - the parser is line-number-based.
# Isolationism seems related to conquest bonuses.

# Strings used for ranks and the title are localization keys.
# For the title: 'lotr.faction.<TITLE>.name'
# For the ranks: 'lotr.faction.<TITLE>.rank.<RANK>'

CONTROL MENELTARMA_SUMMIT 50
RANK CoolGuy 100 false true false
RANK EpicGuy 250 true true false
```

### Invasions
File: `config/lotweakr/lotr/invasions/<NAME>.txt`
```
EpicFactionInvasion
CoolFac

LOTREntityGandalf 20
LOTREntityBalrog 50
LOTREntityBandit 25
```

### Relations
File: `config/lotweakr/lotr/relations.txt`
These are one-way!
```
CoolFac ALLY MORDOR
MORDOR ALLY CoolFac
HOBBIT FRIEND CoolFac
GONDOR ENEMY CoolFac
```
