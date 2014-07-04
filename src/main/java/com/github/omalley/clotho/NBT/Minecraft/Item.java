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

import java.util.EnumSet;
import java.util.Set;

/**
 * Represents an Item in the Inventory.
 */
public class Item {

  static final String AUTHOR = "author";
  static final String COUNT = "Count";
  static final String DAMAGE = "Damage";
  static final String ENCHANTMENT = "ench";
  static final String ID_ATTR = "id";
  static final String LEVEL = "lvl";
  static final String SLOT = "slot";
  static final String TAG = "tag";
  static final String TITLE = "title";

  private final Tag.Compound item;

  /**
   * Constructs an inventory Item from the given tag.
   * @param item The tag from which to construct this inventory Item.
   * @throws com.github.omalley.clotho.NBT.FormatException if the given tag is invalid.
   */
  public Item(Tag.Compound item) throws FormatException {
    this.item = item;
  }

  /**
   * Constructs a new item from the given ID_ATTR, Data, and Count.
   * @param id The Item ID_ATTR.
   * @param data The data/damage value.
   * @param n The stack count.
   */
  public Item(int id, int data, int n) throws FormatException {
    this(new Tag.Compound(null));
    item.setShort(ID_ATTR, (short) id);
    item.setShort(DAMAGE, (short) data);
    item.setByte(COUNT, (byte) n);
  }

  /**
   * Returns the item ID_ATTR for this inventory Item.
   * @return The item ID_ATTR for this inventory Item.
   */
  public short getId()
  {
    return item.getShort(ID_ATTR);
  }
  /**
   * Sets the item ID_ATTR for this inventory Item.
   * @param id The item ID_ATTR for this inventory Item.
   */
  public final void setId(int id) {
    item.setShort(ID_ATTR, (short) id);
  }

  /**
   * Returns the data/damage value for this inventory Item.
   * @return The data/damage value for this inventory Item.
   */
  public short getData()
  {
    return item.getShort(DAMAGE);
  }
  /**
   * Sets the data/damage value for this inventory Item.
   * @param data The data/damage value for this inventory Item.
   */
  public void setData(int data)
  {
    item.setShort(DAMAGE, (short) data);
  }

  /**
   * Returns how many of this item is in the stack.
   * @return How many of this item is in the stack.
   */
  public byte getCount()
  {
    return item.getByte(COUNT);
  }
  /**
   * Sets how many of this item is in the stack.
   * @param n How many of this item is in the stack.
   */
  public void setCount(byte n)
  {
    item.setByte(COUNT, n);
  }

  public byte getSlot() {
    return item.getByte(SLOT);
  }

  public void setSlot(int slot) {
    item.setByte(SLOT, (byte) slot);
  }

  public Tag.Compound getTags() {
    Tag.Compound tag = (Tag.Compound) item.get(TAG);
    if (tag == null) {
      tag = new Tag.Compound(TAG);
      item.Add(tag);
    }
    return tag;
  }

  public Tag.ListTag getEnchantmentList() {
    Tag.Compound tags = getTags();
    Tag.ListTag enchant = (Tag.ListTag) tags.get(ENCHANTMENT);
    if (enchant == null) {
      enchant = new Tag.ListTag(ENCHANTMENT, Tag.Type.COMPOUND);
      tags.Add(enchant);
    }
    return enchant;
  }

  /**
   * Returns the set of all the enchantments on this item.
   * @return The set of all the enchantments on this item.
   */
  public Set<Enchantment> getEnchantments() throws FormatException {
    EnumSet<Enchantment> result = EnumSet.noneOf(Enchantment.class);
    for(Tag item: getEnchantmentList()) {
      Tag.Compound ench = (Tag.Compound) item;
      result.add(Enchantment.getEnchantment(ench.getShort(ID_ATTR)));
    }
    return result;
  }

  /**
   * Given an enchantment, returns the enchantment level, or null if the item does not have the given enchantment.
   * @param enchant The enchantment to check the level of.
   * @return The enchantment level, or null if the item does not have the given enchantment.
   */
  public Short getEnchantLevel(Enchantment enchant) {
    for(Tag item: getEnchantmentList()) {
      Tag.Compound ench = (Tag.Compound) item;
      if (ench.getShort(ID_ATTR) == enchant.ID()) {
        return ench.getShort(LEVEL);
      }
    }
    return null;
  }

  /**
   * Sets the level for the given enchantment, or removes it if the given level is null.
   * @param enchant The enchantment to change the level of.
   * @param level The new level for the enchantment, or null if the enchantment should be removed.
   */
  public void setEnchantLevel(Enchantment enchant, Short level) {
    Tag.ListTag enchantments = getEnchantmentList();
    for(int i=0; i < enchantments.size(); ++i) {
      Tag.Compound ench = (Tag.Compound) enchantments.Get(i);
      if (ench.getShort(ID_ATTR) == enchant.ID()) {
        if (level == null) {
          enchantments.Remove(i);
        } else {
          ench.setShort(LEVEL, level);
        }
        return;
      }
    }
  }

  /**
   * Returns the title of this book.
   * @return The title of this book.
   */
  public String getTitle() {
    return getTags().getString(TITLE);
  }
  /**
   * Sets the title of this book.
   * @param name The title of this book.
   * @throws UnsupportedOperationException if this item is not a published book.
   */
  public void Title(String name) throws FormatException {
    if(getId() != ID.WRITTEN_BOOK.id) {
      throw new UnsupportedOperationException("This item is not a published book.");
    }
    getTags().setString(TITLE, name);
  }

  /**
   * Returns the author of this book.
   * @return The author of this book.
   */
  public String getAuthor() {
    return getTags().getString(AUTHOR);
  }
  /**
   * Sets the author of this book.
   * @param name The author of this book.
   * @throws UnsupportedOperationException if this item is not a published book.
   */
  public void setAuthor(String name) throws FormatException {
    if(getId() != ID.WRITTEN_BOOK.id) {
      throw new UnsupportedOperationException("This item is not a published book.");
    }
    getTags().setString(AUTHOR, name);
  }

  /**
   * Returns the tag for this inventory Item.
   * @return The tag for this inventory Item.
   */
  public Tag.Compound ToNBT() {
    return item;
  }
}
