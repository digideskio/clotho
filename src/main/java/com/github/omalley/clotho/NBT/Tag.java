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
package com.github.omalley.clotho.NBT;

import com.google.gson.stream.JsonWriter;

import javax.xml.bind.DatatypeConverter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collection;
import java.util.Set;

/**
 * The main class used by this NBT package, its static subclasses extend and implement it. The equals() and hashCode() methods are based entirely on the name of the tag.
 * I am aware that this means you can do nonsensical things such as Tag.ByteTag.Compound.ShortTag.ListTag...
 *
 * @author LB
 * @version 19133
 */
public abstract class Tag implements Cloneable {
  /**
   * The name of this tag.
   */
  private String name;

  /**
   * The sole constructor for the abstract tag class.
   *
   * @param _name The name of the new tag. If this tag must always have a name, this parameter must not be null. If this tag must never have a name, this parameter must be null.
   */
  public Tag(String _name) {
    name = _name;
  }

  /**
   * Returns the name of this tag, or null if this tag doesn't have a name.
   *
   * @return The name of this tag, or null if this tag doesn't have a name.
   */
  public String getName() {
    return name;
  }

  /**
   * Write the tag recursively as Json.
   *
   * @param writer    the stream to write to
   * @param recursive the method to use recursively
   */
  abstract void writeJson(JsonWriter writer,
                          Method recursive) throws IOException;

  void writeTypedJson(JsonWriter writer,
                      Method recursive) throws IOException {
    writer.beginObject();
    writer.name("type");
    writer.value(Type().toString().toLowerCase());
    writer.name("value");
    writeJson(writer, recursive);
    writer.endObject();
  }

  static final Method UNTYPED_JSON;
  static final Method TYPED_JSON;

  static {
    try {
      UNTYPED_JSON = Tag.class.getDeclaredMethod("writeJson", JsonWriter.class,
          Method.class);
      UNTYPED_JSON.setAccessible(true);
      TYPED_JSON = Tag.class.getDeclaredMethod("writeTypedJson",
          JsonWriter.class, Method.class);
      TYPED_JSON.setAccessible(true);
    } catch (NoSuchMethodException e) {
      throw new IllegalArgumentException("bad method", e);
    }
  }

  public void writeJson(JsonWriter writer,
                        boolean includeTypes) throws IOException {
    if (includeTypes) {
      writeTypedJson(writer, TYPED_JSON);
    } else {
      writeJson(writer, UNTYPED_JSON);
    }
  }

  public void printJson(boolean types) {
    try {
      JsonWriter writer = new JsonWriter(new PrintWriter(System.out));
      writer.setIndent("  ");
      writeJson(writer, types);
      writer.flush();
    } catch (IOException ioe) {
      throw new IllegalArgumentException("bad state", ioe);
    }
  }

  /**
   * The enumeration class that represents the different tag types in the NBT format.
   */
  public static enum Type {
    END,
    BYTE, SHORT, INT, LONG,
    FLOAT, DOUBLE,
    BYTEARRAY,
    STRING,
    LIST,
    COMPOUND,
    INTARRAY;

    /**
     * Converts an integer ordinal to a tag type.
     *
     * @param ordinal The integer ID of the tag.
     * @return The Tag.Type corresponding to the ID.
     * @throws IndexOutOfBoundsException if the ordinal is not with a valid range.
     */
    public static Type FromID(int ordinal) throws IndexOutOfBoundsException {
      switch (ordinal) {
        case 0:
          return END;
        case 1:
          return BYTE;
        case 2:
          return SHORT;
        case 3:
          return INT;
        case 4:
          return LONG;
        case 5:
          return FLOAT;
        case 6:
          return DOUBLE;
        case 7:
          return BYTEARRAY;
        case 8:
          return STRING;
        case 9:
          return LIST;
        case 10:
          return COMPOUND;
        case 11:
          return INTARRAY;
      }
      throw new IndexOutOfBoundsException("Tag ID out of bounds: " + ordinal);
    }

    /**
     * Converts this type to its respective class.
     *
     * @return The class object for this type's relevant class.
     */
    public Class<? extends Tag> ToClass() {
      switch (this) {
        case END:
          return End.class;
        case BYTE:
          return ByteTag.class;
        case SHORT:
          return ShortTag.class;
        case INT:
          return IntTag.class;
        case LONG:
          return LongTag.class;
        case FLOAT:
          return FloatTag.class;
        case DOUBLE:
          return DoubleTag.class;
        case BYTEARRAY:
          return ByteArray.class;
        case STRING:
          return StringTag.class;
        case LIST:
          return ListTag.class;
        case COMPOUND:
          return Compound.class;
        case INTARRAY:
          return IntArray.class;
      }
      throw new IllegalArgumentException();
    }

    /**
     * The exception thrown by <code>Tag.ListTag</code> when a tag is added that doesn't match the tag's tag type.
     */
    public static class MismatchException extends Exception {
      public MismatchException(String msg) {
        super(msg);
      }
    }
  }

  /**
   * The polymorphic method to get which tag type this tag corresponds to.
   *
   * @return The tag type this tag corresponds to.
   */
  public abstract Type Type();

  /**
   * The main serialization function. Serializes raw, uncompressed NBT data by polymorphically calling a payload serialization function.
   *
   * @param o The <code>OutputStream</code> to serialize to.
   * @throws IOException if the output operation generates an exception.
   */
  public final void Serialize(OutputStream o) throws IOException {
    PreSerialize(o);
    SerializePayload(o);
  }

  /**
   * The polymorphic method used to serialize only the tag's payload.
   *
   * @param o The <code>OutputStream</code> to serialize to.
   * @throws IOException if the output operation generates an exception.
   */
  protected abstract void SerializePayload(OutputStream o) throws IOException;

  /**
   * Represents the UTF-8 <code>Charset</code>.
   */
  protected static final java.nio.charset.Charset UTF8;

  static {
    java.nio.charset.Charset temp = null;
    try {
      temp = java.nio.charset.Charset.forName("UTF-8");
    } catch (Throwable t) {
    } finally {
      UTF8 = temp;
    }
  }

  /**
   * The method used to serialize the type and name of a tag.
   *
   * @param o The <code>OutputStream</code> to serialize to.
   * @throws IOException if the output operation generates an exception.
   */
  private void PreSerialize(OutputStream o) throws IOException {
    o.write((byte) Type().ordinal());
    if (name != null) {
      new StringTag(null, name).SerializePayload(o);
    }
  }

  /**
   * Used to create a visual, text-based representation of this tag.
   *
   * @return A visual, text-based representation of this tag.
   */
  @Override
  public abstract String toString();

  /**
   * A utility method for either quoting the tag's name or returning nothing if the name is <code>null</code>.
   *
   * @return A space followed by the quoted name of the tag, or nothing if the name is null.
   */
  protected final String QuoteName() {
    if (name != null) {
      return " \"" + name + "\"";
    }
    return "";
  }

  /**
   * Returns the hash code of the name of this tag.
   *
   * @return The hash code of the name of this tag.
   */
  @Override
  public final int hashCode() {
    if (name == null) {
      return 0;
    }
    return name.hashCode();
  }

  /**
   * Returns whether the given object is a Tag and has the same name.
   *
   * @param o The object to compare to.
   * @return Whether the given object is a Tag and has the same name.
   */
  @Override
  public final boolean equals(Object o) {
    if (o instanceof Tag) {
      Tag t = (Tag) o;
      if (name == null) {
        return t.name == null;
      } else if (t.name != null) {
        return name.equals(t.name);
      }
    }
    return false;
  }

  /**
   * Returns whether the given tag has the same name.
   *
   * @param t The tag to compare to.
   * @return Whether the given tag has the same name.
   */
  public final boolean equals(Tag t) {
    if (name == null) {
      return t.name == null;
    } else if (t.name != null) {
      return name.equals(t.name);
    }
    return false;
  }

  /**
   * Returns an independent clone of this tag.
   *
   * @return An independent clone of this tag.
   */
  @Override
  public Tag clone() {
    try {
      return (Tag) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Returns an independent clone of this tag with the given name.
   *
   * @param newname The new name for the clone, or null if the clone should not have a name.
   * @return An independent clone of this tag with the given name.
   */
  public final Tag clone(String newname) {
    Tag t = clone();
    if (this != End.TAG) {
      t.name = newname;
    }
    return t;
  }

  /**
   * TAG_End
   */
  public static final class End extends Tag {
    /**
     * The single instance of this tag.
     */
    public static final End TAG = new End();

    /**
     * Used to construct the single instance of this tag.
     */
    private End() throws UnsupportedOperationException {
      super(null);
      if (TAG != null) {
        throw new UnsupportedOperationException();
      }
    }

    public End(String name, InputStream stream) {
      super(null);
    }

    @Override
    public void writeJson(JsonWriter writer,
                          Method recursive) throws IOException {
      // PASS
    }

    /**
     * Returns the tag type corresponding to TAG_End.
     *
     * @return <code>Type.END</code>.
     */
    @Override
    public Type Type() {
      return Type.END;
    }

    /**
     * Does nothing, but prevents this class from being abstract.
     *
     * @param o The <code>OutputStream</code> to serialize to.
     * @throws IOException when pigs fly.
     */
    @Override
    protected void SerializePayload(OutputStream o) throws IOException {
    }

    /**
     * Returns "End".
     *
     * @return "End".
     */
    @Override
    public String toString() {
      return "End";
    }

    /**
     * Returns <code>TAG</code>.
     *
     * @return <code>TAG</code>.
     */
    @Override
    public End clone() {
      return TAG;
    }
  }

  /**
   * TAG_Byte
   */
  public static final class ByteTag extends Tag {
    /**
     * The value of this byte.
     */
    public byte v;

    /**
     * The normal constructor.
     *
     * @param name The name of this byte.
     * @param b    The initial value of the byte.
     */
    public ByteTag(String name, byte b) {
      super(name);
      v = b;
    }

    /**
     * The DeserializePayload constructor.
     *
     * @param name The name of this byte.
     * @param i    The <code>InputStream</code> to deserialize the byte from.
     * @throws IOException     if the input operation generates an exception.
     * @throws FormatException when pigs fly.
     */
    public ByteTag(String name, InputStream i) throws IOException, FormatException //DeserializePayload
    {
      this(name, (byte) i.read());
    }

    @Override
    public void writeJson(JsonWriter writer,
                          Method recursive) throws IOException {
      writer.value(v);
    }

    /**
     * Returns the tag type corresponding to TAG_Byte.
     *
     * @return <code>Type.BYTE</code>
     */
    @Override
    public Type Type() {
      return Type.BYTE;
    }

    /**
     * Serializes the byte to the <code>OutputStream</code>.
     *
     * @param o The <code>OutputStream</code> to serialize the byte to.
     * @throws IOException if the output operation generates an exception.
     */
    @Override
    protected void SerializePayload(OutputStream o) throws IOException {
      o.write(v);
    }

    /**
     * Gives a textual representation of this byte in base-10.
     *
     * @return A textual representation of this byte in base-10.
     */
    @Override
    public String toString() {
      return "ByteTag" + QuoteName() + ": " + v + "";
    }
  }

  /**
   * TAG_Short
   */
  public static final class ShortTag extends Tag {
    /**
     * The value of this short.
     */
    public short v;

    /**
     * The normal constructor.
     *
     * @param name The name of this short.
     * @param s    The initial value of the short.
     */
    public ShortTag(String name, short s) {
      super(name);
      v = s;
    }

    /**
     * The DeserializePayload constructor.
     *
     * @param name The name of this short.
     * @param i    The <code>InputStream</code> to deserialize the short from.
     * @throws IOException     if the input operation generates an exception.
     * @throws FormatException when pigs fly.
     */
    public ShortTag(String name, InputStream i) throws IOException, FormatException //DeserializePayload
    {
      this(name, new DataInputStream(i).readShort());
    }

    @Override
    public void writeJson(JsonWriter writer,
                          Method recursive) throws IOException {
      writer.value(v);
    }

    /**
     * returns the tag type corresponding to TAG_Short.
     *
     * @return <code>Type.SHORT</code>
     */
    @Override
    public Type Type() {
      return Type.SHORT;
    }

    /**
     * Serializes the short to the <code>OutputStream</code>.
     *
     * @param o The <code>OutputStream</code> to serialize the short to.
     * @throws IOException if the output operation generates an exception.
     */
    @Override
    protected void SerializePayload(OutputStream o) throws IOException {
      new DataOutputStream(o).writeShort(v);
    }

    /**
     * Gives a textual representation of this short in base-10.
     *
     * @return A textual representation of this short in base-10.
     */
    @Override
    public String toString() {
      return "ShortTag" + QuoteName() + ": " + v + "";
    }
  }

  /**
   * TAG_Int
   */
  public static final class IntTag extends Tag {
    /**
     * The value of this integer.
     */
    public int v;

    /**
     * The normal constructor.
     *
     * @param name The name of this integer.
     * @param i    The initial value of the integer.
     */
    public IntTag(String name, int i) {
      super(name);
      v = i;
    }

    /**
     * The DeserializePayload constructor.
     *
     * @param name The name of this integer.
     * @param i    The <code>InputStream</code> to deserialize the integer from.
     * @throws IOException     if the input operation generates an exception.
     * @throws FormatException when pigs fly.
     */
    public IntTag(String name, InputStream i) throws IOException, FormatException //DeserializePayload
    {
      this(name, new DataInputStream(i).readInt());
    }

    @Override
    public void writeJson(JsonWriter writer,
                          Method recursive) throws IOException {
      writer.value(v);
    }

    /**
     * Returns the tag type corresponding to TAG_Int.
     *
     * @return <code>Type.INT</code>.
     */
    @Override
    public Type Type() {
      return Type.INT;
    }

    /**
     * Serializes the integer to the <code>OutputStream</code>.
     *
     * @param o The <code>OutputStream</code> to serialize the integer to.
     * @throws IOException if the output operation generates an exception.
     */
    @Override
    protected void SerializePayload(OutputStream o) throws IOException {
      new DataOutputStream(o).writeInt(v);
    }

    /**
     * Gives a textual representation of this integer in base-10.
     *
     * @return A textual representation of this integer in base-10.
     */
    @Override
    public String toString() {
      return "IntTag" + QuoteName() + ": " + v + "";
    }
  }

  /**
   * TAG_Long
   */
  public static final class LongTag extends Tag {
    /**
     * The value of this long.
     */
    public long v;

    /**
     * The normal constructor.
     *
     * @param name The name of this long.
     * @param l    The initial value of the long.
     */
    public LongTag(String name, long l) {
      super(name);
      v = l;
    }

    /**
     * The DeserializePayload constructor.
     *
     * @param name The name of this long.
     * @param i    The <code>InputStream</code> to deserialize the long from.
     * @throws IOException     if the input operation generates an exception.
     * @throws FormatException when pigs fly.
     */
    public LongTag(String name, InputStream i) throws IOException, FormatException //DeserializePayload
    {
      this(name, new DataInputStream(i).readLong());
    }

    @Override
    public void writeJson(JsonWriter writer,
                          Method recursive) throws IOException {
      writer.value(v);
    }

    /**
     * Returns the tag type corresponding to TAG_Long.
     *
     * @return <code>Type.LONG</code>.
     */
    @Override
    public Type Type() {
      return Type.LONG;
    }

    /**
     * Serializes the long to the <code>OutputStream</code>.
     *
     * @param o The <code>OutputStream</code> to serialize the long to.
     * @throws IOException if the output operation generates an exception.
     */
    @Override
    protected void SerializePayload(OutputStream o) throws IOException {
      new DataOutputStream(o).writeLong(v);
    }

    /**
     * Gives a textual representation of this long in base-10.
     *
     * @return A textual representation of this long in base-10.
     */
    @Override
    public String toString() {
      return "LongTag" + QuoteName() + ": " + v + "";
    }
  }

  /**
   * TAG_Float
   */
  public static final class FloatTag extends Tag {
    /**
     * The value of this float.
     */
    public float v;

    /**
     * The normal constructor.
     *
     * @param name The name of this float.
     * @param f    The initial value of the float.
     */
    public FloatTag(String name, float f) {
      super(name);
      v = f;
    }

    /**
     * The DeserializePayload constructor.
     *
     * @param name The name of this float.
     * @param i    The <code>InputStream</code> to deserialize the float from.
     * @throws IOException     if the input operation generates an exception.
     * @throws FormatException when pigs fly.
     */
    public FloatTag(String name, InputStream i) throws IOException, FormatException //DeserializePayload
    {
      this(name, new DataInputStream(i).readFloat());
    }

    @Override
    public void writeJson(JsonWriter writer,
                          Method recursive) throws IOException {
      writer.value(v);
    }

    /**
     * Returns the tag type corresponding to TAG_Float.
     *
     * @return <code>Type.FLOAT</code>.
     */
    @Override
    public Type Type() {
      return Type.FLOAT;
    }

    /**
     * Serializes this float to the <code>OutputStream</code>
     *
     * @param o The <code>OutputStream</code> to serialize this long to.
     * @throws IOException if the output operation generates an exception.
     */
    @Override
    protected void SerializePayload(OutputStream o) throws IOException {
      new DataOutputStream(o).writeFloat(v);
    }

    /**
     * Gives a textual representation of this float in base-10.
     *
     * @return A textual representation of this float in base-10.
     */
    @Override
    public String toString() {
      return "FloatTag" + QuoteName() + ": " + v + "";
    }
  }

  /**
   * TAG_Double
   */
  public static final class DoubleTag extends Tag {
    /**
     * The value of this double.
     */
    public double v;

    /**
     * The normal constructor.
     *
     * @param name The name of this double.
     * @param d    The initial value of the double.
     */
    public DoubleTag(String name, double d) {
      super(name);
      v = d;
    }

    /**
     * The DeserializePayload constructor.
     *
     * @param name The name of this double.
     * @param i    The <code>InputStream</code> to deserialize the double from.
     * @throws IOException     if the input operation generates an exception.
     * @throws FormatException when pigs fly.
     */
    public DoubleTag(String name, InputStream i) throws IOException, FormatException //DeserializePayload
    {
      this(name, new DataInputStream(i).readDouble());
    }

    @Override
    public void writeJson(JsonWriter writer,
                          Method recursive) throws IOException {
      writer.value(v);
    }

    /**
     * Returns the tag type corresponding to TAG_Double.
     *
     * @return <code>Type.DOUBLE</code>.
     */
    @Override
    public Type Type() {
      return Type.DOUBLE;
    }

    /**
     * Serializes the double to the <code>OutputStream</code>.
     *
     * @param o The <code>OutputStream</code> to serialize this double to.
     * @throws IOException if the output operation generates an exception.
     */
    @Override
    protected void SerializePayload(OutputStream o) throws IOException {
      new DataOutputStream(o).writeDouble(v);
    }

    /**
     * Gives a textual representation of this double in base-10.
     *
     * @return A textual representation of this double in base-10.
     */
    @Override
    public String toString() {
      return "DoubleTag" + QuoteName() + ": " + v + "";
    }
  }

  /**
   * TAG_Byte_Array
   */
  public static final class ByteArray extends Tag {
    /**
     * The byte array in raw form.
     */
    public byte[] v;

    /**
     * The normal constructor.
     *
     * @param name The name of this byte array.
     * @param b    The initial byte array.
     */
    public ByteArray(String name, byte[] b) {
      super(name);
      v = b;
    }

    /**
     * The DeserializePayload constructor.
     *
     * @param name The name of this byte array.
     * @param i    The <code>InputStream</code> to deserialize the byte array from.
     * @throws IOException     if the input operation generates an exception.
     * @throws FormatException if the byte array size is negative.
     */
    public ByteArray(String name, InputStream i) throws IOException, FormatException //DeserializePayload
    {
      this(name, (byte[]) null);
      DataInputStream dis = new DataInputStream(i);
      int size = dis.readInt();
      if (size < 0) {
        throw new FormatException("ByteTag Array size was negative: " + size);
      }
      v = new byte[size];
      dis.readFully(v);
    }

    @Override
    public void writeJson(JsonWriter writer,
                          Method recursive) throws IOException {
      writer.value(DatatypeConverter.printHexBinary(v));
    }

    /**
     * Returns the tag type that corresponds to TAG_Byte_Array.
     *
     * @return <code>Type.BYTEARRAY</code>.
     */
    @Override
    public Type Type() {
      return Type.BYTEARRAY;
    }

    /**
     * Serializes the byte array to the <code>OutputStream</code>.
     *
     * @param o The <code>OutputStream</code> to serialize this byte array to.
     * @throws IOException if the output operation generates an exception.
     */
    @Override
    protected void SerializePayload(OutputStream o) throws IOException {
      DataOutputStream dos = new DataOutputStream(o);
      dos.writeInt(v.length);
      dos.write(v);
    }

    /**
     * Gives a textual representation of this byte array with each byte in base-10.
     *
     * @return A textual representation of this byte array with each byte in base-10.
     */
    @Override
    public String toString() {
      String s = "";
      for (byte b : v) {
        if (s.length() != 0) {
          s += ", ";
        }
        s += b;
      }
      return "ByteTag Array" + QuoteName() + ": [" + s + "]";
    }

    /**
     * Returns an independent clone of this ByteTag Array.
     *
     * @return An independent clone of this ByteTag Array.
     */
    @Override
    public ByteArray clone() {
      ByteArray ba = (ByteArray) super.clone();
      ba.v = Arrays.copyOf(v, v.length);
      return ba;
    }
  }

  /**
   * TAG_String
   * <p/>
   * The reason for the use of the fully qualified name <code>StringTag</code> throughout this code.
   */
  public static final class StringTag extends Tag {
    /**
     * The value of this string.
     */
    public String v;

    /**
     * The normal constructor.
     *
     * @param name The name of this string.
     * @param s    The initial string.
     */
    public StringTag(String name, String s) {
      super(name);
      v = s;
    }

    /**
     * The DeserializePayload constructor.
     *
     * @param name The name of this string.
     * @param i    The <code>InputStream</code> to deserialize this string from.
     * @throws IOException     if the input operation generates an exception.
     * @throws FormatException if the string length is negative.
     */
    public StringTag(String name, InputStream i) throws IOException, FormatException //DeserializePayload
    {
      this(name, "");
      DataInputStream dis = new DataInputStream(i);
      short length = dis.readShort();
      if (length < 0) {
        throw new FormatException("StringTag length was negative: " + length);
      }
      byte[] str = new byte[length];
      dis.readFully(str);
      v = new String(str, UTF8);
    }

    @Override
    public void writeJson(JsonWriter writer,
                          Method recursive) throws IOException {
      writer.value(v);
    }

    /**
     * Returns the tag type that corresponds to TAG_String.
     *
     * @return <code>Type.STRING</code>.
     */
    @Override
    public Type Type() {
      return Type.STRING;
    }

    /**
     * Serializes this string to the <code>OutputStream</code>.
     *
     * @param o The <code>OutputStream</code> to serialize this string to.
     * @throws IOException if the output operation generates an exception.
     */
    @Override
    protected void SerializePayload(OutputStream o) throws IOException {
      byte[] sarr = v.getBytes(UTF8);
      new DataOutputStream(o).writeShort((short) sarr.length);
      o.write(sarr);
    }

    /**
     * Gives a textual representation of this string.
     *
     * @return A textual representation of this string.
     */
    @Override
    public String toString() {
      return "StringTag" + QuoteName() + ": \"" + v + "\"";
    }
  }

  /**
   * TAG_List
   */
  public static final class ListTag extends Tag implements Iterable<Tag> {
    /**
     * The tag type this tags supports.
     */
    private final Type type;
    /**
     * The list of tags in this list.
     */
    private java.util.List<Tag> list = new ArrayList<>();

    public ListTag(String name,
                   InputStream input
                   ) throws IOException, NoSuchMethodException,
                            IllegalAccessException, InvocationTargetException,
                            InstantiationException {
      super(name);
      type = Type.FromID(input.read());
      Constructor cons = type.ToClass().getConstructor(String.class,
          InputStream.class);
      int len = new DataInputStream(input).readInt();
      for(int i=0; i < len; ++i) {
        Tag obj = (Tag) cons.newInstance(null, input);
        list.add(obj);
      }
    }

    /**
     * The normal constructor.
     *
     * @param name  The name of the tags.
     * @param _type The tag type this tags supports.
     * @throws IllegalArgumentException if the tag type is TAG_End or null, or a given tag is not a supported type or has a name that is not null.
     */
    public ListTag(String name, Type _type, Tag... tags) {
      super(name);
      if (_type == null) {
        throw new IllegalArgumentException("The tag type was null");
      }
      type = _type;
      if (type != Type.END) {
        for (Tag t : tags) {
          if (t.getName() != null) {
            throw new IllegalArgumentException("Tags in Lists must have null names; given tag had name: \"" + t.getName() + "\"");
          } else if (t.Type() == type) {
            list.add(t);
          } else {
            throw new IllegalArgumentException(new Type.MismatchException(type + " required, given " + t.Type()));
          }
        }
      }
    }

    public double getDouble(int posn) {
      return ((DoubleTag) list.get(posn)).v;
    }

    public void setDouble(int posn, double value) {
      ((DoubleTag) list.get(posn)).v = value;
    }

    public float getFloat(int posn) {
      return ((FloatTag) list.get(posn)).v;
    }

    public void setFloat(int posn, float value) {
      ((FloatTag) list.get(posn)).v = value;
    }

    @Override
    public void writeJson(JsonWriter writer,
                          Method recursive) throws IOException {
      writer.beginArray();
      for (Tag child : list) {
        try {
          recursive.invoke(child, writer, recursive);
        } catch (IllegalAccessException | InvocationTargetException e) {
          throw new IllegalArgumentException("bad invoke", e);
        }
      }
      writer.endArray();
    }

    /**
     * Returns the tag type that corresponds to TAG_List.
     *
     * @return <code>Type.LIST</code>.
     */
    @Override
    public Type Type() {
      return Type.LIST;
    }

    /**
     * Serializes this tags to the <code>OutputStream</code>.
     *
     * @param o The <code>OutputStream</code> to serialize this tags to.
     * @throws IOException if the output operation generates an exception.
     */
    @Override
    protected void SerializePayload(OutputStream o) throws IOException {
      o.write((byte) type.ordinal());
      new DataOutputStream(o).writeInt(list.size());
      for (int i = 0; i < list.size(); ++i) {
        list.get(i).SerializePayload(o);
      }
    }

    /**
     * Gives a textual representation of this tags with nice indenting even with
     * nesting.
     *
     * @return A textual representation of this tags with nice indenting even
     * with nesting.
     */
    @Override
    public String toString() {
      String s = "";
      for (int i = 0; i < list.size(); ++i) {
        if (i != 0) {
          s += ",\n";
        }
        s += list.get(i);
      }
      return "ListTag of " + type + "" + QuoteName() + ": \n[\n" + Compound.PreceedLinesWithTabs(s) + "\n]";
    }

    /**
     * Adds all given tags to this list until a given tag is of a type not supported by this list.
     *
     * @param tags The tags to be added.
     * @throws Tag.Type.MismatchException if the tag is of a type not supported by this tags.
     */
    public void Add(Tag... tags) throws FormatException {
      for (Tag t : tags) {
        if (t.getName() != null) {
          throw new FormatException("Tags in Lists must have null names; given tag had name: \"" + t.getName() + "\"");
        } else if (t.Type() != type) {
          throw new FormatException(type + " required, given " + t.Type());
        }
        list.add(t);
      }
    }

    /**
     * Adds all the tags from the given list to this list.
     *
     * @param l The list from which to add the tags.
     * @throws Tag.Type.MismatchException if the tags in the given list are not supported by this list.
     */
    public void AddFrom(ListTag l) throws FormatException {
      if (l.Supports() != type) {
        throw new FormatException(type + " required, given list of " + l.Supports());
      }
      for (Tag t : l.list) {
        list.add(t);
      }
    }

    /**
     * Adds all the tags from the given collection to this list.
     *
     * @param c The collection from which to add the tags.
     * @throws Tag.Type.MismatchException if the tags in the given collection are not supported by this list.
     */
    public void AddFrom(Collection<? extends Tag> c) throws FormatException {
      for (Tag t : c) {
        Add(t);
      }
    }

    /**
     * Returns the number of tags in this list tag.
     *
     * @return The number of tags in this list tag.
     */
    public int size() {
      return list.size();
    }

    /**
     * Sets the tag at the given index in the list to the given tag.
     *
     * @param index The index to set.
     * @param t     The tag that the index will be set to.
     * @throws Tag.Type.MismatchException if the tag is of a type not supported by this tags.
     */
    public void Set(int index, Tag t) throws FormatException {
      if (t.Type() != type) {
        throw new FormatException(type + " required, given " + t.Type());
      }
      list.set(index, t);
    }

    /**
     * Inserts the given tags to this list at the specified index.
     *
     * @param index The index before which to insert.
     * @param tags  The tags to insert.
     */
    public void Insert(int index, Tag... tags) {
      for (Tag t : tags) {
        if (t.getName() != null) {
          throw new IllegalArgumentException("Tags in Lists must have null names; given tag had name: \"" + t.getName() + "\"");
        } else if (t.Type() != type) {
          throw new IllegalArgumentException(type + " required, given " + t.Type());
        }
        list.add(index++, t);
      }
    }

    /**
     * Inserts the tags from the given list to this list at the specified index.
     *
     * @param index The index at which to insert the tags.
     * @param l     The list from which to insert the tags.
     * @throws Tag.Type.MismatchException if the tags in the given list are not supported by this list.
     */
    public void InsertFrom(int index, ListTag l) throws Type.MismatchException {
      if (l.Supports() != type) {
        throw new Type.MismatchException(type + " required, given list of " + l.Supports());
      }
      for (Tag t : l.list) {
        list.add(index++, t);
      }
    }

    /**
     * Inserts the tags from the given collection to this list at the specified index.
     *
     * @param index The index at which to insert the tags.
     * @param c     The collection from which to insert the tags.
     * @throws Tag.Type.MismatchException if the tags in the given collection are not supported by this list.
     */
    public void InsertFrom(int index, Collection<? extends Tag> c) throws Type.MismatchException {
      for (Tag t : c) {
        Insert(index++, t);
      }
    }

    /**
     * Getter for individual tags in this list tag.
     *
     * @param index The index to get a tag from.
     * @return The tag at the specified index.
     */
    public Tag Get(int index) {
      return list.get(index);
    }

    /**
     * Removes the tag at the specified index from this list tag.
     *
     * @param index The index of the tag to remove.
     * @return The tag that was removed.
     */
    public Tag Remove(int index) {
      return list.remove(index);
    }

    /**
     * Returns the tag type supported by this tags.
     *
     * @return The tag type supported by this tags.
     */
    public Type Supports() {
      return type;
    }

    /**
     * Returns an iterator over this list tag.
     *
     * @return An iterator over this list tag.
     */
    @Override
    public Iterator<Tag> iterator() {
      return list.iterator();
    }

    /**
     * Returns an independent clone of this ListTag tag.
     *
     * @return An independent clone of this ListTag tag.
     */
    @Override
    public ListTag clone() {
      ListTag li = (ListTag) super.clone();
      li.list = new ArrayList<>();
      for (int i = 0; i < list.size(); ++i) {
        li.list.add(list.get(i).clone());
      }
      return li;
    }
  }

  /**
   * TAG_Compound
   */
  public static final class Compound extends Tag implements Iterable<Tag> {
    /**
     * The list of tags in this compound tag.
     */
    private HashMap<String, Tag> tags = new HashMap<>();

    /**
     * The normal constructor.
     *
     * @param name The name of this compound tag.
     * @param tags The initial tags in this compound tag.
     * @throws IllegalArgumentException if the name of one of the given tags is null.
     */
    public Compound(String name, Tag... tags) throws IllegalArgumentException {
      super(name);
      for (Tag t : tags) {
        String n = t.getName();
        if (n == null) {
          throw new IllegalArgumentException("Tag names cannot be null");
        }
        if (t == End.TAG) {
          throw new IllegalArgumentException("Cannot manually add the End tag!");
        }
        this.tags.put(n, t);
      }
    }

    /**
     * The DeserializePayload constructor.
     *
     * @param name The name of this compound tag.
     * @param i    The <code>InputStream</code> to deserialize the compound tag from.
     * @throws IOException     if the input operation generates an exception.
     * @throws FormatException if some other exception is thrown while deserializing the compound tag.
     */
    public Compound(String name, InputStream i) throws IOException, FormatException //DeserializePayload
    {
      this(name);
      try {
        Type t;
        while ((t = Type.FromID(i.read())) != Type.END) {
          String n = new StringTag(null, i).v;
          tags.put(n, t.ToClass().getConstructor(String.class, InputStream.class).newInstance(n, i));
        }
      } catch (IOException e) {
        throw e;
      } catch (Throwable e) {
        throw new FormatException("Exception while deserializing Compound tag " + this, e);
      }
    }

    @Override
    public void writeJson(JsonWriter writer,
                          Method recursive) throws IOException {
      Set<String> keySet = tags.keySet();
      String[] keys = keySet.toArray(new String[keySet.size()]);
      Arrays.sort(keys);
      writer.beginObject();
      for (String key : keys) {
        Tag child = tags.get(key);
        writer.name(child.getName());
        try {
          recursive.invoke(child, writer, recursive);
        } catch (IllegalAccessException | InvocationTargetException e) {
          throw new IllegalArgumentException("bad invoke", e);
        }
      }
      writer.endObject();
    }

    /**
     * Returns the tag type that corresponds to TAG_Compound.
     *
     * @return <code>Type.COMPOUND</code>.
     */
    @Override
    public Type Type() {
      return Type.COMPOUND;
    }

    /**
     * Serializes this compound tag to the <code>OutputStream</code>.
     *
     * @param o The <code>OutputStream</code> to serialize this compound tag to.
     * @throws IOException if the output operation generates an exception.
     */
    @Override
    protected void SerializePayload(OutputStream o) throws IOException {
      for (Tag t : tags.values()) {
        t.Serialize(o);
      }
      End.TAG.Serialize(o);
    }

    /**
     * Gives a textual representation of this compound tag with nice indenting even with nesting.
     *
     * @return A textual representation of this compound tag with nice indenting even with nesting.
     */
    @Override
    public String toString() {
      String s = "";
      for (Tag t : tags.values()) {
        if (s.length() != 0) {
          s += ",\n";
        }
        s += t;
      }
      return "Compound" + QuoteName() + ":\n{\n" + PreceedLinesWithTabs(s) + "\n}";
    }

    /**
     * Utility function used by this class and the ListTag class for preceeding lines with tabs.
     *
     * @param s The string for which each line should be prefixed with a tab.
     * @return The string with each line prefixed with an additional tab.
     */
    /*default*/
    static String PreceedLinesWithTabs(String s) {
      return "\t" + s.replaceAll("\n", "\n\t");
    }

    /**
     * Adds the tags to this compound tag.
     *
     * @param tags The tags to be added.
     * @throws IllegalArgumentException if the name of one of the given tags is null.
     */
    public void Add(Tag... tags) throws IllegalArgumentException {
      for (Tag t : tags) {
        String n = t.getName();
        if (n == null) {
          throw new IllegalArgumentException("Tag names cannot be null");
        }
        if (t.Type() == Type.END) {
          throw new IllegalArgumentException("Cannot manually add a TAG_End!");
        }
        this.tags.put(n, t);
      }
    }

    /**
     * Adds all the tags from the given compound tag to this compound tag.
     *
     * @param c The compound tag from which to add the tags.
     */
    public void AddFrom(Tag.Compound c) {
      tags.putAll(c.tags);
    }

    /**
     * Adds the tags from the given collection to this compound tag.
     *
     * @param c The collection from which to add the tags.
     * @throws IllegalArgumentException if the name of one of the given tags is null.
     */
    public void AddFrom(Collection<? extends Tag> c) throws IllegalArgumentException {
      for (Tag t : c) {
        Add(t);
      }
    }

    /**
     * Returns the number of tags in this compound tag.
     *
     * @return The number of tags in this compound tag.
     */
    public int Size() {
      return tags.size();
    }

    /**
     * Returns the tag with the given name.
     *
     * @param name The name of the tag.
     * @return The tag with the given name, or null if the tag does not exist.
     */
    public Tag get(String name) {
      return tags.get(name);
    }

    public Byte getByte(String field) {
      Tag t = tags.get(field);
      if (t == null) {
        return null;
      }
      return ((ByteTag) t).v;
    }

    public byte[] getByteArray(String field) {
      Tag t = tags.get(field);
      if (t == null) {
        return null;
      }
      return ((ByteArray) t).v;
    }

    public int[] getIntegerArray(String field) {
      Tag t = tags.get(field);
      if (t == null) {
        return null;
      }
      return ((IntArray) t).v;
    }

    public Short getShort(String field) {
      Tag t = tags.get(field);
      if (t == null) {
        return null;
      }
      return ((ShortTag) t).v;
    }

    public Integer getInteger(String field) {
      Tag t = tags.get(field);
      if (t == null) {
        return null;
      }
      return ((IntTag) t).v;
    }

    public Long getLong(String field) {
      Tag t = tags.get(field);
      if (t == null) {
        return null;
      }
      return ((LongTag) t).v;
    }

    public String getString(String field) {
      Tag t = tags.get(field);
      if (t == null) {
        return null;
      }
      return ((StringTag) t).v;
    }

    public Float getFloat(String field) {
      Tag t = tags.get(field);
      if (t == null) {
        return null;
      }
      return ((FloatTag) t).v;
    }

    public Double getDouble(String field) {
      Tag t = tags.get(field);
      if (t == null) {
        return null;
      }
      return ((DoubleTag) t).v;
    }

    public void setByte(String field, Byte value) {
      if (value == null) {
        tags.remove(field);
      } else {
        if (!tags.containsKey(field)) {
          ByteTag t = new ByteTag(field, value);
          tags.put(field, t);
        } else {
          ((ByteTag) tags.get(field)).v = value;
        }
      }
    }

    public void setByteArray(String field, byte[] value) {
      if (value == null) {
        tags.remove(field);
      } else {
        if (!tags.containsKey(field)) {
          ByteArray t = new ByteArray(field, value);
          tags.put(field, t);
        } else {
          ((ByteArray) tags.get(field)).v = value;
        }
      }
    }

    public void setShort(String field, Short value) {
      if (value == null) {
        tags.remove(field);
      } else {
        if (!tags.containsKey(field)) {
          ShortTag t = new ShortTag(field, value);
          tags.put(field, t);
        } else {
          ((ShortTag) tags.get(field)).v = value;
        }
      }
    }

    public void setInteger(String field, Integer value) {
      if (value == null) {
        tags.remove(field);
      } else {
        if (!tags.containsKey(field)) {
          IntTag t = new IntTag(field, value);
          tags.put(field, t);
        } else {
          ((IntTag) tags.get(field)).v = value;
        }
      }
    }

    public void setLong(String field, Long value) {
      if (value == null) {
        tags.remove(field);
      } else {
        if (!tags.containsKey(field)) {
          LongTag t = new LongTag(field, value);
          tags.put(field, t);
        } else {
          ((LongTag) tags.get(field)).v = value;
        }
      }
    }

    public void setString(String field, String value) {
      if (value == null) {
        tags.remove(field);
      } else {
        if (!tags.containsKey(field)) {
          StringTag t = new StringTag(field, value);
          tags.put(field, t);
        } else {
          ((StringTag) tags.get(field)).v = value;
        }
      }
    }

    public void setDouble(String field, Double value) {
      if (value == null) {
        tags.remove(field);
      } else {
        if (!tags.containsKey(field)) {
          DoubleTag t = new DoubleTag(field, value);
          tags.put(field, t);
        } else {
          ((DoubleTag) tags.get(field)).v = value;
        }
      }
    }

    public void setFloat(String field, Float value) {
      if (value == null) {
        tags.remove(field);
      } else {
        if (!tags.containsKey(field)) {
          FloatTag t = new FloatTag(field, value);
          tags.put(field, t);
        } else {
          ((FloatTag) tags.get(field)).v = value;
        }
      }
    }

    public void setFloatList(String field,
                             float... values) throws FormatException {
      ListTag t = (ListTag) tags.get(field);
      if (t == null) {
        t = new ListTag(field, Type.FLOAT);
        tags.put(field, t);
      }
      for (float f : values) {
        t.Add(new FloatTag(null, f));
      }
    }

    public ListTag createCompoundList(String field) {
      ListTag t = new ListTag(field, Type.COMPOUND);
      tags.put(field, t);
      return t;
    }

    public void setDoubleList(String field,
                              double... values) throws FormatException {
      ListTag t = (ListTag) tags.get(field);
      if (t == null) {
        t = new ListTag(field, Type.DOUBLE);
        tags.put(field, t);
      }
      for (double f : values) {
        t.Add(new DoubleTag(null, f));
      }
    }

    /**
     * Returns the tag with the given type and name or throws an exception.
     *
     * @param type The type of tag.
     * @param n    The name of the tag.
     * @return The tag, guaranteed to be of the type specified.
     * @throws FormatException if the tag doesn't exist or is of the wrong type.
     */
    public Tag find(Type type, String n) throws FormatException {
      Tag t = tags.get(n);
      if (t == null) {
        throw new FormatException("No tag with the name \"" + n + "\"");
      } else if (t.Type() != type) {
        throw new FormatException("\"" + n + "\" is " + t.Type() + " instead of " + type);
      }
      return t;
    }

    /**
     * Removes the tag with the given name.
     *
     * @param n The name of the tag to remove.
     * @return The tag that was removed, or null if the tag didn't exist.
     */
    public Tag Remove(String n) {
      return tags.remove(n);
    }

    /**
     * Returns an iterator over this compound tag.
     *
     * @return An iterator over this compound tag.
     */
    @Override
    public Iterator<Tag> iterator() {
      return tags.values().iterator();
    }

    /**
     * Returns an independent clone of this Compound tag.
     *
     * @return An independent clone of this Compound tag.
     */
    @Override
    public Compound clone() {
      Compound c = (Compound) super.clone();
      c.tags = new HashMap<>();
      for (Map.Entry<String, Tag> e : tags.entrySet()) {
        c.tags.put(e.getKey(), e.getValue().clone());
      }
      return c;
    }
  }

  /**
   * TAG_Int_Array
   */
  public static final class IntArray extends Tag {
    /**
     * The integer array in raw form.
     */
    public int[] v;

    /**
     * The normal constructor.
     *
     * @param name The name of this integer array.
     * @param i    The initial integer array.
     */
    public IntArray(String name, int[] i) {
      super(name);
      v = i;
    }

    /**
     * The DeserializePayload constructor.
     *
     * @param name The name of this integer array.
     * @param i    The <code>InputStream</code> to deserialize the integer array from.
     * @throws IOException     if the input operation generates an exception.
     * @throws FormatException if the integer array size is negative.
     */
    public IntArray(String name, InputStream i) throws IOException, FormatException //DeserializePayload
    {
      this(name, (int[]) null);
      DataInputStream dis = new DataInputStream(i);
      int size = dis.readInt();
      if (size < 0) {
        throw new FormatException("Integer Array size was negative: " + size);
      }
      v = new int[size];
      for (int j = 0; j < size; ++j) {
        v[j] = dis.readInt();
      }
    }

    @Override
    public void writeJson(JsonWriter writer,
                          Method recursive) throws IOException {
      writer.beginArray();
      for (int value : v) {
        writer.value(value);
      }
      writer.endArray();
    }

    /**
     * Returns the tag type that corresponds to TAG_Int_Array (?).
     *
     * @return <code>Type.INTARRAY</code>.
     */
    @Override
    public Type Type() {
      return Type.INTARRAY;
    }

    /**
     * Serializes the integer array to the <code>OutputStream</code>.
     *
     * @param o The <code>OutputStream</code> to serialize this integer array to.
     * @throws IOException if the output operation generates an exception.
     */
    @Override
    protected void SerializePayload(OutputStream o) throws IOException {
      DataOutputStream dos = new DataOutputStream(o);
      dos.writeInt(v.length);
      for (int i : v) {
        dos.writeInt(i);
      }
    }

    /**
     * Gives a textual representation of this integer array with each integer in base-10.
     *
     * @return A textual representation of this integer array with each integer in base-10.
     */
    @Override
    public String toString() {
      String s = "";
      for (int i : v) {
        if (s.length() != 0) {
          s += ", ";
        }
        s += i;
      }
      return "IntTag Array" + QuoteName() + ": [" + s + "]";
    }

    /**
     * Returns an independent clone of this Integer Array.
     *
     * @return An independent clone of this Integer Array.
     */
    @Override
    public IntArray clone() {
      IntArray ia = (IntArray) super.clone();
      ia.v = Arrays.copyOf(v, v.length);
      return ia;
    }
  }
}
