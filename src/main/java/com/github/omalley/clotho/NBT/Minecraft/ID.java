/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.omalley.clotho.NBT.Minecraft;

/**
 * Block & Item ID_ATTR constants.
 */
public enum ID {
  AIR(0, "air"),
  STONE(1, "stone"),
  GRASS(2, "grass"),
  DIRT(3, "dirt"),
  COBBLESTONE(4, "cobblestone"),
  WOODEN_PLANK(5, "wooden plank"),
  SAPLING(6, "sapling"),
  BEDROCK(7, "bedrock"),
  FLOWING_WATER(8, "flowing water"),
  WATER(9, "water"),
  FLOWING_LAVA(10, "flowing lava"),
  LAVA(11, "lava"),
  SAND(12, "sand"),
  GRAVEL(13, "gravel"),
  GOLD_ORE(14, "gold ore"),
  IRON_ORE(15, "iron ore"),
  COAL_ORE(16, "coal ore"),
  WOOD(17, "wood"),
  LEAVES(18, "leaves"),
  SPONGE(19, "sponge"),
  GLASS(20, "glass"),
  LAPIS_LAZULI_ORE(21, "lapis lazuli ore"),
  LAPIS_LAZULI_BLOCK(22, "lapis lazuli block"),
  DISPENSER(23, "dispenser"),
  SANDSTONE(24, "sandstone"),
  NOTE_BLOCK(25, "note block"),
  BED_BLOCK(26, "bed block"),
  POWERED_RAIL(27, "powered rail"),
  DETECTOR_RAIL(28, "detector rail"),
  STICKY_PISTON(29, "sticky piston"),
  COBWEB(30, "cobweb"),
  TALL_GRASS(31, "tall grass"),
  DEAD_BUSH(32, "dead bush"),
  PISTON(33, "piston"),
  PISTON_EXTENSION(34, "piston extension"),
  WOOL(35, "wool"),
  PISTON_MOVING(36, "piston moving"),
  DANDELION(37, "dandelion"),
  ROSE(38, "rose"),
  BROWN_MUSHROOM(39, "brown mushroom"),
  RED_MUSHROOM(40, "red mushroom"),
  GOLD_BLOCK(41, "gold block"),
  IRON_BLOCK(42, "iron block"),
  DOUBLE_SLAB(43, "double slab"),
  SLAB(44, "slab"),
  BRICKS(45, "bricks"),
  TNT(46, "tnt"),
  BOOKSHELF(47, "bookshelf"),
  MOSS_STONE(48, "moss stone"),
  OBSIDIAN(49, "obsidian"),
  TORCH(50, "torch"),
  FIRE(51, "fire"),
  MONSTER_SPAWNER(52, "monster spawner"),
  OAK_WOOD_STAIRS(53, "oak wood stairs"),
  CHEST(54, "chest"),
  REDSTONE_WIRE(55, "redstone wire"),
  DIAMOND_ORE(56, "diamond ore"),
  DIAMOND_BLOCK(57, "diamond block"),
  CRAFTING_TABLE(58, "crafting table"),
  WHEAT_BLOCK(59, "wheat block"),
  FARMLAND(60, "farmland"),
  FURNACE(61, "furnace"),
  BURNING_FURNACE(62, "burning furnace"),
  SIGNPOST(63, "signpost"),
  WOODEN_DOOR_BLOCK(64, "wooden door block"),
  LADDERS(65, "ladders"),
  RAILS(66, "rails"),
  COBBLESTONE_STAIRS(67, "cobblestone stairs"),
  WALLSIGN(68, "wallsign"),
  LEVER(69, "lever"),
  STONE_PRESSURE_PLATE(70, "stone pressure plate"),
  IRON_DOOR_BLOCK(71, "iron door block"),
  WOODEN_PRESSURE_PLATE(72, "wooden pressure plate"),
  REDSTONE_ORE(73, "redstone ore"),
  GLOWING_REDSTONE_ORE(74, "glowing redstone ore"),
  REDSTONE_TORCH_OFF(75, "redstone torch off"),
  REDSTONE_TORCH(76, "redstone torch"),
  BUTTON(77, "button"),
  SNOW(78, "snow"),
  ICE(79, "ice"),
  SNOW_BLOCK(80, "snow block"),
  CACTUS(81, "cactus"),
  CLAY_BLOCK(82, "clay block"),
  SUGARCANE_BLOCK(83, "sugarcane block"),
  JUKEBOX(84, "jukebox"),
  FENCE(85, "fence"),
  PUMPKIN(86, "pumpkin"),
  NETHERRACK(87, "netherrack"),
  SOULSAND(88, "soulsand"),
  GLOWSTONE_BLOCK(89, "glowstone block"),
  PORTAL(90, "portal"),
  JACKOLANTERN(91, "jackolantern"),
  CAKE_BLOCK(92, "cake block"),
  REDSTONE_REPEATER_OFF(93, "redstone repeater off"),
  REDSTONE_REPEATER_ON(94, "redstone repeater on"),
  LOCKED_CHEST(95, "locked chest"),
  TRAPDOOR(96, "trapdoor"),
  MONSTER_EGG(97, "monster egg"),
  STONE_BRICKS(98, "stone bricks"),
  HUGE_BROWN_MUSHROOM(99, "huge brown mushroom"),
  HUGE_RED_MUSHROOM(100, "huge red mushroom"),
  IRON_BARS(101, "iron bars"),
  GLASSPANE(102, "glasspane"),
  MELON_BLOCK(103, "melon block"),
  PUMPKIN_STEM(104, "pumpkin stem"),
  MELON_STEM(105, "melon stem"),
  VINES(106, "vines"),
  FENCE_GATE(107, "fence gate"),
  BRICK_STAIRS(108, "brick stairs"),
  STONE_BRICK_STAIRS(109, "stone brick stairs"),
  MYCELIUM(110, "mycelium"),
  LILY_PAD(111, "lily pad"),
  NETHER_BRICK(112, "nether brick"),
  NETHER_BRICK_FENCE(113, "nether brick fence"),
  NETHER_BRICK_STAIRS(114, "nether brick stairs"),
  NETHER_WART_STALK(115, "nether wart stalk"),
  ENCHANTMENT_TABLE(116, "enchantment table"),
  BREWING_STAND_BLOCK(117, "brewing stand block"),
  CAULDRON_BLOCK(118, "cauldronblock"),
  ENDPORTAL(119, "endportal"),
  ENDPORTAL_FRAME(120, "endportal frame"),
  ENDSTONE(121, "endstone"),
  DRAGON_EGG(122, "dragon egg"),
  REDSTONE_LAMP(123, "redstonelamp"),
  REDSTONE_LAMP_ON(124, "redstone lamp on"),
  WOODEN_DOUBLE_SLAB(125, "wooden double slab"),
  WOODEN_SLAB(126, "wooden slab"),
  COCOA_PLANT(127, "cocoa plant"),
  SANDSTONE_STAIRS(128, "sandstone stairs"),
  EMERALD_ORE(129, "emerald ore"),
  ENDER_CHEST(130, "ender chest"),
  TRIPWIRE_HOOK(131, "tripwire hook"),
  TRIPWIRE(132, "tripwire"),
  EMERALD_BLOCK(133, "emerald block"),
  SPRUCE_WOOD_STAIRS(134, "spruce wood stairs"),
  BIRCH_WOOD_STAIRS(135, "birch wood stairs"),
  JUNGLE_WOOD_STAIRS(136, "jungle wood stairs"),
  COMMAND_BLOCK(137, "command block"),
  BEACON_BLOCK(138, "beacon block"),
  COBBLESTONE_WALL(139, "cobblestone wall"),
  FLOWER_POT(140, "flower pot"),
  CARROT(141, "carrot"),
  POTATOES(142, "potatoes"),
  WOODEN_BUTTON(143, "wooden button"),
  SKELETON(144, "skeleton"),
  ANVIL(145, "anvil"),
  TRAPPED_CHEST(146, "trapped chest"),
  LIGHT_WEIGHTED_PRESSURE_PLATE(147, "light weighted pressure plate"),
  HEAVY_WEIGHTED_PRESSURE_PLATE(148, "heavy weighted pressure plate"),
  REDSTONE_COMPARATOR_ON(149, "redstone comparator on"),
  REDSTONE_COMPARATOR_OFF(150, "redstone comparator off"),
  DAYLIGHT_DETECTOR(151, "daylight detector"),
  REDSTONE_BLOCK(152, "redstone block"),
  NETHER_QUARTZ_ORE(153, "nether quartz ore"),
  HOPPER(154, "hopper"),
  QUARTZ_BLOCK(155, "quartz block"),
  QUARTZ_STAIRS(156, "quartz stairs"),
  ACTIVATOR_RAIL(157, "activator rail"),
  DROPPER(158, "dropper"),
  STAINED_HARDENED_CLAY(159, "stained hardened clay"),
  STAINED_GLASS_PANE(160, "stained glass pane"),
  ACACIA_LEAVES(161, "acacia leaves"),
  ACACIA_WOOD(162, "acacia wood"),
  ACACIA_WOOD_STAIRS(163, "acacia wood stairs"),
  DARK_OAK_WOOD_STAIRS(164, "dark oak wood stairs"),
  SLIME_BLOCK(165, "slime block"),
  BARRIER(166, "barrier"),
  IRON_TRAPDOOR(167, "iron trapdoor"),
  PRISMARINE(168, "prismarine"),
  SEA_LATTERN(169, "sea lantern"),
  HAY_BLOCK(170, "hay block"),
  WHITE_CARPET(171, "white carpet"),
  HARDENED_CLAY(172, "hardened clay"),
  COAL_BLOCK(173, "coal block"),
  PACKED_ICE(174, "packed ice"),
  LARGE_FLOWERS(175, "large flowers"),

  IRON_SHOVEL(256, "iron shovel"),
  IRON_PICKAXE(257, "iron pickaxe"),
  IRON_AXE(258, "iron axe"),
  FLINT_AND_STEEL(259, "flint and steel"),
  RED_APPLE(260, "red apple"),
  BOW(261, "bow"),
  ARROW(262, "arrow"),
  COAL(263, "coal"),
  DIAMOND(264, "diamond"),
  IRON_INGOT(265, "iron ingot"),
  GOLD_INGOT(266, "gold ingot"),
  IRON_SWORD(267, "iron sword"),
  WOODEN_SWORD(268, "wooden sword"),
  WOODEN_SHOVEL(269, "wooden shovel"),
  WOODEN_PICKAXE(270, "wooden pickaxe"),
  WOODEN_AXE(271, "wooden axe"),
  STONE_SWORD(272, "stone sword"),
  STONE_SHOVEL(273, "stone shovel"),
  STONE_PICKAXE(274, "stone pickaxe"),
  STONE_AXE(275, "stone axe"),
  DIAMOND_SWORD(276, "diamond sword"),
  DIAMOND_SHOVEL(277, "diamond shovel"),
  DIAMOND_PICKAXE(278, "diamond pickaxe"),
  DIAMOND_AXE(279, "diamond axe"),
  STICK(280, "stick"),
  BOWL(281, "bowl"),
  MUSHROOM_STEW(282, "mushroom stew"),
  GOLDEN_SWORD(283, "golden sword"),
  GOLDEN_SHOVEL(284, "golden shovel"),
  GOLDEN_PICKAXE(285, "golden pickaxe"),
  GOLDEN_AXE(286, "golden axe"),
  STRING(287, "string"),
  FEATHER(288, "feather"),
  GUNPOWDER(289, "gunpowder"),
  WOODEN_HOE(290, "wooden hoe"),
  STONE_HOE(291, "stone hoe"),
  IRON_HOE(292, "iron hoe"),
  DIAMOND_HOE(293, "diamond hoe"),
  GOLD_HOE(294, "gold hoe"),
  WHEAT_SEEDS(295, "    wheat seeds"),
  WHEAT(296, "wheat"),
  BREAD(297, "bread"),
  LEATHER_CAP(298, "leather cap"),
  LEATHER_TUNIC(299, "leather tunic"),
  LEATHER_PANTS(300, "leather pants"),
  LEATHER_BOOTS(301, "leather boots"),
  CHAIN_HELMET(302, "chain helmet"),
  CHAIN_CHESTPLATE(303, "chain chestplate"),
  CHAIN_LEGGINGS(304, "chain leggings"),
  CHAIN_BOOTS(305, "chain boots"),
  IRON_HELMET(306, "ironhelmet"),
  IRON_CHESTPLATE(307, "iron chestplate"),
  IRON_LEGGINGS(308, "iron leggings"),
  IRON_BOOTS(309, "iron boots"),
  DIAMOND_HELMET(310, "diamond helmet"),
  DIAMOND_CHESTPLATE(311, "diamond chestplate"),
  DIAMOND_LEGGINGS(312, "diamond leggings"),
  DIAMOND_BOOTS(313, "diamond boots"),
  GOLDEN_HELMET(314, "golden helmet"),
  GOLDEN_CHESTPLATE(315, "golden chestplate"),
  GOLDEN_LEGGINGS(316, "golden leggings"),
  GOLDEN_BOOTS(317, "golden boots"),
  FLINT(318, "flint"),
  RAW_PORKCHOP(319, "raw porkchop"),
  COOKED_PORKCHOP(320, "cooked porkchop"),
  PAINTING(321, "painting"),
  GOLDEN_APPLE(322, "golden apple"),
  SIGN(323, "sign"),
  WOODEN_DOOR(324, "wooden door"),
  BUCKET(325, "bucket"),
  WATER_BUCKET(326, "water bucket"),
  LAVA_BUCKET(327, "lava bucket"),
  MINECART(328, "minecart"),
  SADDLE(329, "saddle"),
  IRON_DOOR(330, "iron door"),
  REDSTONE_DUST(331, "redstone dust"),
  SNOWBALL(332, "snowball"),
  BOAT(333, "boat"),
  LEATHER(334, "leather"),
  MILK_BUCKET(335, "milk bucket"),
  CLAY_BRICK(336, "clay brick"),
  CLAY(337, "clay"),
  SUGARCANE(338, "sugarcane"),
  PAPER(339, "paper"),
  BOOK(340, "book"),
  SLIMEBALL(341, "slimeball"),
  STORAGE_MINECART(342, "storage minecart"),
  POWEREDMINE_CART(343, "powered minecart"),
  CHICKEN_EGG(344, "chicken egg"),
  COMPASS(345, "compass"),
  FISHING_ROD(346, "fishing rod"),
  CLOCK(347, "clock"),
  GLOWSTONE_DUST(348, "glowstone dust"),
  RAW_FISH(349, "raw fish"),
  COOKED_FISH(350, "cooked fish"),
  DYE(351, "dye"),
  BONE(352, "bone"),
  SUGAR(353, "sugar"),
  CAKE(354, "cake"),
  BED(355, "bed"),
  REDSTONE_REPEATER(356, "redstone repeater"),
  COOKIE(357, "cookie"),
  MAP(358, "map"),
  SHEARS(359, "shears"),
  MELON(360, "melon"),
  PUMPKIN_SEEDS(361, "pumpkin seeds"),
  MELON_SEEDS(362, "melon seeds"),
  RAW_BEEF(363, "raw beef"),
  STEAK(364, "steak"),
  RAW_CHICKEN(365, "raw chicken"),
  COOKED_CHICKEN(366, "cooked chicken"),
  ROTTEN_FLESH(367, "rottenf lesh"),
  ENDER_PEARL(368, "ender pearl"),
  BLAZEROD(369, "blazerod"),
  GHAST_TEAR(370, "ghast tear"),
  GOLD_NUGGET(371, "gold nugget"),
  NETHER_WART(372, "nether wart"),
  POTION(373, "potion"),
  GLASS_BOTTLE(374, "glass bottle"),
  SPIDER_EYE(375, "spider eye"),
  FERMENTED_SPIDER_EYE(376, "fermented spider eye"),
  BLAZE_POWDER(377, "blaze powder"),
  MAGMA_CREAM(378, "magma cream"),
  BREWING_STAND(379, "brewing stand"),
  CAULDRON(380, "cauldron"),
  EYE_OF_ENDER(381, "eye of ender"),
  GLISTERING_MELON(382, "glistering melon"),
  SPAWN_EGG(383, "spawn egg"),
  BOTTLE_O_ENCHANTING(384, "bottle o enchanting"),
  FIRE_CHARGE(385, "fire charge"),
  BOOK_AND_QUILL(386, "book and quill"),
  WRITTEN_BOOK(387, "written book"),
  EMERALD(388, "emerald"),

  RECORD_13(2256, "record 13"),
  RECORD_CAT(2257, "record cat"),
  RECORD_BLOCKS(2258, "record blocks"),
  RECORD_CHIRP(2259, "record chirp"),
  RECORD_FAR(2260, "record far"),
  RECORD_MALL(2261, "record mall"),
  RECORD_MELLOHI(2262, "record mellohi"),
  RECORD_STAL(2263, "record stal"),
  RECORD_STRAD(2264, "record strad"),
  RECORD_WARD(2265, "record ward"),
  RECORD_11(2266, "record 11");

  final int id;
  final String name;

  private ID(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public String toString() {
    return name;
  }

  public int getId() {
    return id;
  }

  private static ID[] cache;

  public static ID fromId(int id) {
    if (cache == null) {
      cache = new ID[2267];
      for(ID value: values()) {
        cache[value.id] = value;
      }
    }
    return cache[id];
  }
}