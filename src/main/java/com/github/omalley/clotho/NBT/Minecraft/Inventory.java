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

import com.github.omalley.clotho.NBT.Tag;
import com.github.omalley.clotho.NBT.FormatException;

/**
 * The class used for various inventories.
 */
public class Inventory {

  private final Tag.ListTag slots;

	/**
	 * Constructs an inventory from the given tag.
	 * @param inventory The tag from which to construct this Inventory.
	 * @throws FormatException if the given tag is invalid.
	 */
	public Inventory(Tag.ListTag inventory) {
    this.slots = inventory;
	}

	/**
	 * Returns the item at the given slot, or null if there is no item in that slot. Slot numbers must be in the range 0 to 127, inclusive.
	 * @param slot The slot number of the item, in the range 0 to 127 inclusive.
	 * @return The item at the given slot, or null if there is no item in that slot.
	 */
	public Item getItem(int slot) throws FormatException {
    for(int i=0; i < slots.size(); ++i) {
      Tag.Compound item = (Tag.Compound) slots.Get(i);
      if (item.getByte(Item.SLOT) == slot) {
        return new Item(item);
      }
    }
		return null;
	}

	/**
	 * Sets the item at the given slot, or null to remove the item at that slot. Slot numbers must be in the range 0 to 127, inclusive.
	 * @param slot The slot number of the item, in the range 0 to 127 inclusive.
	 * @param newItem The item at the given slot, or null to remove the item at that slot.
	 * @throws IllegalArgumentException if the given slot number is not within the range 0 to 127.
	 */
	public void Item(int slot, Item newItem) throws FormatException {
    for(int i=0; i < slots.size(); ++i) {
      Tag.Compound item = (Tag.Compound) slots.Get(i);
      if (item.getByte(Item.SLOT) == slot) {
        if (newItem == null) {
          slots.Remove(i);
        } else {
          newItem.setSlot(slot);
          slots.Set(i, newItem.ToNBT());
        }
      }
    }
    newItem.setSlot(slot);
    slots.Add(newItem.ToNBT());
	}

	/**
	 * Returns the tag for this Inventory.
	 * @return The tag for this Inventory.
	 */
	public Tag.ListTag ToNBT() {
    return slots;
	}
}
