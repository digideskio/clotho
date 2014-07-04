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

/**
 * Section
 */
public class Section {
  int y;
  /**
   * The blocks in this section.
   */
  byte[] blocks, add, data;
  /**
   * The light in this section.
   */
  byte[] blocklight, skylight;

  /**
   * Constructs a Section from the given tag.
   * @param section The tag fromm which to construct this Section.
   * @throws com.github.omalley.clotho.NBT.FormatException if the given tag is invalid.
   */
  public Section(Tag.Compound section) throws FormatException
  {
    blocks = ((Tag.ByteArray)section.find(Tag.Type.BYTEARRAY, "Blocks")).v;
    try
    {
      add = ((Tag.ByteArray)section.find(Tag.Type.BYTEARRAY, "Add")).v;
    }
    catch(FormatException e)
    {
      add = new byte[2048];
    }
    data = ((Tag.ByteArray)section.find(Tag.Type.BYTEARRAY, "Data")).v;
    blocklight = ((Tag.ByteArray)section.find(Tag.Type.BYTEARRAY, "BlockLight")).v;
    skylight = ((Tag.ByteArray)section.find(Tag.Type.BYTEARRAY, "SkyLight")).v;
  }
  /**
   * Constructs an empty Section.
   */
  public Section(int y)
  {
    this.y = y;
    blocks = new byte[4096];
    add = new byte[2048];
    data = new byte[2048];
    blocklight = new byte[2048];
    skylight = new byte[2048];
    for(int i = 0; i < 2048; ++i)
    {
      skylight[i] = 15; //full exposure to sky
    }
  }

  /**
   * Returns whether this section has only air blocks.
   * @return Whether this section has only air blocks.
   */
  public boolean Empty()
  {
    for(byte block : blocks)
    {
      if(block != 0)
      {
        return false;
      }
    }
    for(byte block : add)
    {
      if(block != 0)
      {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns the tag for this section.
   * @param name The name that the compound tag should have, or null if the compound tag should not have a name.
   * @return The tag for this section.
   */
  public Tag.Compound ToNBT(String name)
  {
    Tag.Compound t = new Tag.Compound(name,
        new Tag.ByteTag("Y", (byte) y),
        new Tag.ByteArray("Blocks", blocks),
        new Tag.ByteArray("Data", data),
        new Tag.ByteArray("BlockLight", blocklight),
        new Tag.ByteArray("SkyLight", skylight));
    for(byte b : add)
    {
      if(b != 0)
      {
        t.Add(new Tag.ByteArray("Add", add));
        break;
      }
    }
    return t;
  }

  /**
   * Returns the ID_ATTR of the block at the given coordinates.
   *
   * @param x The X coordinate of the block.
   * @param y The Y coordinate of the block.
   * @param z The Z coordinate of the block.
   * @return The ID_ATTR of the block at the given coordinates, or -1 if the
   * coordinates are invalid.
   */
  public short getBlockID(int x, int y, int z) throws FormatException {
    if (x < 0 || x > 15 || y < 0 || y > 16 || z < 0 || z > 15) {
      throw new IllegalArgumentException("Out of range block " + x + "," + y +
          "," + z);
    }
    return (short) ((blocks[y * 16 * 16 + z * 16 + x] & 0xff) +
        (Chunk.Nibble4(add, y * 16 * 16 + z * 16 + x) << 8));
  }

  /**
   * Sets the ID_ATTR of the block at the given coordinates.
   *
   * @param x  The X coordinate of the block.
   * @param y  The Y coordinate of the block.
   * @param z  The Z coordinate of the block.
   * @param id The ID_ATTR of the block at the given coordinates.
   */
  public void setBlockID(int x, int y, int z, short id) throws FormatException {
    if (x < 0 || x > 15 || y < 0 || y > 16 || z < 0 || z > 15) {
      throw new IllegalArgumentException("Out of range block " + x + "," + y +
          "," + z);
    }
    blocks[y * 16 * 16 + z * 16 + x] = (byte) id;
    Chunk.Nibble4(add, y * 16 * 16 + z * 16 + x, (byte) (id >> 8));
  }

}
