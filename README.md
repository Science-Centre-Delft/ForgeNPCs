# ForgeNPCs
Custom NPCs for Forge ModLoader Minecraft servers.

## Mod Setup
Place the ForgeNPCs jar file in the client and server's `mods` directory. No configuration has to be done on either side.

## Summoning NPCs
NPCs can be summoned using the Minecraft `/summon <entityType> <x> <y> <z> [entityData]` command. NPCs have type `forgenpcs:forgenpcs:npc`. The entity data is a json object with the following supported keys:
- `Rotation`
	- Syntax: `[<float>]` (e.g. `[180f]`).
	- Sets the NPC yaw (0 to 360 degrees).
- `Health`
	- Syntax: `<int>` (e.g. `1`).
	- Sets the NPC initial health. Setting this to 1 causes the NPC to die by a single hit.
- `DisplayName`
	- Syntax: `<string>` (e.g. `"Steve"`).
	- Sets the NPC above-head display name.
- `Pose`
	- Syntax: Json object with optional keys `Head`, `Body`, `LeftArm`, `RightArm`, `LeftLeg` and `RightLeg`. Each key needs to have a value in format `[<float>, <float>, <float>]` (e.g. `{Head: [0f, 30f, 0f]}`).
	- Sets the rotation of all given bodyparts.
- `HandItems`
	- Syntax: `[<mainHandItem>, <offHandItem>]` where both items are json objects describing valid Minecraft items (e.g. `[{}, {}]` for no held items or `[{id: "minecraft:diamond_sword", Count: 1b}, {}]` for a diamond sword in the main hand).
	- Sets the items held by the NPC for both hands.
- `ArmorItems`
	- Syntax: `[<feetItem>, <legsItem>, <chestItem>, <headItem>]` where all items are json objects describing valid Minecraft items (e.g. `[{}, {}, {}, {}]` for no worn items).
- `Texture`
	- Syntax: `<string>` (e.g. `forgenpcs:textures/entity/npc/villager/male_1_1.png` or `entity.npc.villager.male_1_1`).
	- Sets the texture used for the NPC. This supports two formats:
		- Format 1: `<namespace>:<texturePath>`. This has access to all client-side ForgeNPCs textures, resource pack textures and textures from other mods.
		- Format 2: `<serverTexturePath>`. This is a file path to a `.png` file using `.` as file separator (`.png` should not be included in the path). The path is relative to the `mods/forgenpcs/Textures` directory on the server. Note that these textures are provided by the server and do not have to be present in client resources.
- `LookAtPlayer`
	- Syntax: `<boolean>` (e.g. `true`).
	- If set to `true`, makes the NPC look at the player when the player is within a 10 block radius.
- `LookRandomly`
	- Syntax: `<boolean>` (e.g. `true`).
	- If set to `true`, makes the NPC look around 'randomly'.
- `WaterAvoidingRandomWalkingSpeed`
	- Syntax: `<double>` (e.g. `0.5d`).
	- If the speed is greater than 0, makes the NPC walk around randomly with the given walk speed, while avoiding water.
- `PanicSpeed`
	- Syntax: `<double>` (e.g. `0.7d`).
	- If the speed is greater than 0, makes the NPC run around randomly with many path changes with the given speed.
- `Sneaking`
	- Syntax: `<boolean>` (e.g. `true`).
	- Sets the NPC sneaking state.
- `WalkToLocation`
	- Syntax: `{x: <double>, y: <double>, z: <double>, speed: <double>}`.
	- If the speed is greater than 0, makes the NPC walk towards the given absolute position.

## Example NPC summon commands
- Summons an NPC named "Pieter" with 1 health with its arms in the air:
	`/summon forgenpcs:npc ~ ~ ~ {Health: 1, DisplayName: "Pieter", Pose: {Head: [-20f, 0f, 0f], Body: [0f, 0f, 0f], LeftArm: [-170f, 0f, 30f], RightArm: [-170f, 0f, -30f], LeftLeg: [0f, 0f, 0f], RightLeg: [0f, 0f, 0f]}}`
- Summons an NPC named "Knight" with 1 health holding a sword in a fancy pose:
	`/summon forgenpcs:npc ~ ~ ~ {Health: 1, DisplayName: "Knight", Pose: {Head: [33f, 0f, 0f], LeftLeg: [340f, 0f, 0f], RightLeg: [15f, 0f, 0f], RightArm: [58f, 178f, 319f]}, HandItems: [{id: "minecraft:diamond_sword", Count: 1b}, {}]}`
- Summons an NPC named "Steve" with a custom texture:
	`/summon forgenpcs:npc ~ ~ ~ {Health: 1, DisplayName: "Steve", Texture: "forgenpcs:textures/entity/npc/steve_with_overlay.png"}`
