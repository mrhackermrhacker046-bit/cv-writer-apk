package com.example.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CraftRepository(private val dao: CraftItemDao) {
  val allItems: Flow<List<CraftItem>> = dao.getAllItems()

  fun getItemById(id: Long): Flow<CraftItem?> = dao.getItemById(id)

  suspend fun insert(item: CraftItem): Long = dao.insert(item)

  suspend fun update(item: CraftItem) = dao.update(item)

  suspend fun delete(item: CraftItem) = dao.delete(item)

  suspend fun deleteById(id: Long) = dao.deleteById(id)

  suspend fun toggleUpvote(item: CraftItem) {
    val newUpvoted = !item.isUpvoted
    val delta = if (newUpvoted) 1 else -1
    dao.updateUpvote(item.id, newUpvoted, delta)
  }

  suspend fun toggleSaved(item: CraftItem) {
    dao.updateSaved(item.id, !item.isSaved)
  }

  suspend fun prepopulateIfNeeded() {
    val count = dao.getCount()
    if (count == 0) {
      val initialItems = listOf(
        CraftItem(
          title = "Cliffside Citadel of Eldoria",
          category = "Builds",
          description = "A massive medieval mountain fortress perched on extreme jagged cliffs. Features fully furnished throne hall, armory, spiral watchtowers, and underground harbor docks.",
          author = "NordicCrafter",
          edition = "Both",
          mcVersion = "1.21+",
          imageResName = "img_build_castle",
          tags = "#castle, #survival, #fortress, #medieval",
          materials = "Deepslate Tiles, Spruce Wood, Stone Bricks, Copper Lanterns, Iron Bars",
          difficulty = "Master",
          upvotes = 248,
          isUpvoted = false,
          isSaved = true,
          isUserCreated = false,
          createdAt = System.currentTimeMillis() - 86400000L * 2
        ),
        CraftItem(
          title = "Triple Cherry Grove & Mountain Crater Seed",
          category = "Seeds",
          description = "Spawn directly inside a scenic ring of snowy peaks enclosing three cherry blossom groves and an exposed lush cave ravine. A village sits right at spawn with an accessible Ancient City directly beneath!",
          author = "BiomeScout",
          edition = "Both",
          mcVersion = "1.21 Tricky Trials",
          imageResName = "img_seed_cherry",
          seed = "-82910481940182",
          coordinates = "X: -240, Y: 110, Z: 512",
          tags = "#cherrygrove, #scenic, #village, #ancientcity",
          upvotes = 389,
          isUpvoted = true,
          isSaved = true,
          isUserCreated = false,
          createdAt = System.currentTimeMillis() - 86400000L * 3
        ),
        CraftItem(
          title = "Compact Multi-Item Auto Sorter v4",
          category = "Redstone",
          description = "Silent, overflow-protected multi-item sorter utilizing modern crafters and copper bulbs. Features 1-wide tileable slice design with zero item loss and auto-overflow detection.",
          author = "LogicBlock_99",
          edition = "Java",
          mcVersion = "1.21+",
          imageResName = "img_redstone_machine",
          tags = "#redstone, #automation, #storage, #crafter",
          materials = "Crafters, Comparators, Redstone Repeaters, Copper Bulbs, Hoppers, Chests",
          difficulty = "Medium",
          upvotes = 195,
          isUpvoted = false,
          isSaved = false,
          isUserCreated = false,
          createdAt = System.currentTimeMillis() - 86400000L * 4
        ),
        CraftItem(
          title = "Elysium Survival SMP Community",
          category = "Servers",
          description = "A thriving, cross-play friendly SMP server! Features custom player economy, claims system, custom fishing, community build events, and active voice chat integration. Bedrock & Java compatible.",
          author = "ElysiumStaff",
          edition = "Both",
          mcVersion = "1.21.x",
          imageResName = "img_mc_hero",
          serverIp = "play.elysiumsmp.net:25565",
          tags = "#smp, #crossplay, #economy, #pve, #custom",
          upvotes = 422,
          isUpvoted = false,
          isSaved = false,
          isUserCreated = false,
          createdAt = System.currentTimeMillis() - 86400000L * 5
        ),
        CraftItem(
          title = "Mastering Trial Chambers & The Mace",
          category = "Guides",
          description = "Complete tactical walkthrough to conquer Trial Chambers, defeat the Breeze mob, collect Heavy Cores, and craft/enchant the powerful Mace weapon with Wind Burst and Density V.",
          author = "WardenDodger",
          edition = "Both",
          mcVersion = "1.21 Tricky Trials",
          imageResName = "img_redstone_machine",
          tags = "#guide, #trialchambers, #mace, #breeze, #pve",
          materials = "Wind Charges, Shield, Water Bucket, Crossbow, Heavy Core, Breeze Rods",
          difficulty = "Easy",
          upvotes = 142,
          isUpvoted = false,
          isSaved = false,
          isUserCreated = false,
          createdAt = System.currentTimeMillis() - 86400000L * 6
        ),
        CraftItem(
          title = "Floating Steampunk Airship Base",
          category = "Builds",
          description = "A fully functional flying base with docked scout balloons, rotating propeller engine room, glass observation deck, and onboard brewing laboratory. Fits survival skies.",
          author = "AeroMechanic",
          edition = "Java",
          mcVersion = "1.20+",
          imageResName = "img_mc_hero",
          tags = "#steampunk, #airship, #creative, #survivalbase",
          materials = "Copper Grates, Oak Planks, White Wool, Chain, Campfires, Brass Blocks",
          difficulty = "Medium",
          upvotes = 310,
          isUpvoted = false,
          isSaved = false,
          isUserCreated = false,
          createdAt = System.currentTimeMillis() - 86400000L * 7
        ),
        CraftItem(
          title = "Infinite Iron Golem Farm (350/hr)",
          category = "Redstone",
          description = "Highly reliable, compact iron farm design using 3 frightened villagers and a zombie in a boat. Compact 5x5 footprint, safe from lightning, starts producing in 5 minutes.",
          author = "IronFoundry",
          edition = "Java",
          mcVersion = "1.21+",
          imageResName = "img_build_castle",
          tags = "#ironfarm, #redstone, #survival, #earlygame",
          materials = "3 Beds, 3 Villagers, 1 Zombie, Lava bucket, Water bucket, Signs, Hoppers",
          difficulty = "Easy",
          upvotes = 267,
          isUpvoted = false,
          isSaved = false,
          isUserCreated = false,
          createdAt = System.currentTimeMillis() - 86400000L * 8
        ),
        CraftItem(
          title = "Mansion & Witch Hut Double Spawn Seed",
          category = "Seeds",
          description = "Unbelievable seed with a Woodland Mansion touching a Witch Hut within 150 blocks of spawn! Ideal for dual witch farm speedrun and quick totem of undying farming.",
          author = "SeedHunter_X",
          edition = "Bedrock",
          mcVersion = "1.21+",
          imageResName = "img_seed_cherry",
          seed = "93810482019482",
          coordinates = "X: 84, Y: 68, Z: -210",
          tags = "#mansion, #witchhut, #totem, #rare",
          upvotes = 315,
          isUpvoted = false,
          isSaved = false,
          isUserCreated = false,
          createdAt = System.currentTimeMillis() - 86400000L * 9
        )
      )
      dao.insertAll(initialItems)
    }
  }
}
