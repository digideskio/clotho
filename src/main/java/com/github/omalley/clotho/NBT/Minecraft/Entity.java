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
 * The class extended by the various entity classes in Minecraft.
 */
public class Entity {
  static final String AGE = "Age";
  static final String AIR = "Air";
  static final String DAMAGE = "damage";
  static final String DIRECTION = "Dir";
  static final String FALL_DISTANCE = "FallDistance";
  static final String FIRE = "Fire";
  static final String FUEL = "Fuel";
  static final String FUSE = "Fuse";
  static final String HEALTH = "Health";
  static final String IN_DATA = "indata";
  static final String IN_GROUND = "inGround";
  static final String IN_TILE = "inTile";
  static final String ITEMS = "Items";
  static final String MOTION = "Motion";
  static final String MOTIVE = "Motive";
  static final String ON_GROUND = "OnGround";
  static final String PICKUP = "pickup";
  static final String POSITION = "Pos";
  static final String PUSH_X = "PushX";
  static final String PUSH_Z = "PushZ";
  static final String ROTATION = "Rotation";
  static final String SHAKE = "shake";
  static final String TILE = "Tile";
  static final String TILE_X = "TileX";
  static final String TILE_Y = "TileY";
  static final String TILE_Z = "TileZ";
  static final String TYPE = "Type";
  static final String VALUE = "Value";
  static final String X_TILE = "xTile";
  static final String Y_TILE = "yTile";
  static final String Z_TILE = "zTile";

  protected final Tag.Compound entity;

	/**
	 * Given an Entity's tag, returns the Class object for the class that represents that Entity ID_ATTR.
	 * @param entity The tag that represents the entity.
	 * @return The Class object for the class that represents the Entity ID_ATTR.
	 * @throws FormatException if the given Tile Entity ID_ATTR is unknown.
	 */
	public static Entity create(Tag.Compound entity) throws FormatException {
		String ID = ((Tag.StringTag)entity.find(Tag.Type.STRING, "id")).v;
		switch(ID) {
		case "Item":
      return new DroppedItem(entity);
		case "Painting":
      return new Painting(entity);
		case "XPOrb":
      return new XPOrb(entity);
		case "Minecart":
      switch(entity.getInteger(TYPE)) {
        case 0:
          return new Minecart(entity);
        case 1:
          return new Minecart.StorageMinecart(entity);
        case 2:
          return new Minecart.Furnace(entity);
      }
      throw new FormatException("Invalid Minecart Type: "+
          entity.getInteger(TYPE));
		case "Boat":
      return new Boat(entity);
		case "PrimedTnt":
      return new PrimedTnt(entity);
		case "FallingSand":
      return new FallingSand(entity);
    case "Villager":
      return new Mob.Villager(entity);
		}
    return new Entity(entity);
	}

	/**
	 * The constructor required to instantiate any class that extends entity.
	 * @param entity The entity's tag. This tag will not be affected by the entity class object at any time.
	 * @throws FormatException if there is something wrong with the format of the entity's tag.
	 */
	public Entity(Tag.Compound entity) throws FormatException {
    this.entity = entity;
	}

	/**
	 * Constructs an entity from a position.
	 * @param X The X position.
	 * @param Y The Y position.
	 * @param Z The Z position.
	 */
	public Entity(double X, double Y, double Z) throws FormatException {
    entity = new Tag.Compound(null);
    entity.setDoubleList(POSITION, X, Y, Z);
    entity.setDoubleList(MOTION, 0, 0 , 0);
    entity.setFloatList(ROTATION, 0, 0);
    entity.setFloat(FALL_DISTANCE, 0.0f);
    entity.setShort(FIRE, (short) 0);
    entity.setShort(AIR, (short) 0);
    entity.setByte(ON_GROUND, (byte) 0);
	}

	/**
	 * Returns the position of this entity.
	 * @return The position of this entity.
	 */
	public Tag.ListTag getPos()
	{
		return (Tag.ListTag) entity.get(POSITION);
	}

	/**
	 * Returns the X position of this entity.
	 * @return The X position of this entity.
	 */
	public double PosX()
	{
		return getPos().getDouble(0);
	}
	/**
	 * Returns the Y position of this entity.
	 * @return The Y position of this entity.
	 */
	public double PosY()
	{
		return getPos().getDouble(1);
	}
	/**
	 * Returns the Z position of this entity.
	 * @return The Z position of this entity.
	 */
	public double PosZ()
	{
		return getPos().getDouble(2);
	}

	/**
	 * Sets the X position of this entity.
	 * @param x The new X position of this entity.
	 */
	public void PosX(double x) {
		getPos().setDouble(0, x);
	}
	/**
	 * Sets the Y position of this entity.
	 * @param y The new Y position of this entity.
	 */
	public void PosY(double y) {
		getPos().setDouble(1, y);
	}
	/**
	 * Sets the Z position of this entity.
	 * @param z The new Z position of this entity.
	 */
	public void PosZ(double z){
		getPos().setDouble(2, z);
	}

	/**
	 * Returns the motion of this entity.
	 * @return The motion of this entity.
	 */
	public Tag.ListTag getMotion() {
		return (Tag.ListTag) entity.get(MOTION);
	}

	/**
	 * Returns the X motion of this entity.
	 * @return The X motion of this entity.
	 */
	public double MotionX()
	{
		return getMotion().getDouble(0);
	}

	/**
	 * Returns the Y motion of this entity.
	 * @return The Y motion of this entity.
	 */
	public double MotionY()
	{
		return getMotion().getDouble(1);
	}

	/**
	 * Returns the Z motion of this entity.
	 * @return The Z motion of this entity.
	 */
	public double MotionZ()
	{
		return getMotion().getDouble(2);
	}

	/**
	 * Sets the X motion of this entity.
	 * @param x The new X motion of this entity.
	 */
	public void MotionX(double x)
	{
		getMotion().setDouble(0, x);
	}
	/**
	 * Sets the Y motion of this entity.
	 * @param y The new Y motion of this entity.
	 */
	public void MotionY(double y)
	{
		getMotion().setDouble(1, y);
	}
	/**
	 * Sets the Z motion of this entity.
	 * @param z The new Z motion of this entity.
	 */
	public void MotionZ(double z)
	{
		getMotion().setDouble(2, z);
	}

	/**
	 * Returns the rotation of this entity.
	 * @return The rotation of this entity.
	 */
	public Tag.ListTag getRotation() {
		return (Tag.ListTag) entity.get(ROTATION);
	}

	/**
	 * Returns the Yaw of this entity.
	 * @return The Yaw of this entity.
	 */
	public float Yaw()
	{
		return getRotation().getFloat(0);
	}
	/**
	 * Returns the Pitch of this entity.
	 * @return The Pitch of this entity.
	 */
	public float Pitch()
	{
		return getRotation().getFloat(1);
	}

	/**
	 * Sets the yaw for this entity.
	 * @param yaw The yaw for this entity.
	 */
	public void Yaw(float yaw)
	{
		getRotation().setFloat(0, yaw);
	}
	/**
	 * Sets the pitch for this entity.
	 * @param pitch The pitch for this entity.
	 */
	public void Pitch(float pitch)
	{
		getRotation().setFloat(1, pitch);
	}

	/**
	 * Returns the fall distance for this entity.
	 * @return The fall distance for this entity.
	 */
	public float FallDistance()
	{
		return entity.getFloat(FALL_DISTANCE);
	}
	/**
	 * Sets the fall distance for this entity.
	 * @param distance The fall distance for this entity.
	 */
	public void FallDistance(float distance) {
		entity.setFloat(FALL_DISTANCE, distance);
	}

	/**
	 * Returns the number of ticks before this entity's fire is extinguished.
	 * @return The number of ticks before this entity's fire is extinguished.
	 */
	public short FireTicks()
	{
		return entity.getShort(FIRE);
	}
	/**
	 * Sets she number of ticks before this entity's fire is extinguished.
	 * @param ticks The number of ticks before this entity's fire is extinguished.
	 */
	public void FireTicks(short ticks)
	{
		entity.setShort(FIRE, ticks);
	}

	/**
	 * Returns the number of ticks before this entity begins to drown.
	 * @return The number of ticks before this entity begins to drown.
	 */
	public short AirTicks()
	{
		return entity.getShort(AIR);
	}
	/**
	 * Sets the number of ticks before this entity begins to drown.
	 * @param ticks The number of ticks before this entity begins to drown.
	 */
	public void AirTicks(short ticks)
	{
		entity.setShort(AIR, ticks);
	}

	/**
	 * Returns whether or not this entity is on the ground.
	 * @return Whether or not this entity is on the ground.
	 */
	public boolean getOnGround() {
		return entity.getByte(ON_GROUND) != 0;
	}
	/**
	 * Sets whether or not this entity is on the ground.
	 * @param onGround Whether or not this entity is on the ground.
	 */
	public void setOnGround(boolean onGround) {
		entity.setByte(ON_GROUND, onGround ? (byte) 1 : 0);
	}

	/**
	 * Converts this entity to its tag.
	 * <p>
	 * Should be overridden and called by classes that extend this class.
	 * @param name The name for the compound tag, or null if the compound tag should not have a name.
	 * @return The tag for this entity.
	 */
	public Tag.Compound ToNBT(String name) {
		return entity;
	}

  /**
	 * Boat entity
	 */
	public static class Boat extends Entity {
		/**
		 * Constructs a Boat entity from the given tag.
		 * @param boat The tag from which to construct this Boat entity.
		 * @throws FormatException if the given tag is invalid.
		 */
		public Boat(Tag.Compound boat) throws FormatException {
			super(boat);
		}
		/**
		 * Constructs a Boat from a position.
		 * @param X The X position.
		 * @param Y The Y position.
		 * @param Z The Z position.
		 */
		public Boat(double X, double Y, double Z) throws FormatException {
			super(X, Y, Z);
		}
	}

	/**
	 * Minecart entities
	 */
	public static class Minecart extends Entity {
		/**
		 * Constructs a Minecart entity from the given tag.
		 * @param minecart The tag from which to construct this Minecart entity.
		 * @throws FormatException if the given tag is invalid.
		 */
		public Minecart(Tag.Compound minecart) throws FormatException {
			super(minecart);
		}
		/**
		 * Constructs a Minecart from a position.
		 * @param X The X position.
		 * @param Y The Y position.
		 * @param Z The Z position.
		 */
		public Minecart(double X, double Y, double Z) throws FormatException {
			super(X, Y, Z);
      entity.setInteger("Type", 0);
		}
	}

  /**
   * Storage Minecart
   */
  public static class StorageMinecart extends Minecart {

    /**
     * Constructs a Storage Minecart entity from the given tag.
     * @param mcwchest The tag from which to construct this Storage Minecart entity.
     * @throws FormatException if the give tag is invalid.
     */
    public StorageMinecart(Tag.Compound mcwchest) throws FormatException {
      super(mcwchest);
    }
    /**
     * Constructs a Storage Minecart from a position.
     * @param X The X position.
     * @param Y The Y position.
     * @param Z The Z position.
     */
    public StorageMinecart(double X, double Y, double Z) throws FormatException
    {
      super(X, Y, Z);
      entity.setInteger("Type", 1);
    }

    /**
     * Returns the item at the specified slot.
     * @param slot The slot from which to get the item, range 0 to 26.
     * @return The item at the specified slot.
     */
    public Item getItem(int slot) throws FormatException {
      Tag.ListTag items = (Tag.ListTag) entity.get(ITEMS);
      for(int i=0; i < items.size(); ++i) {
        Tag.Compound child = (Tag.Compound) items.Get(i);
        if (child.getByte(Item.SLOT) == slot) {
          return new Item(child);
        }
      }
      return null;
    }
    /**
     * Sets the item at the specified slot.
     * @param slot The slot to set the new item to, range 0 to 26.
     * @param item The item to set to the slot.
     */
    public void setItem(int slot, Item item) throws FormatException {
      Tag.ListTag items = (Tag.ListTag) entity.get(ITEMS);
      for(int i=0; i < items.size(); ++i) {
        Tag.Compound child = (Tag.Compound) items.Get(i);
        if (child.getByte(Item.SLOT) == slot) {
          item.setSlot(slot);
          items.Set(i, item.ToNBT());
        }
      }
      item.setSlot(slot);
      items.Add(item.ToNBT());
    }
  }

  /**
   * Powered Minecart
   */
  public static class Furnace extends Minecart {

    /**
     * Constructs a Powered Minecart entity from the given tag.
     * @param mcwfurnace The tag from which to construct this Powered Minecart entity.
     * @throws FormatException if the given tag is invalid.
     */
    public Furnace(Tag.Compound mcwfurnace) throws FormatException {
      super(mcwfurnace);
    }
    /**
     * Constructs a Powered Minecart from a position.
     * @param X The X position.
     * @param Y The Y position.
     * @param Z The Z position.
     */
    public Furnace(double X, double Y, double Z) throws FormatException {
      super(X, Y, Z);
      entity.setInteger(TYPE, 2);
      entity.setDouble(PUSH_X, 0.0);
      entity.setDouble(PUSH_Z, 0.0);
      entity.setShort(FUEL, (short) 0);
    }

    /**
     * Returns the X Push factor.
     * @return The X Push factor.
     */
    public double getPushX()
    {
      return entity.getDouble(PUSH_X);
    }
    /**
     * Returns the Z Push factor.
     * @return The Z Push factor.
     */
    public double getPushZ()
    {
      return entity.getDouble(PUSH_Z);
    }
    /**
     * Sets the X Push factor.
     * @param push The X Push factor.
     */
    public void setPushX(double push)
    {
      entity.setDouble(PUSH_X, push);
    }
    /**
     * Sets the Z Push factor.
     * @param push The Z Push factor.
     */
    public void setPushZ(double push)
    {
      entity.setDouble(PUSH_Z, push);
    }

    /**
     * Returns the number of ticks before this Powered Minecart runs out of fuel.
     * @return The number of ticks before this Powered Minecart runs out of fuel.
     */
    public short getFuel()
    {
      return entity.getShort(FUEL);
    }
    /**
     * Sets the number of ticks before this Powered Minecart runs out of fuel.
     * @param ticks The number of ticks before this Powered Minecart runs out of fuel.
     */
    public void setFuel(short ticks)
    {
      entity.setShort(FUEL, ticks);
    }
  }

	/**
	 * TNT dynamic tile entity.
	 */
	public static class PrimedTnt extends Entity {

    /**
     * Constructs a TNT dynamic tile entity from the given tag.
     *
     * @param primedtnt The tag from which to construct this TNT dynamic tile entity.
     * @throws FormatException If the given tag is invalid.
     */
    public PrimedTnt(Tag.Compound primedtnt) throws FormatException {
      super(primedtnt);
    }

    /**
     * Constructs a TNT dynamic tile from a position.
     *
     * @param X The X position.
     * @param Y The Y position.
     * @param Z The Z position.
     */
    public PrimedTnt(double X, double Y, double Z) throws FormatException {
      super(X, Y, Z);
      entity.setByte(FUSE, (byte) 0);
    }

    /**
     * Returns the number of ticks before the TNT explodes.
     *
     * @return The number of ticks before the TNT explodes.
     */
    public byte getFuse() {
      return entity.getByte(FUSE);
    }

    /**
     * Sets the number of ticks before the TNT explodes.
     *
     * @param ticks The number of ticks before the TNT explodes.
     */
    public void setFuse(byte ticks) {
      entity.setByte(FUSE, ticks);
    }
  }

	/**
	 * Falling block dynamic tile entity.
	 */
	public static class FallingSand extends Entity {

		/**
		 * Constructs a Falling Block dynamic tile entity from the given tag.
		 * @param fallingsand The tag from which to construct this Falling Block dynamic tile entity.
		 * @throws FormatException if the given tag is invalid.
		 */
		public FallingSand(Tag.Compound fallingsand) throws FormatException {
			super(fallingsand);
		}
		/**
		 * Constructs a Falling Block dynamic tile from a position.
		 * @param X The X position.
		 * @param Y The Y position.
		 * @param Z The Z position.
		 */
		public FallingSand(double X, double Y, double Z) throws FormatException {
			super(X, Y, Z);
      entity.setByte(TILE, (byte) ID.SAND.id);
		}

		/**
		 * Returns the Block ID_ATTR of this falling block.
		 * @return The Block ID_ATTR of this falling block.
		 */
		public byte BlockID()
		{
			return entity.getByte(TILE);
		}
		/**
		 * Sets the Block ID_ATTR of this falling block.
		 * @param id The Block ID_ATTR of this falling block.
		 */
		public void BlockID(byte id) {
			entity.setByte(TILE, id);
		}
	}

	/**
	 * Ender Crystal
	 */
	public static class EnderCrystal extends Entity {
		/**
		 * Constructs an Ender Crystal entity from the given tag.
		 * @param endercrystal The tag from which to construct this Ender Crystal.
		 * @throws FormatException if the given tag is invalid.
		 */
		public EnderCrystal(Tag.Compound endercrystal) throws FormatException {
			super(endercrystal);
		}

		/**
		 * Constructs an Ender Crystal from a position.
		 * @param X The X position.
		 * @param Y The Y position.
		 * @param Z The Z position.
		 */
		public EnderCrystal(double X, double Y, double Z) throws FormatException {
			super(X, Y, Z);
		}
	}

	/**
	 * Thrown Eye of Ender
	 */
	public static class EyeOfEnderSignal extends Entity {
		/**
		 * Constructs a Thrown Eye of Ender from the given tag.
		 * @param eyeofendersignal The tag from which to construct this Thrown Eye of Ender.
		 * @throws FormatException if the given tag is invalid.
		 */
		public EyeOfEnderSignal(Tag.Compound eyeofendersignal
                            ) throws FormatException {
			super(eyeofendersignal);
		}

		/**
		 * Constructs a Thrown Eye of Ender from a position.
		 * @param X The X position.
		 * @param Y The Y position.
		 * @param Z The Z position.
		 */
		public EyeOfEnderSignal(double X, double Y,
                            double Z) throws FormatException {
			super(X, Y, Z);
		}
	}

	/**
	 * Dropped Item entity
	 * <p>
	 * For items in inventories, see {@link com.github.omalley.clotho.NBT.Minecraft.Item}.
	 */
	public static class DroppedItem extends Entity {
		/**
		 * Constructs a Dropped Item entity from the given tag.
		 * @param item The tag from which to construct this Dropped Item entity.
		 * @throws FormatException
		 */
		public DroppedItem(Tag.Compound item) throws FormatException {
			super(item);
		}

		/**
		 * Constructs a Dropped Item from a position.
		 * @param X The X position.
		 * @param Y The Y position.
		 * @param Z The Z position.
		 */
		public DroppedItem(double X, double Y, double Z) throws FormatException {
			super(X, Y, Z);
      entity.setShort(HEALTH, (short) 5);
      entity.setShort(AGE, (short) 0);
      entity.setShort(Item.ID_ATTR, (short) 7);
      entity.setShort(Item.DAMAGE, (short) 0);
      entity.setByte(Item.COUNT, (byte) 1);
		}

		/**
		 * Returns the hit points this Dropped Item has.
		 * @return The hit points this Dropped Item has.
		 */
		public short getHealth()
		{
			return entity.getShort(HEALTH);
		}
		/**
		 * Sets the hit points this Dropped Item has.
		 * @param hp The hit points this Dropped Item has.
		 */
		public void setHealth(short hp)
		{
			entity.setShort(HEALTH, hp);
		}

		/**
		 * Returns the number of ticks this Dropped Item has been left alone.
		 * @return The number of ticks this Dropped Item has been left alone.
		 */
		public short getAge()
		{
			return entity.getShort(AGE);
		}
		/**
		 * Sets the number of ticks this Dropped Item has been left alone.
		 * @param ticks The number of ticks this Dropped Item has been left alone.
		 */
		public void setAge(short ticks)
		{
			entity.setShort(AGE, ticks);
		}

		/**
		 * Returns the item that was dropped.
		 * @return The item that was dropped.
		 */
		public Item getItem() throws FormatException {
			return new Item(entity);
		}

		/**
		 * Sets the item that was dropped.
		 * @param dropped The item that was dropped.
		 */
		public DroppedItem(Item dropped) throws FormatException {
			super(dropped.ToNBT());
      entity.setShort(HEALTH, (short) 5);
      entity.setShort(AGE, (short) 0);
		}
	}

	/**
	 * Painting entity
	 */
	public static class Painting extends Entity {

		/**
		 * Constructs a Painting entity from the given tag.
		 * @param painting The tag from which this Painting entity is constructed.
		 * @throws FormatException if the given tag is invalid.
		 */
		public Painting(Tag.Compound painting) throws FormatException {
			super(painting);
		}

		/**
		 * Constructs a Painting from a position.
		 * @param X The X position.
		 * @param Y The Y position.
		 * @param Z The Z position.
		 */
		public Painting(double X, double Y, double Z) throws FormatException {
			super(X, Y, Z);
      entity.setByte(DIRECTION, Direction.East.getId());
      entity.setString(MOTIVE, "Creebet");
      entity.setInteger(TILE_X, (int)X);
      entity.setInteger(TILE_Y, (int)Y);
      entity.setInteger(TILE_Z, (int)Z);
		}

		/**
		 * Returns the direction that this Painting is facing.
		 * @return The direction that this Painting is facing.
		 */
		public Direction getDirection() throws FormatException {
			return Direction.FromOrdinal(entity.getByte(DIRECTION));
		}

		/**
		 * Sets the direction that this Painting is facing.
		 * @param direction The direction that this Painting is facing.
		 */
		public void setDirection(Direction direction)
		{
			entity.setByte(DIRECTION, direction.getId());
		}

		/**
		 * Returns the name of the art on this painting.
		 * @return The name of the art on this painting.
		 */
		public String getMotive()
		{
			return entity.getString(MOTIVE);
		}
		/**
		 * Sets the name of the art on this painting.
		 * @param name The name of the art on this painting.
		 */
		public void setMotive(String name)
		{
			entity.setString(MOTIVE, name);
		}

		/**
		 * Returns the X coordinate of the Tile that this painting is on.
		 * @return The X coordinate of the Tile that this painting is on.
		 */
		public int getTileX()
		{
			return entity.getInteger(TILE_X);
		}
		/**
		 * Returns the Y coordinate of the Tile that this painting is on.
		 * @return The Y coordinate of the Tile that this painting is on.
		 */
		public int getTileY()
		{
			return entity.getInteger(TILE_Y);
		}
		/**
		 * Returns the Z coordinate of the Tile that this painting is on.
		 * @return The Z coordinate of the Tile that this painting is on.
		 */
		public int getTileZ()
		{
			return entity.getInteger(TILE_Z);
		}
		/**
		 * Sets the X coordinate of the Tile that this painting is on.
		 * @param x The X coordinate of the Tile that this painting is on.
		 */
		public void setTileX(int x)
		{
			entity.setInteger(TILE_X, x);
		}
		/**
		 * Sets the Y coordinate of the Tile that this painting is on.
		 * @param y The Y coordinate of the Tile that this painting is on.
		 */
		public void setTileY(int y)
		{
			entity.setInteger(TILE_Y, y);
		}
		/**
		 * Sets the Z coordinate of the Tile that this painting is on.
		 * @param z The Z coordinate of the Tile that this painting is on.
		 */
		public void setTileZ(int z)
		{
			entity.setInteger(TILE_Z, z);
		}
		/**
		 * Sets the coordinates of the Tile that this painting is on.
		 * @param x The X coordinate of the Tile that this painting is on.
		 * @param y The Y coordinate of the Tile that this painting is on.
		 * @param z The Z coordinate of the Tile that this painting is on.
		 */
		public void setTile(int x, int y, int z)
		{
			setTileX(x);
      setTileY(y);
      setTileZ(z);
		}
	}

	/**
	 * XP Orb entity
	 */
	public static class XPOrb extends Entity {
		/**
		 * Constructs an XP Orb entity from the given tag.
		 * @param xporb The tag from which to construct this XP Orb entity.
		 * @throws FormatException if the given tag is invalid.
		 */
		public XPOrb(Tag.Compound xporb) throws FormatException {
			super(xporb);
		}

		/**
		 * Constructs an XP Orb from a position.
		 * @param X The X position.
		 * @param Y The Y position.
		 * @param Z The Z position.
		 */
		public XPOrb(double X, double Y, double Z) throws FormatException {
			super(X, Y, Z);
      entity.setShort(HEALTH, (short) 5);
      entity.setShort(AGE, (short) 0);
      entity.setShort(VALUE, (short) 1);
		}

		/**
		 * Returns the hit points this XP Orb has.
		 * @return The hit points this XP Orb has.
		 */
		public short Health()
		{
			return entity.getShort(HEALTH);
		}
		/**
		 * Sets the hit points this XP Orb has.
		 * @param hp The hit points this XP Orb has.
		 */
		public void Health(short hp)
		{
			entity.setShort(HEALTH, hp);
		}

		/**
		 * Returns the number of ticks this XP Orb has been left alone.
		 * @return The number of ticks this XP Orb has been left alone.
		 */
		public short Age()
		{
			return entity.getShort(AGE);
		}
		/**
		 * Sets the number of ticks this XP Orb has been left alone.
		 * @param ticks The number of ticks this XP Orb has been left alone.
		 */
		public void Age(short ticks)
		{
			entity.setShort(AGE, ticks);
		}

		/**
		 * Returns the amount of experience this XP Orb will give when picked up.
		 * @return The amount of experience this XP Orb will give when picked up.
		 */
		public short Value()
		{
			return entity.getShort(VALUE);
		}
		/**
		 * Sets the amount of experience this XP Orb will give when picked up.
		 * @param xp The amount of experience this XP Orb will give when picked up.
		 */
		public void Value(short xp)
		{
			entity.setShort(VALUE, xp);
		}
	}

	/**
	 * Projectiles
	 */
	public static abstract class Projectile extends Entity {

    /**
     * Constructs a projectile from the given tag.
     *
     * @param projectile The tag from which to construct the projectile.
     * @throws FormatException if the tag is invalid.
     */
    public Projectile(Tag.Compound projectile) throws FormatException {
      super(projectile);
    }

    /**
     * Constructs a Projectile from a position.
     *
     * @param X The X position.
     * @param Y The Y position.
     * @param Z The Z position.
     */
    public Projectile(double X, double Y, double Z) throws FormatException {
      super(X, Y, Z);
      entity.setShort(X_TILE, (short) X);
      entity.setShort(Y_TILE, (short) Y);
      entity.setShort(Z_TILE, (short) Z);
      entity.setByte(IN_TILE, (byte) 0);
      entity.setByte(SHAKE, (byte) 0);
      entity.setByte(IN_GROUND, (byte) 0);
    }

    /**
     * Returns the X coordinate of the tile this projectile is in.
     *
     * @return The X coordinate of the tile this projectile is in.
     */
    public short getTileX() {
      return entity.getShort(X_TILE);
    }

    /**
     * Returns the Y coordinate of the tile this projectile is in.
     *
     * @return The Y coordinate of the tile this projectile is in.
     */
    public short getTileY() {
      return entity.getShort(Y_TILE);
    }

    /**
     * Returns the Z coordinate of the tile this projectile is in.
     *
     * @return The Z coordinate of the tile this projectile is in.
     */
    public short getTileZ() {
      return entity.getShort(Z_TILE);
    }

    /**
     * Sets the X coordinate of the tile this projectile is in.
     *
     * @param x The X coordinate of the tile this projectile is in.
     */
    public void setTileX(short x) {
      entity.setShort(X_TILE, x);
    }

    /**
     * Sets the Y coordinate of the tile this projectile is in.
     *
     * @param y The Y coordinate of the tile this projectile is in.
     */
    public void setTileY(short y) {
      entity.setShort(Y_TILE, y);
    }

    /**
     * Sets the Z coordinate of the tile this projectile is in.
     *
     * @param z The Z coordinate of the tile this projectile is in.
     */
    public void setTileZ(short z) {
      entity.setShort(Z_TILE, z);
    }

    /**
     * Sets the coordinates of the tile this projectile is in.
     *
     * @param x The X coordinate of the tile this projectile is in.
     * @param y The Y coordinate of the tile this projectile is in.
     * @param z The Z coordinate of the tile this projectile is in.
     */
    public void setTile(short x, short y, short z) {
      setTileX(x);
      setTileY(y);
      setTileZ(z);
    }

    /**
     * Returns whether this projectile is in a tile.
     *
     * @return Whether this projectile is in a tile.
     */
    public boolean InTile() {
      return entity.getByte(IN_TILE) != 0;
    }

    /**
     * Sets whether this projectile is in a tile.
     *
     * @param in Whether this projectile is in a tile.
     */
    public void InTile(boolean in) {
      entity.setByte(IN_TILE, (byte) (in ? 1 : 0));
    }

    /**
     * Returns the shake value of this projectile.
     *
     * @return The shake value of this projectile.
     */
    public byte Shake() {
      return entity.getByte(SHAKE);
    }

    /**
     * Sets the shake value of this projectile.
     *
     * @param shake The shake value of this projectile.
     */
    public void Shake(byte shake) {
      entity.setByte(SHAKE, shake);
    }

    /**
     * Returns whether this projectile is in the ground or not.
     *
     * @return Whether this projectile is in the ground or not.
     */
    public boolean InGround() {
      return entity.getByte(IN_GROUND) != 0;
    }

    /**
     * Sets whether this projectile is in the ground or not.
     *
     * @param in Whether this projectile is in the ground or not.
     */
    public void InGround(boolean in) {
      entity.setByte(IN_GROUND, (byte)(in ? 1 : 0));
    }
  }

  /**
   * Arrow projectiles
   */
  public static class Arrow extends Projectile {

    /**
     * Constructs an Arrow projectile from the given tag.
     *
     * @param arrow The tag from which to construct this Arrow projectile.
     * @throws FormatException if the given tag is invalid.
     */
    public Arrow(Tag.Compound arrow) throws FormatException {
      super(arrow);
    }

    /**
     * Constructs an Arrow from a position.
     *
     * @param X The X position.
     * @param Y The Y position.
     * @param Z The Z position.
     */
    public Arrow(double X, double Y, double Z) throws FormatException {
      super(X, Y, Z);
      entity.setByte(IN_DATA, (byte) 0);
      entity.setByte(PICKUP, (byte) 0);
      entity.setDouble(DAMAGE, 0.0);
    }

    public byte getInData() {
      return entity.getByte(IN_DATA);
    }

    public void setInData(byte datain) {
      entity.setByte(IN_DATA, datain);
    }

    public byte getPickup() {
      return entity.getByte(PICKUP);
    }

    public void setPickup(byte up) {
      entity.setByte(PICKUP, up);
    }

    public double getDamage() {
      return entity.getDouble(DAMAGE);
    }

    public void setDamage(double d) {
      entity.setDouble(DAMAGE, d);
    }
  }

  /**
   * Thrown Snowball projectiles
   */
  public static class Snowball extends Projectile {
    /**
     * Constructs a Thrown Snowball projectile from the given tag.
     *
     * @param snowball The tag from which to construct this Thrown Snowball projectile.
     * @throws FormatException if the given tag is invalid.
     */
    public Snowball(Tag.Compound snowball) throws FormatException {
      super(snowball);
    }

    /**
     * Constructs a Thrown Snowball from a position.
     *
     * @param X The X position.
     * @param Y The Y position.
     * @param Z The Z position.
     */
    public Snowball(double X, double Y, double Z) throws FormatException {
      super(X, Y, Z);
    }
  }

  /**
   * Thrown Egg projectiles
   */
  public static class Egg extends Projectile {
    /**
     * Constructs a Thrown Egg projectile from the given tag.
     *
     * @param egg The tag from which to construct this Thrown Egg projectile.
     * @throws FormatException if the given tag is invalid.
     */
    public Egg(Tag.Compound egg) throws FormatException {
      super(egg);
    }

    /**
     * Constructs a Thrown Egg from a position.
     *
     * @param X The X position.
     * @param Y The Y position.
     * @param Z The Z position.
     */
    public Egg(double X, double Y, double Z) throws FormatException {
      super(X, Y, Z);
    }
  }

  /**
   * Ghast Fireball projectiles
   */
  public static class Fireball extends Projectile {
    /**
     * Constructs a Ghast Fireball projectile from the given tag.
     *
     * @param fireball The tag from which to construct this Ghast Fireball projectile.
     * @throws FormatException if the given tag is invalid.
     */
    public Fireball(Tag.Compound fireball) throws FormatException {
      super(fireball);
    }

    /**
     * Constructs a Ghast Fireball from a position.
     *
     * @param X The X position.
     * @param Y The Y position.
     * @param Z The Z position.
     */
    public Fireball(double X, double Y, double Z) throws FormatException {
      super(X, Y, Z);
    }
  }

  /**
   * Blaze Fireball projectiles (also used by Fire Charges)
   */
  public static class SmallFireball extends Projectile {
    /**
     * Constructs a Blaze Fireball projectile from the given tag.
     *
     * @param fireball The tag from which to construct this Blaze Fireball projectile.
     * @throws FormatException if the given tag is invalid.
     */
    public SmallFireball(Tag.Compound fireball) throws FormatException {
      super(fireball);
    }

    /**
     * Constructs a Blaze Fireball from a position.
     *
     * @param X The X position.
     * @param Y The Y position.
     * @param Z The Z position.
     */
    public SmallFireball(double X, double Y, double Z) throws FormatException {
      super(X, Y, Z);
    }
  }

  /**
   * Thrown Ender Pearl projectiles
   */
  public static class ThrownEnderpearl extends Projectile {
    /**
     * Constructs a Thrown Ender Pearl projectile from the given tag.
     *
     * @param thrownenderpearl The tag from which to construct this Thrown Ender Pearl projectile.
     * @throws FormatException if the given tag is invalid.
     */
    public ThrownEnderpearl(Tag.Compound thrownenderpearl
    ) throws FormatException {
      super(thrownenderpearl);
    }

    /**
     * Constructs a Thrown Ender Pearl from a position.
     *
     * @param X The X position.
     * @param Y The Y position.
     * @param Z The Z position.
     */
    public ThrownEnderpearl(double X, double Y,
                            double Z) throws FormatException {
      super(X, Y, Z);
    }
  }

}
