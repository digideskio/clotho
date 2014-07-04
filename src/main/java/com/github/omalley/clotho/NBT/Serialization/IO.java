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
package com.github.omalley.clotho.NBT.Serialization;

import com.github.omalley.clotho.NBT.FormatException;
import com.github.omalley.clotho.NBT.Tag;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * This class can serialize and deserialize classes that implement <code>NBTable</code> to/from NBT structures.
 * @author LB
 */
public final class IO
{
	/**
	 * Serializes an instance of an NBTable class to a compound tag by serializing all non-transient fields that it can, including those of any NBTable super classes; fields that are null will be ignored. It can handle the primitive types, strings, other NBTable objects, and up to one dimensional arrays of such. It will also serialize fields of type Class<?> by saving the name of the class. Additionally, if one of the classes has an instance method called "ToNBT" that takes only a StringTag for its name as a parameter and returns a Tag, that will also be serialized. Here is an example declaration:<br />
	 * <code>public Tag ToNBT(StringTag name);</code>
	 * @param name The name of the compound tag to return.
	 * @param obj The object to serialize to the compound tag.
	 * @param preferList Whether to prefer TAG_List over TAG_Byte_Array and TAG_Int_Array when faced with byte[] and int[] types.
	 * @return The compound tag representing the fully serialized NBTable object.
	 * @throws IllegalAccessException in rare cases.
	 */
	public static Tag.Compound Serialize(String name, NBTable obj, boolean preferList) throws IllegalAccessException, FormatException
	{
		return Serialize(name, obj.getClass(), obj, preferList);
	}
	/**
	 * The recursive method that does all the work for the above public method; used to hide the ugly Class parameter.
	 * @param name Same as above.
	 * @param clazz The class level at which to serialize the fields of.
	 * @param obj Same as above.
	 * @param preferList Same as above.
	 * @return Same as above.
	 * @throws IllegalAccessException in the same cases as above.
	 */
  @SuppressWarnings("unchecked")
	private static Tag.Compound Serialize(String name, Class<? extends NBTable> clazz, NBTable obj, boolean preferList) throws IllegalAccessException, FormatException
	{
		Class<?> sup = clazz.getSuperclass();
		Tag.Compound t;
		if(sup != null && NBTable.class.isAssignableFrom(sup))
		{
			t = Serialize(name, (Class<? extends NBTable>)sup, obj, preferList);
		}
		else
		{
			t = new Tag.Compound(name);
		}
		for(Field field : clazz.getDeclaredFields())
		{
			field.setAccessible(true);
			if(!Modifier.isTransient(field.getModifiers()))
			{
				Object o = field.get(obj);
				if(o == null)
				{
					continue;
				}
				Class<?> c = field.getClass();
				String n = field.getName();
				if(o instanceof NBTable)
				{
					t.Add(Serialize(n, (NBTable)o, preferList));
				}
				else if(o instanceof Byte)
				{
					t.Add(new Tag.ByteTag(n, (Byte)o));
				}
				else if(o instanceof Short)
				{
					t.Add(new Tag.ShortTag(n, (Short)o));
				}
				else if(o instanceof Integer)
				{
					t.Add(new Tag.IntTag(n, (Integer)o));
				}
				else if(o instanceof Long)
				{
					t.Add(new Tag.LongTag(n, (Long)o));
				}
				else if(o instanceof Float)
				{
					t.Add(new Tag.FloatTag(n, (Float)o));
				}
				else if(o instanceof Double)
				{
					t.Add(new Tag.DoubleTag(n, (Double)o));
				}
				else if(o instanceof String)
				{
					t.Add(new Tag.StringTag(n, (String)o));
				}
				else if(NBTable[].class.isAssignableFrom(c))
				{
					Tag.ListTag list = new Tag.ListTag(n, Tag.Type.COMPOUND);
						for(NBTable nbtable : (NBTable[])o)
						{
							list.Add(Serialize(null, nbtable, preferList));
						}
						t.Add(list);
				}
				else if(o instanceof byte[])
				{
					if(preferList)
					{
						Tag.ListTag list = new Tag.ListTag(n, Tag.Type.BYTE);
							for(byte b : (byte[])o)
							{
								list.Add(new Tag.ByteTag(null, b));
							}
						t.Add(list);
					}
					else
					{
						t.Add(new Tag.ByteArray(n, (byte[])o));
					}
				}
				else if(o instanceof short[])
				{
					Tag.ListTag list = new Tag.ListTag(n, Tag.Type.SHORT);
						for(short s : (short[])o)
						{
							list.Add(new Tag.ShortTag(null, s));
						}
						t.Add(list);
				}
				else if(o instanceof int[])
				{
					if(preferList)
					{
						Tag.ListTag list = new Tag.ListTag(n, Tag.Type.INT);
							for(int i : (int[])o)
							{
								list.Add(new Tag.IntTag(null, i));
							}
							t.Add(list);
					}
					else
					{
						t.Add(new Tag.IntArray(n, (int[])o));
					}
				}
				else if(o instanceof long[])
				{
					Tag.ListTag list = new Tag.ListTag(n, Tag.Type.LONG);
						for(long l : (long[])o)
						{
							list.Add(new Tag.LongTag(null, l));
						}
						t.Add(list);
				}
				else if(o instanceof float[])
				{
					Tag.ListTag list = new Tag.ListTag(n, Tag.Type.FLOAT);
						for(float f : (float[])o)
						{
							list.Add(new Tag.FloatTag(null, f));
						}
						t.Add(list);
				}
				else if(o instanceof double[])
				{
					Tag.ListTag list = new Tag.ListTag(n, Tag.Type.DOUBLE);
						for(double d : (double[])o)
						{
							list.Add(new Tag.DoubleTag(null, d));
						}
						t.Add(list);
				}
				else if(o instanceof String[])
				{
					Tag.ListTag list = new Tag.ListTag(n, Tag.Type.STRING);
						for(String s : (String[])o)
						{
							list.Add(new Tag.StringTag(null, s));
						}
						t.Add(list);
				}
				else if(o instanceof Class<?>)
				{
					t.Add(new Tag.StringTag(n, ((Class<?>)o).getCanonicalName()));
				}
				else
				{
					try
					{
						Method m = c.getDeclaredMethod("ToNBT", String.class);
						m.setAccessible(true);
						if(Tag.class.isAssignableFrom(m.getReturnType()))
						{
							t.Add((Tag)m.invoke(o, n));
						}
					}
					catch(NoSuchMethodException|InvocationTargetException e)
					{
					}
				}
			}
		}
		return t;
	}
	/**
	 * Deserializes a compound tag and returns a new instance of the given class. In order to construct the instance the given class must have a constructor that takes a <code>Tag.Compound</code> as its only parameter. The constructor must initialize any null fields to the bare minimum of an instantiated class of the correct type if the fields are to be deserialized, otherwise null fields are ignored. See {@url Serialize} for information.
	 * @param clazz The class to deserialize.
	 * @param outer The outer class reference to use to construct non-static inner classes. May be null if the given class is not a non-static inner class.
	 * @param t The tag to deserialize from.
	 * @return An instance of the deserialized class.
	 * @throws IllegalArgumentException if this method cannot find a way to properly instantiate the given class.
	 * @throws NoSuchMethodException If the required constructor for deserialization is not found.
	 * @throws InstantiationException if the required constructor throws an exception at instantiation.
	 * @throws IllegalAccessException in rare cases.
	 * @throws InvocationTargetException if the required constructor throws an exception.
	 * @throws FormatException if a required tag cannot be found to deserialize to the class.
	 * @throws ClassNotFoundException if a class object cannot be found to deserialize to Class fields.
	 */
	public static NBTable Deserialize(Class<? extends NBTable> clazz, Object outer, Tag.Compound t) throws IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, FormatException, ClassNotFoundException
	{
		Class<?> out;
		Constructor<? extends NBTable> cons;
		NBTable obj;
		if((out = clazz.getDeclaringClass()) != null && !Modifier.isStatic(clazz.getModifiers()))
		{
			cons = clazz.getDeclaredConstructor(out, Tag.Compound.class);
			cons.setAccessible(true);
			obj = cons.newInstance(outer, t);
		}
		else if(out == null || Modifier.isStatic(clazz.getModifiers()))
		{
			cons = clazz.getDeclaredConstructor(Tag.Compound.class);
			cons.setAccessible(true);
			obj = cons.newInstance(t);
		}
		else
		{
			throw new IllegalArgumentException("Can't tell how to construct this class...");
		}
		Deserialize(clazz, obj, t);
		return obj;
	}
	/**
	 * The recursive method that does all the work for the above public method; used to hide the ugly NBTable parameter.
	 * @param clazz Same as above.
	 * @param obj The object to deserialize to.
	 * @param t Same as above.
	 * @throws NoSuchMethodException in the same cases as above.
	 * @throws InstantiationException in the same cases as above.
	 * @throws IllegalAccessException in the same cases as above.
	 * @throws InvocationTargetException in the same cases as above.
	 * @throws FormatException in the same cases as above.
	 * @throws ClassNotFoundException in the same cases as above.
	 */
  @SuppressWarnings("unchecked")
	private static void Deserialize(Class<? extends NBTable> clazz, NBTable obj, Tag.Compound t) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, FormatException, ClassNotFoundException
	{
		Class<?> sup = clazz.getSuperclass();
		if(sup != null && NBTable.class.isAssignableFrom(sup))
		{
			Deserialize((Class<? extends NBTable>)sup, obj, t);
		}
	FieldLoop:
		for(Field field : clazz.getDeclaredFields())
		{
			field.setAccessible(true);
			if(!Modifier.isTransient(field.getModifiers()))
			{
				Object o = field.get(obj);
				if(o == null)
				{
					continue;
				}
				Class<?> c = field.getClass();
				String n = field.getName();
				if(o instanceof NBTable)
				{
					field.set(obj, Deserialize(c.asSubclass(NBTable.class), (Object)null, (Tag.Compound)t.find(Tag.Type.COMPOUND, n)));
				}
				else if(o instanceof Byte)
				{
					field.set(obj, ((Tag.ByteTag)t.find(Tag.Type.BYTE, n)).v);
				}
				else if(o instanceof Short)
				{
					field.set(obj, ((Tag.ShortTag)t.find(Tag.Type.SHORT, n)).v);
				}
				else if(o instanceof Integer)
				{
					field.set(obj, ((Tag.IntTag)t.find(Tag.Type.INT, n)).v);
				}
				else if(o instanceof Long)
				{
					field.set(obj, ((Tag.LongTag)t.find(Tag.Type.LONG, n)).v);
				}
				else if(o instanceof Float)
				{
					field.set(obj, ((Tag.FloatTag)t.find(Tag.Type.FLOAT, n)).v);
				}
				else if(o instanceof Double)
				{
					field.set(obj, ((Tag.DoubleTag)t.find(Tag.Type.DOUBLE, n)).v);
				}
				else if(o instanceof String)
				{
					field.set(obj, ((Tag.StringTag)t.find(Tag.Type.STRING, n)).v);
				}
				else if(NBTable[].class.isAssignableFrom(c))
				{
					Tag.ListTag list = (Tag.ListTag)t.find(Tag.Type.LIST, n);
					if(list.Supports() != Tag.Type.COMPOUND)
					{
						throw new FormatException("Expected list of Compound tags, got list of: "+list.Supports());
					}
					NBTable[] arr = (NBTable[])Array.newInstance(c.getComponentType(), list.size());
					for(int i = 0; i < arr.length; ++i)
					{
						arr[i] = Deserialize(c.asSubclass(NBTable.class), (Object)null, (Tag.Compound)list.Get(i));
					}
					field.set(obj, arr);
				}
				else if(o instanceof byte[])
				{
					for(Tag list : t)
					{
						if(n.equals(list.getName()))
						{
							Tag.ListTag tl;
							if(list.Type() == Tag.Type.BYTEARRAY)
							{
								byte[] arr = ((Tag.ByteArray)list).v;
								field.set(obj, Arrays.copyOf(arr, arr.length));
								continue FieldLoop;
							}
							else if(list.Type() == Tag.Type.LIST && (tl = (Tag.ListTag)list).Supports() == Tag.Type.BYTE)
							{
								byte[] arr = new byte[tl.size()];
								for(int j = 0; j < tl.size(); ++j)
								{
									arr[j] = ((Tag.ByteTag)tl.Get(j)).v;
								}
								field.set(obj, arr);
								continue FieldLoop;
							}
						}
					}
					throw new FormatException("Could not find a ByteArray tag or ListTag of ByteTag tags with the name \""+n+"\"");
				}
				else if(o instanceof short[])
				{
					Tag.ListTag list = (Tag.ListTag)t.find(Tag.Type.LIST, n);
					if(list.Supports() != Tag.Type.SHORT)
					{
						throw new FormatException("Expected list of ShortTag tags, got list of: "+list.Supports());
					}
					short[] arr = new short[list.size()];
					for(int i = 0; i < list.size(); ++i)
					{
						arr[i] = ((Tag.ShortTag)list.Get(i)).v;
					}
				}
				else if(o instanceof int[])
				{
					for(Tag list : t)
					{
						if(n.equals(list.getName()))
						{
							Tag.ListTag tl;
							if(list.Type() == Tag.Type.INTARRAY)
							{
								int[] arr = ((Tag.IntArray)list).v;
								field.set(obj, Arrays.copyOf(arr, arr.length));
								continue FieldLoop;
							}
							else if(list.Type() == Tag.Type.LIST && (tl = (Tag.ListTag)list).Supports() == Tag.Type.INT)
							{
								int[] arr = new int[tl.size()];
								for(int j = 0; j < tl.size(); ++j)
								{
									arr[j] = ((Tag.IntTag)tl.Get(j)).v;
								}
								field.set(obj, arr);
								continue FieldLoop;
							}
						}
					}
					throw new FormatException("Could not find an IntArray tag or ListTag of IntTag tags with the name \""+n+"\"");
				}
				else if(o instanceof long[])
				{
					Tag.ListTag list = (Tag.ListTag)t.find(Tag.Type.LIST, n);
					if(list.Supports() != Tag.Type.LONG)
					{
						throw new FormatException("Expected list of LongTag tags, got list of: "+list.Supports());
					}
					long[] arr = new long[list.size()];
					for(int i = 0; i < list.size(); ++i)
					{
						arr[i] = ((Tag.LongTag)list.Get(i)).v;
					}
				}
				else if(o instanceof float[])
				{
					Tag.ListTag list = (Tag.ListTag)t.find(Tag.Type.LIST, n);
					if(list.Supports() != Tag.Type.FLOAT)
					{
						throw new FormatException("Expected list of FloatTag tags, got list of: "+list.Supports());
					}
					float[] arr = new float[list.size()];
					for(int i = 0; i < list.size(); ++i)
					{
						arr[i] = ((Tag.FloatTag)list.Get(i)).v;
					}
				}
				else if(o instanceof double[])
				{
					Tag.ListTag list = (Tag.ListTag)t.find(Tag.Type.LIST, n);
					if(list.Supports() != Tag.Type.DOUBLE)
					{
						throw new FormatException("Expected list of DoubleTag tags, got list of: "+list.Supports());
					}
					double[] arr = new double[list.size()];
					for(int i = 0; i < list.size(); ++i)
					{
						arr[i] = ((Tag.DoubleTag)list.Get(i)).v;
					}
				}
				else if(o instanceof String[])
				{
					Tag.ListTag list = (Tag.ListTag)t.find(Tag.Type.LIST, n);
					if(list.Supports() != Tag.Type.STRING)
					{
						throw new FormatException("Expected list of StringTag tags, got list of: "+list.Supports());
					}
					String[] arr = new String[list.size()];
					for(int i = 0; i < list.size(); ++i)
					{
						arr[i] = ((Tag.StringTag)list.Get(i)).v;
					}
				}
				else if(o instanceof Class<?>)
				{
					field.set(obj, Class.forName(((Tag.StringTag)t.find(Tag.Type.STRING, n)).v));
				}
				else
				{
					Constructor<?> subcons = c.getConstructor(Tag.class);
					subcons.setAccessible(true);
					field.set(obj, subcons.newInstance(t.get(n)));
				}
			}
		}
	}

	/**
	 * The constructor for this class, which you won't be using.
	 * @throws UnsupportedOperationException always.
	 */
	private IO() throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
}
