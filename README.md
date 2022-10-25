# Reforging

### NEW! For a fully featured custom configuration generator visit its website https://aregcraft.github.io/reforging-site/.

Reforging plugin adds a new mechanic for enhancing weapons with various stat boosts and abilities. It is completely
customizable with options to add custom reforges, heavily tweak existing abilities, and change the looks of items.

![Storm Ability](https://github.com/Aregcraft/reforging/blob/master/screenshots/storm_ability.png)
![Throw Ability](https://github.com/Aregcraft/reforging/blob/master/screenshots/throw_ability.png)
![Sword on an Anvil](https://github.com/Aregcraft/reforging/blob/master/screenshots/sword_on_anvil.png)
![Axe on an Anvil](https://github.com/Aregcraft/reforging/blob/master/screenshots/axe_on_anvil.png)
![Fire Ability](https://github.com/Aregcraft/reforging/blob/master/screenshots/fire_ability.png)
![Shield Ability](https://github.com/Aregcraft/reforging/blob/master/screenshots/shield_ability.png)

## Config

**If you are not familiar with JSON, you may use online YAML to JSON converters,
for example [https://onlineyamltools.com/convert-yaml-to-json](https://onlineyamltools.com/convert-yaml-to-json).**

Specify colors and placeholders with `%COLOR_NAME%` or `%PLACEHOLDER_NAME%` (e.g., `%DARK_BLUE%`, `%REFORGE_NAME%`).

### item.json

Configures the visuals of reforged items.

#### name: string

The name of each reforged item.

Colorized, placeholders:
- REFORGE_NAME - The name of the reforging (e.g., "Sharp", "Shielded")
- NAME - THe name of the item (e.g., "Diamond Sword")

#### lore: string array

The lore attached to the end of each reforged item.

Colorized, placeholders:
- **BASE_ATTACK_SPEED** - The base attack speed of the weapon
- **ATTACK_SPEED** - The boost of attack speed provided by the reforging
- **BASE_ATTACK_DAMAGE** - The base attack damage of the weapon
- **ATTACK_DAMAGE** - The boost of attack damage provided by the reforging
- **MAX_HEALTH** - The boost of max health provided by the reforging
- **KNOCKBACK_RESISTANCE** - The boost of knockback resistance provided by the reforging
- **MOVEMENT_SPEED** - The boost of movement speed provided by the reforging
- **ARMOR** - The boost of armor provided by the reforging
- **ARMOR_TOUGHNESS** - The boost of armor toughness provided by the reforging
- **ATTACK_KNOCKBACK** - The boost of attack knockback provided by the reforging

```json
{
  "name": "%RESET%%REFORGE_NAME% %NAME%",
  "lore": [
    "",
    "%GRAY%When in main hand:",
    "%DARK_GREEN% %BASE_ATTACK_SPEED% %DARK_BLUE%(%ATTACK_SPEED%)%DARK_GREEN% Attack Speed",
    "%DARK_GREEN% %BASE_ATTACK_DAMAGE% %DARK_BLUE%(%ATTACK_DAMAGE%)%DARK_GREEN% Attack Damage",
    "%DARK_BLUE% %MAX_HEALTH% Max Health",
    "%DARK_BLUE% %KNOCKBACK_RESISTANCE% Knockback Resistance",
    "%DARK_BLUE% %MOVEMENT_SPEED% Movement Speed",
    "%DARK_BLUE% %ARMOR% Armor",
    "%DARK_BLUE% %ARMOR_TOUGHNESS% Armor Toughness",
    "%DARK_BLUE% %ATTACK_KNOCKBACK% Attack Knockback"
  ]
}
```

### anvil.json

Configures the visuals and mechanics of the reforging anvil.

#### name: string

The name of the reforging anvil item.

Colorized, no placeholders.

#### lore: string array

The lore of the reforging anvil item.

Colorized, no placeholders.

#### recipe: object

##### shape: string array

The shape of the crafting recipe for the reforging anvil item. 

3 strings, each consisting of 3 symbols representing items as per `keys`.

##### keys: object

Maps single symbols to their respective item. Item should be specified with all CAPS and without `minecraft:`.

#### soundEffect: object

Specifies the volume and pitch of the sound (`BLOCK_ANVIL_USE`) played whenever a player performs reforging.

#### price: integer

Specifies the amount of respective material (e.g., diamonds for diamond sword, iron ingots for iron axe) required
to perform reforging.

```json
{
  "name": "%DARK_PURPLE%Reforge Anvil",
  "lore": [
    "%GRAY%Place this anvil anywhere in the world",
    "%GRAY%to start reforging stuff!"
  ],
  "recipe": {
    "shape": [
      "ooo",
      "dad",
      "iii"
    ],
    "keys": {
      "o": "OBSIDIAN",
      "d": "DIAMOND",
      "a": "ANVIL",
      "i": "IRON_BLOCK"
    }
  },
  "soundEffect": {
    "volume": 1,
    "pitch": 0
  },
  "price": 1
}
```

### reforges.json

This file allows you to create as many unique reforges as you wish.

#### name: string

The name of the reforging, it corresponds to the `REFORGE_NAME` label.

#### ability: string

The name of the ability of the reforging as per `abilities.json`.

#### [attribute]: float

These are all the attributes, each corresponding to their respective stat and placeholder.

- **attackSpeed**
- **attackDamage**
- **maxHealth**
- **knockbackResistance**
- **movementSpeed**
- **armor**
- **armorToughness**
- **attackKnockback**

#### weight: int

Represents the chance of this particular reforge being applied to the weapon. This is not a percentage,
but rather relative weight, which means that there are no restrictions on the upper bound of the number.

```json
[
  {
    "name": "Shielded",
    "ability": "SHIELD",
    "maxHealth": 4,
    "attackDamage": -1,
    "armor": 4,
    "knockbackResistance": 2,
    "weight": 40
  },
  {
    "name": "Sharp",
    "ability": "DUMMY",
    "maxHealth": -2,
    "attackDamage": 4,
    "attackSpeed": 0.2,
    "weight": 80
  },
  {
    "name": "Infernal",
    "ability": "FIRE",
    "weight": 20
  },
  {
    "name": "Murderous",
    "ability": "THROW",
    "attackDamage": 2,
    "attackSpeed": 0.5,
    "movementSpeed": 0.1,
    "weight": 20
  },
  {
    "name": "Enraged",
    "ability": "STORM",
    "attackDamage": 3,
    "attackSpeed": 0.3,
    "weight": 20
  }
]
```

### abilities.json

There are currently only 2 abilities but more will be added soon. Each ability has many customizable options.
This file allows creating multiple abilities with their own names and tweaks to the 2 base built-in abilities.

#### shieldAbilities: object

This is the first base ability. It gives player damage resistance for a specified period of time. You can have
multiple abilities inheriting from this with different options and names.

##### price: object

Specifies the price the player pays when activates this ability.

###### health: double

Specifies the amount of health subtracted from the player when activating the ability.

###### food: integer

Specifies the amount of food subtracted from the player when activating the ability.

##### particle: string

Specifies the type of the particle which is used to form a circle around the player. All CAPS, without `minecraft:`.
**Visit this page for the types of the particles [https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Particle.html](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Particle.html)**.

##### particleFrequency: double

Specifies the "frequency" of the particles. The "frequency" is used to determine the step angle, which is 180 divided
by the "frequency". The higher the number is, The further apart will the particles be.

##### radius: double

Specifies the radius of the circle formed around the player.

##### duration: integer

Specifies the duration of the shield in ticks **(1 second = 20 ticks)**.

##### disableAttack: boolean (true/false)

Specifies whether the player should be prevented from attacking other entities while the shield is active.

#### fireAbilities: object

This is the second base ability. It forms multiple circles in the direction where the player is facing, each further
from the player, and with bigger radius. If any entity hits the particle, then that entity will be set on fire.

##### price: object

Specifies the price the player pays when activates this ability.

###### health: double

Specifies the amount of health subtracted from the player when activating the ability.

###### food: integer

Specifies the amount of food subtracted from the player when activating the ability.

##### particle: string

Specifies the type of the particle which is used to form circles. All CAPS, without `minecraft:`.
**Visit this page for the types of the particles [https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Particle.html](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Particle.html)**.

##### particleFrequency: double

Specifies the "frequency" of the particles. The "frequency" is used to determine the step angle, which is 180 divided
by the "frequency". The higher the number is, The further apart will the particles be.

##### circleDistance: double

Specifies the distance between each circle.

##### radius: double

Specifies the radius of the smallest circle.

##### fireRange: double

Specifies the maximum distance from a particle which will set an entity on fire.

##### circleCount: integer

Specifies the count of the circles. Each next circle is bigger than the previous one.

##### fireDuration: integer

Specifies how long will hit entities be on fire in ticks **(1 second = 20 ticks)**.

##### circlePeriod: integer

Specifies how much time after forming a circle should pass to form the next one in ticks **(1 second = 20 ticks)**.

#### throwAbilities: object

This is the third base ability. It allows the player to throw sword which will disappear after hitting a solid block
or travelling the maximum specified distance and damage all the entities it hits on the way.

##### price: object

Specifies the price the player pays when activates this ability.

###### health: double

Specifies the amount of health subtracted from the player when activating the ability.

###### food: integer

Specifies the amount of food subtracted from the player when activating the ability.

##### maxDistance: double

Specifies the maximum distance that the weapon travels before disappearing.

##### damageAmplifier: double

Specifies how the amplifier applied to the weapons usual damage when thrown.

##### speed: double

Specifies the speed of the weapon when thrown in blocks.

##### range: double

Specifies the maximum distance from the weapon which will damage an entity.

#### stormAbilities: object

This is the fourth base ability. It allows the player to strike a circle of lighting around them.

##### price: object

Specifies the price the player pays when activates this ability.

###### health: double

Specifies the amount of health subtracted from the player when activating the ability.

###### food: integer

Specifies the amount of food subtracted from the player when activating the ability.

##### radius: double

Specifies the radius of the circle formed around the player.

##### frequency: double

Specifies the "frequency" of the lighting. The "frequency" is used to determine the step angle, which is 180 divided
by the "frequency". The higher the number is, The further apart will the lighting be.

```json
{
  "shieldAbilities": {
    "SHIELD": {
      "price": {
        "health": 6,
        "food": 6
      },
      "particle": "ENCHANTMENT_TABLE",
      "particleFrequency": 9,
      "radius": 1,
      "duration": 60,
      "disableAttack": true
    }
  },
  "fireAbilities": {
    "FIRE": {
      "price": {
        "health": 4,
        "food": 4
      },
      "particle": "FLAME",
      "particleFrequency": 9,
      "circleDistance": 1,
      "radius": 0.25,
      "fireRange": 1,
      "circleCount": 5,
      "fireDuration": 40,
      "circlePeriod": 2
    }
  },
  "throwAbilities": {
    "THROW": {
      "price": {
        "health": 0,
        "food": 1
      },
      "maxDistance": 5,
      "damageAmplifier": 0.75,
      "speed": 0.5,
      "range": 1
    }
  },
  "stormAbilities": {
    "STORM": {
      "price": {
        "health": 2,
        "food": 4
      },
      "radius": 3,
      "frequency": 9
    }
  }
}
```
