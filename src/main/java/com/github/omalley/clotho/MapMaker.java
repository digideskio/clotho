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
package com.github.omalley.clotho;

import com.github.omalley.clotho.NBT.FormatException;
import com.github.omalley.clotho.NBT.Minecraft.Chunk;
import com.github.omalley.clotho.NBT.Minecraft.ID;
import com.github.omalley.clotho.NBT.Minecraft.Map;
import com.github.omalley.clotho.NBT.Minecraft.Region;
import com.github.omalley.clotho.NBT.Minecraft.Section;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.UnflaggedOption;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Create a map from a Minecraft save game's region files.
 */
public class MapMaker {
  static final int SECTIONS_PER_CHUNK = 16;
  static final int CUBES_PER_SECTION = 16;
  static final int CUBES_PER_CHUNK = 16;
  static final int CHUNKS_PER_REGION = 32;
  static final int VARIANTS_PER_COLOR = 4;
  static final int CHUNKS_PER_INDEX = 8;

  static final Color indexColor = new Color(252, 116, 253);

  static class ResourceCount {
    int[] count = new int[3000];
    int missing = 0;
  }

  private final BufferedImage image;
  private final Graphics graphics;
  private final int xRegionMin;
  private final int zRegionMin;
  private final Color[][] colors;

  public MapMaker(int xRegionMin, int xRegionWidth,
                  int zRegionMin, int zRegionWidth) {
    this.xRegionMin = xRegionMin;
    this.zRegionMin = zRegionMin;
    int width = CUBES_PER_CHUNK * CHUNKS_PER_REGION * xRegionWidth +
        xRegionWidth - 1;
    int height = CUBES_PER_CHUNK * CHUNKS_PER_REGION * zRegionWidth +
        zRegionWidth - 1;
    image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
    graphics = image.getGraphics();
    graphics.setColor(new Color(0,0,0));
    graphics.fillRect(0, 0, width, height);
    colors = new Color[CUBES_PER_CHUNK][];
    for(int i=0; i < CUBES_PER_CHUNK; ++i) {
      colors[i] = new Color[CUBES_PER_CHUNK];
    }
    graphics.setColor(indexColor);
    FontMetrics fontMetrics = graphics.getFontMetrics();
    int descent = fontMetrics.getDescent();
    int fontHeight = fontMetrics.getHeight();
    for(int x=1; x < xRegionWidth*CHUNKS_PER_REGION/CHUNKS_PER_INDEX; ++x) {
      int xCoord = x * CUBES_PER_CHUNK * CHUNKS_PER_INDEX + x - 1;
      graphics.fillRect(xCoord, 0, 1, height);
      String label = Integer.toString((x * CHUNKS_PER_INDEX +
          xRegionMin * CHUNKS_PER_REGION) * CUBES_PER_CHUNK);
      graphics.drawString(label, xCoord + 2 , fontHeight);
      graphics.drawString(label, xCoord + 2 , height - descent);
    }
    for(int z=1; z < zRegionWidth*CHUNKS_PER_REGION/CHUNKS_PER_INDEX; ++z) {
      int zCoord = z * CUBES_PER_CHUNK * CHUNKS_PER_INDEX + z - 1;
      graphics.fillRect(0, zCoord, width, 1);
      String label = Integer.toString((z * CHUNKS_PER_INDEX +
          zRegionMin * CHUNKS_PER_REGION) * CUBES_PER_CHUNK);
      graphics.drawString(label, 2 , zCoord + fontHeight);
      graphics.drawString(label, width - fontMetrics.stringWidth(label) - 2,
          zCoord + fontHeight);
    }
  }

  void clearColors() {
    for(int x=0; x < CUBES_PER_CHUNK; ++x) {
      for(int z=0; z < CUBES_PER_CHUNK; ++z) {
        colors[x][z] = null;
      }
    }
  }

  int fillInSection(Color[][] colors,
                    Section section,
                    int sectionId) throws FormatException {
    int result = 0;
    for(int x=0; x < CUBES_PER_CHUNK; ++x) {
      for(int z=0; z < CUBES_PER_CHUNK; ++z) {
        if (colors[x][z] == null) {
          for(int y= CUBES_PER_SECTION - 1; y >= 0; --y) {
            int color =
                getColorByCubeKind(ID.fromId(section.getBlockID(x, y, z)));
            if (color != 0) {
              int height = sectionId * CUBES_PER_SECTION + y;
              result += 1;
              colors[x][z] = Map.MapColors[color * VARIANTS_PER_COLOR +
                  getColorVariant(height)];
              break;
            }
          }
        }
      }
    }
    return result;
  }

  void drawChunk(Chunk chunk, int xOffset, int zOffset) throws FormatException {
    int remaining = CUBES_PER_CHUNK * CUBES_PER_CHUNK;
    int section = SECTIONS_PER_CHUNK - 1;
    clearColors();
    while (section >= 0 && remaining > 0) {
      Section sect = chunk.getSection(section);
      if (sect != null) {
        remaining -= fillInSection(colors, sect, section);
      }
      section -= 1;
    }
    for(int x=0; x < CUBES_PER_CHUNK; x++) {
      for(int z=0; z < CUBES_PER_CHUNK; z++) {
        if (colors[x][z] != null) {
          graphics.setColor(colors[x][z]);
        } else {
          graphics.setColor(Map.MapColors[0]);
        }
        graphics.fillRect(x + xOffset, z + zOffset, 1, 1);
      }
    }
  }

  public void drawRegion(Region region, int xRegion,
                         int zRegion) throws IOException, FormatException {
    int xRegionPositive = xRegion - xRegionMin;
    int zRegionPositive = zRegion - zRegionMin;
    for(int x = 0; x < CHUNKS_PER_REGION; ++x) {
      for(int z = 0; z < CHUNKS_PER_REGION; ++z) {
        Chunk chunk = region.ReadChunk(x, z);
        if (chunk != null) {
          drawChunk(chunk,
              CUBES_PER_CHUNK * (x + CHUNKS_PER_REGION * xRegionPositive) +
                  (xRegionPositive * CHUNKS_PER_REGION + x) / CHUNKS_PER_INDEX,
              CUBES_PER_CHUNK * (z + CHUNKS_PER_REGION * zRegionPositive) +
                  (zRegionPositive * CHUNKS_PER_REGION + z) / CHUNKS_PER_INDEX);
        }
      }
    }
  }

  public void saveImage(String filename) throws IOException {
    ImageIO.write(image, "png", new File(filename));
  }

  private static class RegionFile {
    final int x;
    final int z;
    final File filename;
    RegionFile(File name) throws IOException {
      filename = name;
      String tail = filename.getName();
      String[] parts = tail.split("\\.");
      if (parts.length == 4 && "mca".equals(parts[3])) {
        x = Integer.parseInt(parts[1]);
        z = Integer.parseInt(parts[2]);
      } else {
        throw new IOException("Region file doesn't match pattern: " + name);
      }
    }
  }

  private static class RegionList {
    final List<RegionFile> filenames;
    int minX = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;
    int minZ = Integer.MAX_VALUE;
    int maxZ = Integer.MIN_VALUE;
    RegionList(File... files) throws IOException {
      filenames = new ArrayList<RegionFile>(files.length);
      for(File file: files) {
        filenames.add(new RegionFile(file));
      }
      for(RegionFile file: filenames) {
        minX = Math.min(minX, file.x);
        maxX = Math.max(maxX, file.x);
        minZ = Math.min(minZ, file.z);
        maxZ = Math.max(maxZ, file.z);
      }
    }
  }

  /**
   * Pick the color variant depending on the height. These values were picked
   * by finding the quartiles of the surface level in a large map built by
   * the default generator.
   * @param y the elevation
   * @return 0, 1, 2, or 3 for the map color variants
   */
  private static int getColorVariant(int y) {
    if (y <= 62) {
      return 3;
    } else if (y <= 68) {
      return 0;
    } else if (y <= 74) {
      return 1;
    } else {
      return 2;
    }
  }

  /**
   * Get the map color for a given kind of object.
   * @param kind the kind of object in the location
   * @return a base map color number
   */
  private static int getColorByCubeKind(ID kind) {
    if (kind == null) {
      return 0;
    }
    switch (kind) {
      case GRASS:
      case MYCELIUM:
        return 1;

      case SAND:
      case GRAVEL:
      case SOULSAND:
        return 2;

      case SPONGE:
      case BED:
      case COBWEB:
        return 3;

      case LAVA:
      case TNT:
        return 4;

      case ICE:
      case PACKED_ICE:
        return 5;

      case IRON_BLOCK:
      case REDSTONE_BLOCK:
      case IRON_DOOR:
      case IRON_BARS:
      case BREWING_STAND:
      case CAULDRON:
        return 6;

      case SAPLING:
      case LEAVES:
      case ACACIA_LEAVES:
      case TALL_GRASS:
      case DEAD_BUSH:
      case DANDELION:
      case ROSE:
      case BROWN_MUSHROOM:
      case RED_MUSHROOM:
      case MELON_SEEDS:
      case PUMPKIN_SEEDS:
      case WHEAT_SEEDS:
      case CACTUS:
      case SUGARCANE:
      case PUMPKIN:
      case JACKOLANTERN:
      case MELON:
      case PUMPKIN_STEM:
      case MELON_STEM:
      case VINES:
      case LILY_PAD:
      case NETHER_WART:
      case DRAGON_EGG:
        return 7;

      case SNOW:
      case SNOW_BLOCK:
      case QUARTZ_BLOCK:
        return 8;

      case CLAY:
      case MONSTER_EGG:
        return 9;

      case DIRT:
      case FARMLAND:
        return 10;

      case STONE:
      case COBBLESTONE:
      case BEDROCK:
      case GOLD_ORE:
      case IRON_ORE:
      case COAL_ORE:
      case COAL_BLOCK:
      case LAPIS_LAZULI_ORE:
      case DISPENSER:
      case SANDSTONE:
      case STICKY_PISTON:
      case PISTON:
      case PISTON_EXTENSION:
      case PISTON_MOVING:
      case DOUBLE_SLAB:
      case SLAB:
      case BRICKS:
      case MOSS_STONE:
      case MONSTER_SPAWNER:
      case FURNACE:
      case COBBLESTONE_STAIRS:
      case STONE_PRESSURE_PLATE:
      case REDSTONE_ORE:
      case STONE_BRICKS:
      case BRICK_STAIRS:
      case STONE_BRICK_STAIRS:
      case NETHER_BRICK:
      case NETHER_BRICK_FENCE:
      case NETHER_BRICK_STAIRS:
      case ENCHANTMENT_TABLE:
      case ENDSTONE:
        return 11;

      case WATER:
        return 12;

      case WOODEN_PLANK:
      case WOOD:
      case NOTE_BLOCK:
      case BOOKSHELF:
      case ACACIA_WOOD_STAIRS:
      case BIRCH_WOOD_STAIRS:
      case DARK_OAK_WOOD_STAIRS:
      case JUNGLE_WOOD_STAIRS:
      case OAK_WOOD_STAIRS:
      case SPRUCE_WOOD_STAIRS:
      case CHEST:
      case CRAFTING_TABLE:
      case SIGNPOST:
      case WOODEN_DOOR:
      case WALLSIGN:
      case WOODEN_PRESSURE_PLATE:
      case JUKEBOX:
      case FENCE:
      case LOCKED_CHEST:
      case TRAPDOOR:
      case HUGE_BROWN_MUSHROOM:
      case HUGE_RED_MUSHROOM:
      case FENCE_GATE:
        return 13;

      case WOOL:
      case STAINED_HARDENED_CLAY:
        return 14;

      case GOLD_BLOCK:
        return 30;

      case DIAMOND_ORE:
      case DIAMOND_BLOCK:
        return 31;

      case LAPIS_LAZULI_BLOCK:
        return 32;

      case EMERALD_BLOCK:
        return 33;

      case OBSIDIAN:
        return 34;

      case NETHERRACK:
        return 35;

      default:
        return 0;
    }
  }

  public static void main(String[] args) throws Exception {
    JSAP jsap = new JSAP();
    String HOME = System.getProperty("user.home");
    jsap.registerParameter(new Switch("help", 'h', "help",
        "provide help"));
    jsap.registerParameter(new UnflaggedOption("directory", JSAP.STRING_PARSER,
        HOME + "/Library/Application Support/minecraft/saves/Creative", true,
        true, "list of save game directories"));
    JSAPResult options = jsap.parse(args);

    if (options.success() && !options.getBoolean("help")) {
      for(String dir: options.getStringArray("directory")) {
        File saveDir = new File(dir);
        if (!saveDir.exists() || !saveDir.isDirectory()) {
           System.err.println("Bad save game directory - " + dir);
        } else {
          // get the list of region files
          File regionDir = new File(saveDir, "region");
          RegionList regions = new RegionList(regionDir.listFiles());
          MapMaker map = new MapMaker(regions.minX,
              regions.maxX - regions.minX + 1,
              regions.minZ,
              regions.maxZ - regions.minZ + 1);
          for (RegionFile file : regions.filenames) {
            Region region = new Region(file.filename);
            map.drawRegion(region, file.x, file.z);
          }
          String mapName = saveDir.getName() + "-map.png";
          System.out.println("Writing map to " + mapName);
          map.saveImage(mapName);
        }
      }
    } else {
      for (Iterator errs = options.getErrorMessageIterator();
           errs.hasNext(); ) {
        System.err.println("Error: " + errs.next());
      }
      System.err.println("Usage: map " + jsap.getUsage());
      System.err.println();
      System.err.println(jsap.getHelp());
      System.exit(1);
    }
  }
}
