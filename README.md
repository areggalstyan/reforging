# Reforging

Reforging plugin adds a new mechanic for enhancing weapons with various stat boosts and abilities. It is completely
customizable with options to add custom reforges, heavily tweak existing abilities, and change the looks of items.

### How to apply a reforge?

1. Craft a reforging anvil (recipe specified in anvil.json)
2. Place it somewhere in the world
3. Right-click it with a weapon in your hand (any sword or axe)
4. You will see it placed on the anvil
5. Right-click it with the set amount of the individual material (e.g., diamonds for the diamond sword, the amount
   specified in anvil.json)
6. The set amount of the individual material will be consumed
7. It will drop, when you pick it up, it will have a reforge on it

### How to activate an ability?

1. Right-click on the air with the weapon in your hand

### Commands

#### /reforge

Reforges the item in the main hand of the player.

##### Usage

/reforge &lt;reforgeName&gt;

##### Permission

`reforging.command.reforge`

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

Configures the visuals and mechanics of the reforging anvil.

#### name: string

The name of the reforging anvil item.

Colorized, no placeholders.

#### lore: string array

The lore of the reforging anvil item.

Colorized, no placeholders.

#### recipe: object

Specifies the crafting recipe for the reforging anvil item.

##### shape: string array

The shape of the crafting recipe for the reforging anvil item. 

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
  "name": "%DARK_PURPLE%Reforging Anvil",
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

#### weight: double

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

Sets entities on fire in the player's facing direction according to the specified function.

##### price: object

Specifies the price the player pays when activates this ability.

###### health: double

Specifies the amount of health subtracted from the player when activating the ability.

###### food: int

Specifies the amount of food subtracted from the player when activating the ability.

##### function: object

Specifies a mathematical parametric function with variable t.

###### x: expression

Specifies the parametric expression for the x coordinate.

###### y: expression

Specifies the parametric expression for the y coordinate.

###### z: expression

Specifies the parametric expression for the z coordinate.

###### min: double

Specifies the minimum value of the parameter.

###### max: double

Specifies the maximum value of the parameter.

###### delta: double

Specifies the change of the parameter's value.

##### particle: particle

Specifies the type of the particle which is used to visualize the function.

##### fireDuration: int

Specifies how long will hit entities be on fire in ticks (1 second = 20 ticks).

##### duration: int

Specifies the duration in ticks (1 second = 20 ticks).

#### stormAbility: object

Strikes lighting around the player according to the specified function.

##### price: object

Specifies the price the player pays when activates this ability.

###### health: double

Specifies the amount of health subtracted from the player when activating the ability.

###### food: int

Specifies the amount of food subtracted from the player when activating the ability.

##### function: object

Specifies a mathematical parametric function with variable t.

###### x: expression

Specifies the parametric expression for the x coordinate.

###### y: expression

Specifies the parametric expression for the y coordinate.

###### z: expression

Specifies the parametric expression for the z coordinate.

###### min: double

Specifies the minimum value of the parameter.

###### max: double

Specifies the maximum value of the parameter.

###### delta: double

Specifies the change of the parameter's value.

#### throwAbility: object

Allows the player to throw their weapon which will travel for the specified amount of time and damage all the entities it hits on the way.

##### price: object

Specifies the price the player pays when activates this ability.

###### health: double

Specifies the amount of health subtracted from the player when activating the ability.

###### food: int

Specifies the amount of food subtracted from the player when activating the ability.

##### damageFactor: double

Specifies the factor that is applied to the weapon's normal damage.

##### speed: double

Specifies the speed of the weapon when thrown in blocks per tick.

##### duration: int

Specifies the duration in ticks (1 second = 20 ticks).

#### teleportAbility: object

Teleports the player in their facing direction.

##### price: object

Specifies the price the player pays when activates this ability.

###### health: double

Specifies the amount of health subtracted from the player when activating the ability.

###### food: int

Specifies the amount of food subtracted from the player when activating the ability.

##### distance: double

Specifies the maximum distance that player can teleport.

##### cooldown: int

Specifies the cooldown in ticks (1 second = 20 ticks).

#### shieldAbility: object

Gives player damage resistance for the specified period of time.

##### price: object

Specifies the price the player pays when activates this ability.

###### health: double

Specifies the amount of health subtracted from the player when activating the ability.

###### food: int

Specifies the amount of food subtracted from the player when activating the ability.

##### function: object

Specifies a mathematical parametric function with variable t.

###### x: expression

Specifies the parametric expression for the x coordinate.

###### y: expression

Specifies the parametric expression for the y coordinate.

###### z: expression

Specifies the parametric expression for the z coordinate.

###### min: double

Specifies the minimum value of the parameter.

###### max: double

Specifies the maximum value of the parameter.

###### delta: double

Specifies the change of the parameter's value.

##### particle: particle

Specifies the type of the particle which is used to visualize the function.

##### disableAttack: boolean

Specifies whether the player should be prevented from attacking other entities while the shield is active.

##### duration: int

Specifies the duration in ticks (1 second = 20 ticks).

#### function: object

Specifies a mathematical parametric function with variable t.

##### price: object

Specifies the price the player pays when activates this ability.

###### health: double

Specifies the amount of health subtracted from the player when activating the ability.

###### food: int

Specifies the amount of food subtracted from the player when activating the ability.

##### x: expression

Specifies the parametric expression for the x coordinate.

##### y: expression

Specifies the parametric expression for the y coordinate.

##### z: expression

Specifies the parametric expression for the z coordinate.

##### min: double

Specifies the minimum value of the parameter.

##### max: double

Specifies the maximum value of the parameter.

##### delta: double

Specifies the change of the parameter's value.

#### earthAbility: object

Forms one block tall protective barrier around the player according to the specified function.

##### price: object

Specifies the price the player pays when activates this ability.

###### health: double

Specifies the amount of health subtracted from the player when activating the ability.

###### food: int

Specifies the amount of food subtracted from the player when activating the ability.

##### function: object

Specifies a mathematical parametric function with variable t.

###### x: expression

Specifies the parametric expression for the x coordinate.

###### y: expression

Specifies the parametric expression for the y coordinate.

###### z: expression

Specifies the parametric expression for the z coordinate.

###### min: double

Specifies the minimum value of the parameter.

###### max: double

Specifies the maximum value of the parameter.

###### delta: double

Specifies the change of the parameter's value.

```json
{
  "shieldAbilities": {
    "SHIELD": {
      "price": {
        "health": 6,
        "food": 6
      },
      "duration": 200,
      "function": {
        "x": "cos(48t)",
        "y": "0.5t",
        "z": "sin(48t)",
        "min": 0,
        "max": 5,
        "delta": 0.25
      },
      "particle": "ENCHANTMENT_TABLE",
      "disableAttack": true
    }
  },
  "fireAbilities": {
    "FIRE": {
      "price": {
        "health": 4,
        "food": 4
      },
      "duration": 1,
      "function": {
        "x": "0.025tcos(t)",
        "y": "0.025tsin(t)",
        "z": "0.005t",
        "min": 0,
        "max": 20,
        "delta": 0.25
      },
      "particle": "FLAME",
      "fireDuration": 40
    }
  },
  "throwAbilities": {
    "THROW": {
      "price": {
        "health": 0,
        "food": 1
      },
      "duration": 20,
      "damageFactor": 0.75,
      "speed": 0.5
    }
  },
  "stormAbilities": {
    "STORM": {
      "price": {
        "health": 2,
        "food": 4
      },
      "function": {
        "x": "3cos(t)",
        "z": "3sin(t)",
        "min": 0,
        "max": 6.28318530718,
        "delta": 0.34906585039
      }
    }
  },
  "earthAbilities": {
    "EARTH": {
      "price": {
        "health": 1,
        "food": 1
      },
      "function": {
        "x": "3cos(t)",
        "z": "3sin(t)",
        "min": 0,
        "max": 6.28318530718,
        "delta": 0.34906585039
      }
    }
  },
  "teleportAbilities": {
    "TELEPORT": {
      "price": {
        "health": 3,
        "food": 3
      },
      "cooldown": 40,
      "distance": 2
    }
  }
}
```

