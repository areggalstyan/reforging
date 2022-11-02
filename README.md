# Reforging

Reforging plugin adds a new mechanic for enhancing weapons with various stat boosts and abilities. It is completely
customizable with options to add custom reforges, heavily tweak existing abilities, and change the looks of items.

### How to apply a reforge?

1. Craft a reforge anvil (recipe specified in anvil.json)
2. Place it somewhere in the world
3. Right-click it with a weapon in your hand (any sword or axe)
4. You will see it placed on the anvil
5. Right-click it with the set amount of the individual material (e.g., diamonds for the diamond sword, the amount specified in anvil.json)
6. The set amount of the individual material will be consumed
7. It will drop, when you pick it up, it will have a reforge on it

### How to activate an ability?

1. Right-click on the air with the weapon in your hand

### Configuration

Visit [https://reforging.vercel.app/](https://reforging.vercel.app/) for a fully featured custom configuration
generator. Visit [https://github.com/Aregcraft/reforging](https://github.com/Aregcraft/reforging) for an in-depth
description of abilities and help with manual configuration.

### Support

If you encounter any bugs or issues please report them at
[https://github.com/Aregcraft/reforging/issues](https://github.com/Aregcraft/reforging/issues).

<!-- <screenshots> -->

![Fire Ability](https://github.com/Aregcraft/reforging/blob/master/screenshots/fire_ability.png)
![Shield Ability](https://github.com/Aregcraft/reforging/blob/master/screenshots/shield_ability.png)
![Earth Ability](https://github.com/Aregcraft/reforging/blob/master/screenshots/earth_ability.png)
![Sword on Anvil](https://github.com/Aregcraft/reforging/blob/master/screenshots/sword_on_anvil.png)
![Throw Ability](https://github.com/Aregcraft/reforging/blob/master/screenshots/throw_ability.png)
![Storm Ability](https://github.com/Aregcraft/reforging/blob/master/screenshots/storm_ability.png)

<!-- </screenshots> -->

## Manual Configuration

**If you are not familiar with JSON, you may use online YAML to JSON converters,
for example [https://onlineyamltools.com/convert-yaml-to-json](https://onlineyamltools.com/convert-yaml-to-json).**

Specify colors and placeholders with `%COLOR_NAME%` or `%PLACEHOLDER_NAME%` (e.g., `%DARK_BLUE%`, `%REFORGE_NAME%`).

### item.json

Configures the visuals of reforged items.

#### name: string

The name of each reforged item.

Colorized, placeholders:
- REFORGE_NAME - The name of the reforge (e.g., "Sharp", "Shielded")
- NAME - The name of the item (e.g., "Diamond Sword")

#### lore: string array

The lore attached to the end of each reforged item.

Colorized, placeholders:
- **BASE_ATTACK_SPEED** - The base attack speed of the weapon
- **ATTACK_SPEED** - The boost of attack speed provided by the reforge
- **BASE_ATTACK_DAMAGE** - The base attack damage of the weapon
- **ATTACK_DAMAGE** - The boost of attack damage provided by the reforge
- **MAX_HEALTH** - The boost of max health provided by the reforge
- **KNOCKBACK_RESISTANCE** - The boost of knockback resistance provided by the reforge
- **MOVEMENT_SPEED** - The boost of movement speed provided by the reforge
- **ARMOR** - The boost of armor provided by the reforge
- **ARMOR_TOUGHNESS** - The boost of armor toughness provided by the reforge
- **ATTACK_KNOCKBACK** - The boost of attack knockback provided by the reforge

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

Configures the visuals and mechanics of the reforge anvil.

#### name: string

The name of the reforge anvil item.

Colorized, no placeholders.

#### lore: string array

The lore of the reforge anvil item.

Colorized, no placeholders.

#### recipe: object

##### shape: string array

The shape of the crafting recipe for the reforge anvil item. 

3 strings, each consisting of 3 symbols representing items as per `keys`.

##### keys: object

Maps single symbols to their respective item. Item should be specified with all CAPS and without `minecraft:`.

#### soundEffect: object

Specifies the volume and pitch of the sound (`BLOCK_ANVIL_USE`) played whenever a player applies a reforge.

#### price: int

Specifies the amount of respective material (e.g., diamonds for diamond sword, iron ingots for iron axe) required
to apply a reforge.

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

The name of the reforge, it corresponds to the `REFORGE_NAME` label.

#### ability: string

The name of the ability of the reforge as per `abilities.json`.

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
  },
  {
    "name": "Titanic",
    "ability": "EARTH",
    "attackDamage": -1,
    "armor": 2,
    "weight": 20
  }
]
```

### abilities.json

Each ability has many customizable options. This file allows creating multiple abilities with their own names and
tweaks to the base built-in abilities.

<!-- <abilities> -->

#### fireAbility: object

Forms multiple circles in the direction where the player is facing, each further from the player, and with bigger radius. If any entity hits the particle, then that entity will be set on fire.

##### price: object

Specifies the price the player pays when activates this ability.

###### health: double

Specifies the amount of health subtracted from the player when activating the ability.

###### food: int

Specifies the amount of food subtracted from the player when activating the ability.

##### particle: particle

Specifies the type of the particle which is used to form circles.

##### particleFrequency: double

Specifies the number of particles.

##### circleDistance: double

Specifies the distance between each circle.

##### radius: double

Specifies the radius of the smallest circle.

##### fireRange: double

Specifies the maximum distance from a particle which will set an entity on fire.

##### circleCount: int

Specifies the count of the circles. Each next circle is bigger than the previous one.

##### fireDuration: int

Specifies how long will hit entities be on fire in ticks (1 second = 20 ticks).

##### circlePeriod: int

Specifies how much time after forming a circle should pass to form the next one in ticks (1 second = 20 ticks).

#### stormAbility: object

Allows the player to strike a circle of lighting around them.

##### price: object

Specifies the price the player pays when activates this ability.

###### health: double

Specifies the amount of health subtracted from the player when activating the ability.

###### food: int

Specifies the amount of food subtracted from the player when activating the ability.

##### radius: double

Specifies the radius of the circle formed around the player.

##### frequency: double

Specifies the number of lightnings.

#### throwAbility: object

Allows the player to throw sword which will disappear after hitting a solid block or travelling the maximum specified distance and damage all the entities it hits on the way.

##### price: object

Specifies the price the player pays when activates this ability.

###### health: double

Specifies the amount of health subtracted from the player when activating the ability.

###### food: int

Specifies the amount of food subtracted from the player when activating the ability.

##### maxDistance: double

Specifies the maximum distance that the weapon travels before disappearing.

##### damageAmplifier: double

Specifies how the amplifier applied to the weapons usual damage when thrown.

##### speed: double

Specifies the speed of the weapon when thrown in blocks.

##### range: double

Specifies the maximum distance from the weapon which will damage an entity.

#### teleportAbility: object

Allows players to teleport at the direction that they are looking.

##### price: object

Specifies the price the player pays when activates this ability.

###### health: double

Specifies the amount of health subtracted from the player when activating the ability.

###### food: int

Specifies the amount of food subtracted from the player when activating the ability.

##### maxDistance: double

Specifies the maximum distance that player can teleport.

##### cooldown: int

Specifies the cooldown in ticks (1 second = 20 ticks).

#### shieldAbility: object

Gives player damage resistance for a specified period of time. You can have multiple abilities inheriting from this with different options and names.

##### price: object

Specifies the price the player pays when activates this ability.

###### health: double

Specifies the amount of health subtracted from the player when activating the ability.

###### food: int

Specifies the amount of food subtracted from the player when activating the ability.

##### particle: particle

Specifies the type of the particle which is used to form a circle around the player.

##### particleFrequency: double

Specifies the number of particles.

##### radius: double

Specifies the radius of the circle formed around the player.

##### duration: int

Specifies the duration of the shield in ticks (1 second = 20 ticks).

##### disableAttack: boolean

Specifies whether the player should be prevented from attacking other entities while the shield is active.

#### earthAbility: object

Allows the player to form a protective block circle around them.

##### price: object

Specifies the price the player pays when activates this ability.

###### health: double

Specifies the amount of health subtracted from the player when activating the ability.

###### food: int

Specifies the amount of food subtracted from the player when activating the ability.

##### radius: double

Specifies the radius of the circle formed around the player.

##### frequency: double

Specifies the number of blocks.

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
  },
  "earthAbilities": {
    "EARTH": {
      "price": {
        "health": 1,
        "food": 1
      },
      "radius": 3,
      "frequency": 9
    }
  },
  "teleportAbilities": {
    "TELEPORT": {
      "price": {
        "health": 3,
        "food": 3
      },
      "maxDistance": 2,
      "cooldown": 40
    }
  }
}
```

<!-- </abilities> -->
