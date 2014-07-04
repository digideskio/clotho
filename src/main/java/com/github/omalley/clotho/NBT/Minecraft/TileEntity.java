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
 * The Tile Entity class.
 */
public class TileEntity {
  static final String BLOCK_DATA = "blockData";
  static final String BLOCK_ID = "blockId";
  static final String BURN_TIME = "BurnTime";
  static final String COOK_TIME = "CookTime";
  static final String DATA = "Data";
  static final String DELAY = "Delay";
  static final String ENTITY_ID = "EntityId";
  static final String EXTENDING = "extending";
  static final String FACING = "facing";
  static final String ID = "id";
  static final String ITEM = "Item";
  static final String ITEMS = "Items";
  static final String MAX_SPAWN_DELAY = "MaxSpawnDelay";
  static final String MIN_SPAWN_DELAY = "MinSpawnDelay";
  static final String NOTE = "note";
  static final String PROGRESS = " progress";
  static final String RECORD = "Record";
  static final String SPAWN_COUNT = "SpawnCount";
  static final String TEXT_1 = "Text1";
  static final String TEXT_2 = "Text2";
  static final String TEXT_3 = "Text3";
  static final String TEXT_4 = "Text4";
  static final String X = "x";
  static final String Y = "y";
  static final String Z = "z";

	protected final Tag.Compound entity;

  public static TileEntity createEntity(Tag.Compound entity
                                        ) throws FormatException {
    String id = entity.getString(ID);
    switch(id)
    {
      case "Furnace":
        return new Furnace(entity);
      case "Sign":
        return new Sign(entity);
      case "MobSpawner":
        return new MobSpawner(entity);
      case "Chest":
        return new Chest(entity);
      case "Music":
        return new Music(entity);
      case "Trap":
        return new Trap(entity);
      case "RecordPlayer":
        return new RecordPlayer(entity);
      case "Piston":
        return new Piston(entity);
      case "Cauldron":
        return new Cauldron(entity);
      case "EnchantTable":
        return new EnchantTable(entity);
      case "Airportal":
        return new Airportal(entity);
      case "FlowerPot":
        return new FlowerPot(entity);
    }
    return new TileEntity(entity);
  }

	/**
	 * Constructs a tile entity from a compound tag.
	 * @param tileentity The tag from which to instantiate this tile entity.
	 * @throws FormatException if the given tag is invalid.
	 */
	public TileEntity(Tag.Compound tileentity) {
    this.entity = tileentity;
	}

	/**
	 * Returns the tag for this tile entity.
	 * @return The tag for this tile entity.
	 */
	public final Tag.Compound toNBT() {
    return entity;
	}

	/**
	 * The tile entity for furnaces to store their items and smelting state.
	 */
	public static class Furnace extends TileEntity {

		/**
		 * Constructs a Furnace tile entity from a tile entity tag.
		 * @param furnace The tag from which to construct this Furnace tile entity.
		 * @throws FormatException if the tag is invalid.
		 */
		public Furnace(Tag.Compound furnace) throws FormatException {
			super(furnace);
		}

    public Tag.ListTag getItems() {
      Tag.ListTag items = (Tag.ListTag) entity.get(ITEMS);
      if (items == null) {
        items = new Tag.ListTag(ITEMS, Tag.Type.COMPOUND);
        entity.Add(items);
      }
      return items;
    }

		/**
		 * Returns the item being smelted.
		 * @return The item being smelted.
		 */
		public Item getSmelting() throws FormatException {
			Tag.ListTag items = getItems();
      if (items.size() >= 1) {
        return new Item((Tag.Compound) items.Get(0));
      }
      return null;
		}

		/**
		 * Sets the item being smelted.
		 * @param item The item being smelted.
		 */
		public void setSmelting(Item item) throws FormatException {
      Tag.ListTag items = getItems();
      if (items.size() >= 1) {
         items.Set(0, item.ToNBT());
      }
      items.Add(item.ToNBT());
		}

		/**
		 * Returns the item to be used as fuel.
		 * @return The item to be used as fuel.
		 */
		public Item getFuel() throws FormatException {
      Tag.ListTag items = getItems();
      if (items.size() >= 2) {
        return new Item((Tag.Compound) items.Get(1));
      }
      return null;
		}

		/**
		 * Sets the item to be used as fuel.
		 * @param item The item to be used as fuel.
		 */
		public void setFuel(Item item) throws FormatException {
      Tag.ListTag items = getItems();
      if (items.size() >= 2) {
        items.Set(1, item.ToNBT());
      }
      items.Add(item.ToNBT());
		}

		/**
		 * Returns the result item.
		 * @return The result item.
		 */
		public Item getResult() throws FormatException {
      Tag.ListTag items = getItems();
      if (items.size() >= 3) {
        return new Item((Tag.Compound) items.Get(2));
      }
      return null;
		}

		/**
		 * Sets the result item.
		 * @param item The result item.
		 */
		public void setResult(Item item) throws FormatException {
      Tag.ListTag items = getItems();
      if (items.size() >= 3) {
        items.Set(2, item.ToNBT());
      }
      items.Add(item.ToNBT());
		}
	}

  public static class FlowerPot extends TileEntity {
    public FlowerPot(Tag.Compound entity) {
      super(entity);
    }
  }

	/**
	 * The tile entity for signs to store their text.
	 */
	public static class Sign extends TileEntity {

		/**
		 * Constructs a Sign tile entity from the given tag.
		 * @param sign The tag from which to construct this Sign tile entity.
		 */
		public Sign(Tag.Compound sign) {
			super(sign);
		}

		/**
		 * Retrieves a line of text from the sign.
		 * @param line The line number to get, ranged 0 to 3.
		 * @return The text on that line.
		 */
		public String getLine(int line) {
			switch(line) {
			case 0:	return entity.getString(TEXT_1);
			case 1: return entity.getString(TEXT_2);
			case 2: return entity.getString(TEXT_3);
			case 3: return entity.getString(TEXT_4);
			}
			throw new IllegalArgumentException("Line numbers are 0 to 3; given "+line);
		}

		/**
		 * Sets the text of a line on the sign.
		 * @param line The line number to set, ranged 0 to 3.
		 * @param text The text to set the line to.
		 */
		public void Line(int line, String text) {
			switch(line) {
			case 0:
        entity.setString(TEXT_1, text);
        break;
			case 1:
        entity.setString(TEXT_2, text);
        break;
			case 2:
        entity.setString(TEXT_3, text);
        break;
			case 3:
        entity.setString(TEXT_4, text);
        break;
      default:
        throw new IllegalArgumentException("Line numbers are 0 to 3; given "
            + line);
			}
		}
	}

	/**
	 * The tile entity used by Monster Spawners to store which mob they spawn and how many ticks there are until the next spawn.
	 */
	public static class MobSpawner extends TileEntity {

		/**
		 * Constructs a Mob Spawner tile entity from the given tag.
		 * @param mobspawner The tag from which to construct this Mob Spawner.
		 * @throws FormatException if the given tag is invalid.
		 */
		public MobSpawner(Tag.Compound mobspawner) {
      super(mobspawner);
    }
	}

	/**
	 * The tile entity used by Chests to store their items.
	 */
	public static class Chest extends TileEntity {

		/**
		 * Constructs a Chest tile entity from the given tag.
		 * @param chest The tag from which to construct the Chest tile entity.
		 * @throws FormatException if the given tag is invalid.
		 */
		public Chest(Tag.Compound chest) {
			super(chest);
		}
	}

	/**
	 * The tile entity used by Note Blocks to store which note they play.
	 * <p>
	 * Why this isn't simply in the block data value is a mystery.
	 */
	public static class Music extends TileEntity {

		/**
		 * Constructs a Music tile entity from a tag.
		 * @param music The tag from which to construct the Music tile entity.
		 * @throws FormatException if the tag is invalid.
		 */
		public Music(Tag.Compound music) {
			super(music);
		}
	}

	/**
	 * The tile entity used by Dispensers to store the items they contain.
	 */
	public static class Trap extends TileEntity {
		public Trap(Tag.Compound trap) {
			super(trap);
		}
  }

	/**
	 * The tile entity used by Jukeboxes to store the record/item ID_ATTR they contain.
	 * <p>
	 * Why this isn't simply in the block data value is a mystery.
	 */
	public static class RecordPlayer extends TileEntity {

		/**
		 * Constructs a Record Player tile entity from the given tag.
		 * @param recordplayer The tag from which to construct the Record Player tile entity.
		 * @throws FormatException if the given tag is invalid.
		 */
		public RecordPlayer(Tag.Compound recordplayer) {
			super(recordplayer);
		}
	}

	/**
	 * The tile entity used by block 36, the block being moved by a piston.
	 */
	public static class Piston extends TileEntity {

		/**
		 * Represents a direction for blocks being moved by pistons.
		 */
		public static enum Facing {
			Down(0), Up(1),
			North(2), South(3), East(4), West(5);

      final int id;
      private Facing(int id) {
        this.id = id;
      }

			/**
			 * Converts an ordinal to the Facing it represents.
			 * @param ordinal The facing as a number.
			 * @return The Facing represented by the given ordinal.
			 * @throws FormatException If the ordinal does not represent a valid facing.
			 */
			public static Facing fromOrdinal(int ordinal) throws FormatException {
        for(Facing f: values()) {
          if (f.id == ordinal) {
            return f;
          }
        }
				throw new FormatException("Unknown direction: "+ordinal);
			}
		}

		/**
		 * Constructs a Piston tile entity from the given tag.
		 * @param piston The tag from which to construct the Piston tile entity.
		 * @throws FormatException if the given tag is invalid.
		 */
		public Piston(Tag.Compound piston) {
			super(piston);
		}

		/**
		 * Returns the ID_ATTR of the block being moved.
		 * @return The ID_ATTR of the block being moved.
		 */
		public int getBlockID()
		{
			return entity.getInteger(BLOCK_ID);
		}
		/**
		 * Sets the ID_ATTR of the block being moved.
		 * @param id The ID_ATTR of the block being moved.
		 */
		public void setBlockID(int id)
		{
			entity.setInteger(BLOCK_ID, id);
		}

		/**
		 * Returns the data/damage value of the block being moved.
		 * @return The data/damage value of the block being moved.
		 */
		public int getBlockData()
		{
			return entity.getInteger(BLOCK_DATA);
		}
		/**
		 * Sets the data/damage value of the block being moved.
		 * @param data The data/damage value of the block being moved.
		 */
		public void BlockData(int data)
		{
			entity.setInteger(BLOCK_DATA, data);
		}

		/**
		 * Returns the direction the block is being moved.
		 * @return The direction the block is being moved.
		 */
		public Facing getFacing() throws FormatException {
			return Facing.fromOrdinal(entity.getInteger(FACING));
		}

		/**
		 * Sets the direction the block is being moved.
		 * @param direction The direction the block is being moved.
		 */
		public void setFacing(Facing direction) {
			entity.setInteger(FACING, direction.id);
		}

		/**
		 * Returns the progress of moving the block so far.
		 * @return The progress of moving the block so far.
		 */
		public float getProgress()
		{
			return entity.getFloat(PROGRESS);
		}
		/**
		 * Sets the progress of moving the block so far.
		 * @param progress The progress of moving the block so far.
		 */
		public void setProgress(float progress)
		{
			entity.setFloat(PROGRESS, progress);
		}

		/**
		 * Returns whether or not the block is being pushed.
		 * @return Whether or not the block is being pushed.
		 */
		public boolean Extending()
		{
			return entity.getByte(EXTENDING) != 0;
		}
		/**
		 * Sets whether or not the block is being pushed.
		 * @param notretracting Whether or not the block is being pushed.
		 */
		public void Extending(boolean notretracting)
		{
			entity.setByte(EXTENDING, (byte) (notretracting ? 1 : 0));
		}

	}

	/**
	 * The tile entity used by Brewing Stands to store their items and brewing
   * progress.
	 */
	public static class Cauldron extends TileEntity {

    /**
     * Constructs a Cauldron tile entity from the given tag.
     *
     * @param cauldron The tag from which to  construct the Cauldron tile entity.
     * @throws FormatException if the tag is invalid.
     */
    public Cauldron(Tag.Compound cauldron) {
      super(cauldron);
    }
  }

	/**
	 * The tile entity used by Enchantment Tables to keep track of their rotation and opening/closing, only used at runtime.
	 */
	public static class EnchantTable extends TileEntity {
		/**
		 * Constructs an Enchantment Table tile entity from the given tag.
		 * @param enchanttable The tag from which to construct this Enchantment Table tile entity.
		 * @throws FormatException if the tag is invalid.
		 */
		public EnchantTable(Tag.Compound enchanttable) throws FormatException
		{
			super(enchanttable);
		}
	}

	/**
	 * The tile entity used by End Portal blocks to have that cool paralaxing look, only used at runtime.
	 */
	public static class Airportal extends TileEntity {
		/**
		 * Constructs an End Portal tile entity from the given tag.
		 * @param airportal The tag from which to construct this End Portal tile entity.
		 * @throws FormatException if the tag is invalid.
		 */
		public Airportal(Tag.Compound airportal) throws FormatException
		{
			super(airportal);
		}
	}
}
