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

import com.github.omalley.clotho.NBT.FormatException;
import com.github.omalley.clotho.NBT.Tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A 16x256x16 chunk of a world and all of the things in it.
 */
public class Chunk {
  private final static String X_POS = "xPos";
  private final static String Z_POS = "zPos";
  private final static String LAST_UPDATE = "LastUpdate";
  private final static String TERRAIN_POPULATED = "TerrainPopulated";
  private final static String BIOMES = " Biomes";
  private final static String HEIGHT_MAP = "HeightMap";
  private final static String SECTIONS = "Sections";
  private final static String Y = "Y";
  private final static String INHABITED_TIME = "InhabitedTime";
  private final static String ENTITIES = "Entities";
  private final static String TILE_ENTITIES = "TileEntities";
  private final static String TILE_TICKS = "TileTicks";

  private final Tag.Compound object;

  public Chunk(Tag.Compound obj) {
    object = obj;
  }

  public Chunk(int x, int z) throws IllegalArgumentException {
    object = new Tag.Compound("Level");
    if (x < 0 || x > 31 || z < 0 || z > 31) {
      throw new IllegalArgumentException("Invalid Chunk Coordinates: (" +
          x + ", " + z + ")");
    }
    object.setInteger(X_POS, x);
    object.setInteger(Z_POS, z);
    object.setLong(LAST_UPDATE, 0L);
    object.setByte(TERRAIN_POPULATED, (byte) 0);
    byte[] biomes = new byte[256];
    Arrays.fill(biomes, (byte) -1);
    object.setByteArray(BIOMES, biomes);
  }

  /**
   * Utility function, returns the nibble at the given index in the given 4-bit
   * value array.
   *
   * @param arr   The 4-bit value array, full of nibbles.
   * @param index The index of the nibble to taste.
   * @return The specified nibble in the array.
   */
  static byte Nibble4(byte[] arr, int index) {
    return (byte) ((index & 1) == 0 ? arr[index / 2] & 0x0F :
        arr[index / 2] >> 4);
  }

  /**
   * Utility function, sets the nibble at the given index in the given 4-bit
   * value array.
   *
   * @param arr   The 4-bit value array, full of nibbles.
   * @param index The index of the nibble to bite.
   * @param v4    The new 4-bit value to set the nibble to.
   */
  static void Nibble4(byte[] arr, int index, byte v4) {
    byte n = arr[index / 2];
    //could use optimizing...
    arr[index / 2] = (byte) ((index & 1) == 0 ? ((n >> 4) << 4) + (v4 & 0x0F) :
        (n & 0x0F) + ((v4 & 0x0F) << 4));
  }

  public Section getSection(int y) throws FormatException {
    Tag.ListTag sections = (Tag.ListTag) object.get(SECTIONS);
    if (sections != null) {
      for (Tag child : sections) {
        Tag.Compound ch = (Tag.Compound) child;
        if (ch.getByte(Y) == y) {
          return new Section(ch);
        }
      }
    }
    return null;
  }

  public Section createSection(int y) throws FormatException {
    Tag.ListTag sections = (Tag.ListTag) object.get(SECTIONS);
    if (sections != null) {
      for (int i = 0; i < sections.size(); ++i) {
        Tag.Compound child = (Tag.Compound) sections.Get(i);
        int level = child.getByte(Y);
        if (level == y) {
          return new Section(child);
        } else if (level > y) {
          Section result = new Section(y);
          sections.Insert(i, result.ToNBT(null));
          return result;
        }
      }
      Section result = new Section(y / 16);
      sections.Add(result.ToNBT(null));
      return result;
    } else {
      sections = object.createCompoundList(SECTIONS);
      Section result = new Section(y / 16);
      sections.Add(result.ToNBT(null));
      return result;
    }
  }

  /**
   * Returns the Data of the block at the given coordinates, or -1 if the
   * coordinates are invalid.
   *
   * @param x The X coordinate of the block.
   * @param y The Y coordinate of the block.
   * @param z The Z coordinate of the block.
   * @return The Data of the block at the given coordinates, or -1 if the
   * coordinates are invalid.
   */
  public byte getBlockData(int x, int y, int z) throws FormatException {
    if (x < 0 || x > 15 || y < 0 || y > 255 || z < 0 || z > 15) {
      throw new IllegalArgumentException("Bad block data address " + x + ", " +
          y + ", " + z);
    }
    Section s = getSection(y/16);
    if (s == null) {
      return 0;
    }
    y %= 16;
    return Nibble4(s.data, y * 16 * 16 + z * 16 + x);
  }

  /**
   * Sets the Data of the block at the given coordinates, or does nothing if the
   * coordinates are invalid.
   *
   * @param x      The X coordinate of the block.
   * @param y      The Y coordinate of the block.
   * @param z      The Z coordinate of the block.
   * @param nibble The Data of the block at the given coordinates.
   */
  public void setBlockData(int x, int y, int z,
                        byte nibble) throws FormatException {
    if (x < 0 || x > 15 || y < 0 || y > 255 || z < 0 || z > 15) {
      throw new IllegalArgumentException("Bad block data address " + x + ", " +
          y + ", " + z);
    }
    Section s = createSection(y/16);
    y %= 16;
    Nibble4(s.data, y * 16 * 16 + z * 16 + x, nibble);
  }

  /**
   * Returns the Block Light of the block at the given coordinates, or -1 if the
   * coordinates are invalid.
   *
   * @param x The X coordinate of the block.
   * @param y The Y coordinate of the block.
   * @param z The Z coordinate of the block.
   * @return The Block Light of the block at the given coordinates, or -1 if the
   * coordinates are invalid.
   */
  public byte BlockLight(int x, int y, int z) throws FormatException {
    if (x < 0 || x > 15
        || y < 0 || y > 255
        || z < 0 || z > 15) {
      throw new IllegalArgumentException("Bad block address " + x + ", " +
          y + ", " + z);
    }
    Section s = getSection(y/16);
    if (s == null) {
      return 0;
    }
    y %= 16;
    return Nibble4(s.blocklight, y * 16 * 16 + z * 16 + x);
  }

  /**
   * Sets the Block Light of the block at the given coordinates, or does nothing
   * if the coordinates are invalid.
   *
   * @param x      The X coordinate of the block.
   * @param y      The Y coordinate of the block.
   * @param z      The Z coordinate of the block.
   * @param nibble The Block Light of the block at the given coordinates.
   */
  public void BlockLight(int x, int y, int z,
                         byte nibble) throws FormatException {
    if (x < 0 || x > 15
        || y < 0 || y > 255
        || z < 0 || z > 15) {
      throw new IllegalArgumentException("Bad block address " + x + ", " +
          y + ", " + z);
    }
    Section s = createSection(y/16);
    y %= 16;
    Nibble4(s.blocklight, y * 16 * 16 + z * 16 + x, nibble);
  }

  /**
   * Returns the Sky Light of the block at the given coordinates, or -1 if the coordinates are invalid.
   *
   * @param x The X coordinate of the block.
   * @param y The Y coordinate of the block.
   * @param z The Z coordinate of the block.
   * @return The Sky Light of the block at the given coordinates, or -1 if the coordinates are invalid.
   */
  public byte SkyLight(int x, int y, int z) throws FormatException {
    if (x < 0 || x > 15
        || y < 0 || y > 255
        || z < 0 || z > 15) {
      throw new IllegalArgumentException("Bad block address " + x + ", " +
          y + ", " + z);
    }
    Section s = getSection(y/16);
    if (s == null) {
      return 15; //full exposure to sky
    }
    y %= 16;
    return Nibble4(s.skylight, y * 16 * 16 + z * 16 + x);
  }

  /**
   * Sets the Sky Light of the block at the given coordinates, or does nothing
   * if the coordinates are invalid.
   *
   * @param x      The X coordinate of the block.
   * @param y      The Y coordinate of the block.
   * @param z      The Z coordinate of the block.
   * @param nibble The Sky Light of the block at the given coordinates.
   */
  public void SkyLight(int x, int y, int z,
                       byte nibble) throws FormatException {
    if (x < 0 || x > 15
        || y < 0 || y > 255
        || z < 0 || z > 15) {
      throw new IllegalArgumentException("Bad block address " + x + ", " +
          y + ", " + z);
    }
    Section s = createSection(y/16);
    y %= 16;
    Nibble4(s.skylight, y * 16 * 16 + z * 16 + x, nibble);
  }

  /**
   * Returns the X chunk coordinate.
   *
   * @return The X chunk coordinate.
   */
  public int PosX() {
    return object.getInteger(X_POS);
  }

  /**
   * Returns the Z chunk coordinate.
   *
   * @return The Z chunk coordinate.
   */
  public int PosZ() {
    return object.getInteger(Z_POS);
  }

  /**
   * Sets the chunk coordinates.
   *
   * @param x The X chunk coordinate.
   * @param z The Z chunk coordinate.
   * @throws IllegalArgumentException if the chunk coordinates are invalid.
   */
  public void Pos(int x, int z) throws IllegalArgumentException {
    if (x < 0 || x > 31
        || z < 0 || z > 31) {
      throw new IllegalArgumentException("Invalid Chunk Coordinates: (" + x +
          ", " + z + ")");
    }
    object.setInteger(X_POS, x);
    object.setInteger(Z_POS, z);
  }

  /**
   * Returns the tick when this chunk was last updated.
   *
   * @return The tick when this chunk was last updated.
   */
  public long LastTick() {
    return object.getLong(LAST_UPDATE);
  }

  /**
   * Sets the tick when this chunk was last updated.
   *
   * @param tick The tick when this chunk was last updated.
   */
  public void LastTick(long tick) {
    object.setLong(LAST_UPDATE, tick);
  }

  /**
   * Returns whether this chunk has been filed with special features such as ores, streams, lakes, structures, etc.
   *
   * @return Whether this chunk has been filed with special features such as ores, streams, lakes, structures, etc.
   */
  public boolean Populated() {
    return object.getByte(TERRAIN_POPULATED) != 0;
  }

  /**
   * Sets whether this chunk has been filed with special features such as ores, streams, lakes, structures, etc.
   *
   * @param populated Whether this chunk has been filed with special features such as ores, streams, lakes, structures, etc.
   */
  public void Populated(boolean populated) {
    object.setByte(TERRAIN_POPULATED, (byte) (populated ? 1 : 0));
  }

  /**
   * Returns how many ticks the chunk has been inhabited by players.
   *
   * @return how many ticks the chunk has been inhabited by players.
   */
  public long InhabitedTime() {
    return object.getLong(INHABITED_TIME);
  }

  /**
   * Sets the number of ticks this chunk has been inhabited by players.
   *
   * @param ticks the number of ticks this chunk has been inhabited by players. May be negative.
   */
  public void InhabitedTime(long ticks) {
    object.setLong(INHABITED_TIME, ticks);
  }

  /**
   * Returns the Biome ID_ATTR at the given column coordinates.
   * <br/>
   * <br/>-1	(Uncalculated)
   * <br/>0	Ocean
   * <br/>1	Plains
   * <br/>2	Desert
   * <br/>3	Extreme Hills
   * <br/>4	Forest
   * <br/>5	Taiga
   * <br/>6	Swampland
   * <br/>7	River
   * <br/>8	Hell
   * <br/>9	Sky
   * <br/>10	Frozen Ocean
   * <br/>11	Frozen River
   * <br/>12	Ice Plains
   * <br/>13	Ice Mountains
   * <br/>14	Mushroom Island
   * <br/>15	Mushroom Island Shore
   * <br/>16	Beach
   * <br/>17	Desert Hills
   * <br/>18	Forest Hills
   * <br/>19	Taiga Hills
   * <br/>20	Extreme Hills Edge
   * <br/>21	Jungle
   * <br/>22	Jungle Hills
   *
   * @param x The X coordinate of the column.
   * @param z The Z coordinate of the column.
   * @return The Biome ID_ATTR at the given column coordinates.
   * @throws IllegalArgumentException if the given coordinates are invalid.
   */
  public byte Biome(int x, int z) throws IllegalArgumentException {
    if (x < 0 || x > 15
        || z < 0 || z > 15) {
      throw new IllegalArgumentException("Invalid column coordinates: (" + x +
          ", " + z + ")");
    }
    byte[] biomes = object.getByteArray(BIOMES);
    return biomes[x * 16 + z];
  }

  /**
   * Sets the Biome ID_ATTR at the given column coordinates.
   * <br/>
   * <br/>-1	(Uncalculated)
   * <br/>0	Ocean
   * <br/>1	Plains
   * <br/>2	Desert
   * <br/>3	Extreme Hills
   * <br/>4	Forest
   * <br/>5	Taiga
   * <br/>6	Swampland
   * <br/>7	River
   * <br/>8	Hell
   * <br/>9	Sky
   * <br/>10	Frozen Ocean
   * <br/>11	Frozen River
   * <br/>12	Ice Plains
   * <br/>13	Ice Mountains
   * <br/>14	Mushroom Island
   * <br/>15	Mushroom Island Shore
   * <br/>16	Beach
   * <br/>17	Desert Hills
   * <br/>18	Forest Hills
   * <br/>19	Taiga Hills
   * <br/>20	Extreme Hills Edge
   * <br/>21	Jungle
   * <br/>22	Jungle Hills
   *
   * @param x  The X coordinate of the column.
   * @param z  The Z coordinate of the column.
   * @param id The Biome ID_ATTR.
   * @throws IllegalArgumentException if the given coordinates are invalid.
   */
  public void Biome(int x, int z, byte id) throws IllegalArgumentException {
    if (x < 0 || x > 15
        || z < 0 || z > 15) {
      throw new IllegalArgumentException("Invalid column coordinates: (" + x + ", " + z + ")");
    }
    byte[] biomes = object.getByteArray(BIOMES);
    if (biomes == null) {
      biomes = new byte[256];
      object.setByteArray(BIOMES, biomes);
    }
    biomes[x * 16 + z] = id;
  }

  /**
   * Returns the Y coordinate of the block where the light from the sky is at full strength.
   *
   * @param x The X coordinate of the column.
   * @param z The Z coordinate of the column.
   * @return The Y coordinate of the block where the light from the sky is at full strength.
   * @throws IllegalArgumentException if the given coordinates are invalid.
   */
  public int HeightMap(int x, int z) throws IllegalArgumentException {
    if (x < 0 || x > 15
        || z < 0 || z > 15) {
      throw new IllegalArgumentException("Invalid column coordinates: (" + x +
          ", " + z + ")");
    }
    int[] heightmap = object.getIntegerArray(HEIGHT_MAP);
    return heightmap[z * 16 + x];
  }

  /**
   * Sets the Y coordinate of the block where the light from the sky is at full strength.
   *
   * @param x      The X coordinate of the column.
   * @param z      The Z coordinate of the column.
   * @param height The Y coordinate of the block where the light from the sky is at full strength.
   * @throws IllegalArgumentException if the given coordinates are invalid.
   */
  public void HeightMap(int x, int z, int height) throws IllegalArgumentException {
    if (x < 0 || x > 15 || z < 0 || z > 15) {
      throw new IllegalArgumentException("Invalid column coordinates: (" + x +
          ", " + z + ")");
    }
    byte[] heightmap = object.getByteArray(HEIGHT_MAP);
    if (heightmap == null) {
      heightmap = new byte[256];
      object.setByteArray(HEIGHT_MAP, heightmap);
    }
    heightmap[z * 16 + x] = (byte) height;
  }

  /**
   * Returns the read-only list of Entities in this chunk.
   */
  public List<Entity> Entities() throws FormatException {
    Tag.ListTag entities = (Tag.ListTag) object.get(ENTITIES);
    List<Entity> result = new ArrayList<Entity>(entities.size());
    for(int i=0; i < entities.size(); ++i) {
      result.add(new Entity((Tag.Compound) entities.Get(i)));
    }
    return result;
  }

  /**
   * Returns the read-only list of Tile Entities in this chunk.
   */
  public List<TileEntity> TileEntities() {
    Tag.ListTag entities = (Tag.ListTag) object.get(TILE_ENTITIES);
    List<TileEntity> result = new ArrayList<TileEntity>(entities.size());
    for(int i=0; i < entities.size(); ++i) {
      result.add(new TileEntity((Tag.Compound) entities.Get(i)));
    }
    return result;
  }

  /**
   * Returns the read-only list of Tile Ticks in this chunk.
   */
  public List<TileTick> TileTicks() throws FormatException {
    Tag.ListTag entities = (Tag.ListTag) object.get(TILE_TICKS);
    List<TileTick> result = new ArrayList<TileTick>(entities.size());
    for(int i=0; i < entities.size(); ++i) {
      result.add(new TileTick((Tag.Compound) entities.Get(i)));
    }
    return result;
  }

  public Tag.Compound ToNBT() throws IOException {
    return object;
  }
}
