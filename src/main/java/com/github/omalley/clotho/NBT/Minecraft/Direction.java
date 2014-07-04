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

/**
 * Represents the direction the Painting is facing.
 */
public enum Direction {
  East(0), North(1), West(2), South(3);

  private final byte id;

  private Direction(int id) {
    this.id = (byte) id;
  }

  /**
   * Returns the Direction constant that corresponds to the given ordinal.
   * @param ordinal The direction value of the Painting.
   * @return The Direction constant that corresponds to the given ordinal.
   * @throws com.github.omalley.clotho.NBT.FormatException if the ordinal is invalid.
   */
  public static Direction FromOrdinal(byte ordinal) throws FormatException {
    for(Direction dir: values()) {
      if (dir.id == ordinal) {
        return dir;
      }
    }
    throw new FormatException("Invalid Painting Direction: " + ordinal);
  }

  public byte getId() {
    return id;
  }
}
