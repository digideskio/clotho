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
 * An enumeration class to represent the current known enchantments.
 */
public enum Enchantment
{
  Protection(0), FireProtection(1), FeatherFalling(2), BlastProtection(3),
  ProjectileProtection(4), Respiration(5), AquaAffinity(6),
  Sharpness(16), Smite(17), BaneOfArthropods(18), Knockback(19),
  FireAspect(20), Looting(21),
  Efficiency(32), SilkTouch(33), Unbreaking(34), Fortune(35),
  Power(48), Punch(49), Flame(50), Infinity(51),
  LuckOfTheSea(61), Lure(62);

private final short id;
Enchantment(int id) {
this.id = (short) id;
}
  /**
   * Given an enchantment ID_ATTR, returns the corresponding
* <code>Enchantment</code>.
   * @param ID The ID_ATTR of the enchantment.
   * @return The <code>Enchantment</code> that corresponds to the given ID_ATTR.
   * @throws com.github.omalley.mapper.NBT.FormatException if the given ID_ATTR does not correspond to any
* known enchantment.
   */
  public static Enchantment getEnchantment(short ID) throws FormatException {
for(Enchantment value: values()) {
if (ID == value.id) {
return value;
}
}
    throw new FormatException("Unknown Enchantment ID_ATTR: "+ID);
  }

  /**
   * Returns the ID_ATTR of this <code>Enchantment</code>.
   * @return The ID_ATTR of this <code>Enchantment</code>.
   */
  public short ID() {
return id;
  }
}
