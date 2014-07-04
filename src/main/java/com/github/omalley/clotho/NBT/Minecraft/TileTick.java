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
 * Tile Tick
 */
public class TileTick
{
  /**
   * The block ID_ATTR.
   */
  private int i;
  /**
   * The number of ticks until processing should occur.
   */
  private int t;
  /**
   * The position of this tile tick.
   */
  private int x, y, z;

  /**
   * Constructs a Tile Tick object from a tile tick compound tag.
   * @param tiletick The tag from which to instantiate this tile tick.
   * @throws com.github.omalley.clotho.NBT.FormatException If the given tag is invalid.
   */
  public TileTick(Tag.Compound tiletick) throws FormatException
  {
    i = ((Tag.IntTag)tiletick.find(Tag.Type.INT, "i")).v;
    t = ((Tag.IntTag)tiletick.find(Tag.Type.INT, "t")).v;
    x = ((Tag.IntTag)tiletick.find(Tag.Type.INT, "x")).v;
    y = ((Tag.IntTag)tiletick.find(Tag.Type.INT, "y")).v;
    z = ((Tag.IntTag)tiletick.find(Tag.Type.INT, "z")).v;
  }

  /**
   * Returns the block ID_ATTR for this tile tick.
   * @return The block ID_ATTR for this tile tick.
   */
  public int BlockID()
  {
    return i;
  }
  /**
   * Sets the block ID_ATTR for this tile tick.
   * @param id The block ID_ATTR for this tile tick.
   */
  public void BlockID(int id)
  {
    i = id;
  }

  /**
   * Returns the number of ticks before this tile tick should be processed.
   * @return The number of ticks before this tile tick should be processed.
   */
  public int Ticks()
  {
    return t;
  }
  /**
   * Sets the number of ticks before this tile tick should be processed.
   * @param ticks The number of ticks before this tile tick should be processed.
   */
  public void Ticks(int ticks)
  {
    t = ticks;
  }

  /**
   * Returns the X coordinate of this tile tick.
   * @return The X coordinate of this tile tick.
   */
  public int X()
  {
    return x;
  }
  /**
   * Returns the Y coordinate of this tile tick.
   * @return The Y coordinate of this tile tick.
   */
  public int Y()
  {
    return y;
  }
  /**
   * Returns the Z coordinate of this tile tick.
   * @return The Z coordinate of this tile tick.
   */
  public int Z()
  {
    return z;
  }
  /**
   * Sets the X coordinate of this tile tick.
   * @param x The X coordinate of this tile tick.
   */
  public void X(int x)
  {
    this.x = x;
  }
  /**
   * Sets the Y coordinate of this tile tick.
   * @param y The Y coordinate of this tile tick.
   */
  public void Y(int y)
  {
    this.y = y;
  }
  /**
   * Sets the Z coordinate of this tile tick.
   * @param z The Z coordinate of this tile tick.
   */
  public void Z(int z)
  {
    this.z = z;
  }

  /**
   * Returns the tag for this tile tick.
   * @param name The name that the compound tag should have, or null if the compound tag should not have a name.
   * @return The tag for this tile tick.
   */
  public Tag.Compound ToNBT(String name)
  {
    return new Tag.Compound(name, new Tag.IntTag("i", i),
                    new Tag.IntTag("t", t),
                    new Tag.IntTag("x", x),
                    new Tag.IntTag("y", y),
                    new Tag.IntTag("z", z));
  }
}
