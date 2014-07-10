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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * The input/output class for this NBT package.
 * Like the java.lang.Math class, this class is final, uninstantiable, and both of its methods are static.
 * @author LB
 */
public final class IO {
	/**
	 * Reads an NBT structure from a GZipped <code>InputStream</code>.
	 * @param i The <code>InputStream</code> to read the GZipped NBT structure from.
	 * @return The root compound tag of the NBT structure.
	 * @throws java.io.IOException if the input operation generates an exception.
	 * @throws FormatException if the NBT format isn't quite right.
	 */
	public static Tag.Compound Read(InputStream i) throws java.io.IOException, FormatException
	{
		return ReadUncompressed(new GZIPInputStream(i));
	}
	/**
	 * Reads an NBT structure from an uncompressed <code>InputStream</code>.
	 * @param i The <code>InputStream</code> to read the uncompressed NBT structure from.
	 * @return The root compound tag of the NBT structure.
	 * @throws java.io.IOException if the input operation generates an exception.
	 * @throws FormatException if the NBT format isn't quite right.
	 */
	public static Tag.Compound ReadUncompressed(InputStream i) throws IOException, FormatException
	{
		int ch = i.read();
		if(ch == Tag.Type.COMPOUND.ordinal())
		{
			return new Tag.Compound(new Tag.StringTag(null, i).v, i);
		}
		else if(ch == -1)
		{
			throw new FormatException("Unexpected end of stream before reading root tag");
		}
		throw new FormatException("Root tag was not a Compound tag; tag ID was "+ch);
	}

	/**
	 * Writes an NBT structure in a GZipped format to an <code>OutputStream</code>.
	 * @param nbt The root compound tag of the NBT structure.
	 * @param o The <code>OutputStream</code> to write the GZipped NBT structure to.
	 * @throws java.io.IOException if the output operation generates an exception.
	 */
	public static void Write(Tag.Compound nbt, OutputStream o) throws IOException
	{
		GZIPOutputStream go = new GZIPOutputStream(o);
		WriteUncompressed(nbt, go);
		go.finish();
	}
	/**
	 * Writes an NBT structure in an uncompressed format to an <code>OutputStream</code>.
	 * @param nbt The root compound tag of the NBT structure.
	 * @param o The <code>OutputStream</code> to write the uncompressed NBT structure to.
	 * @throws java.io.IOException if the output operation generates an exception.
	 */
	public static void WriteUncompressed(Tag.Compound nbt, OutputStream o) throws IOException
	{
		nbt.Serialize(o);
	}

	/**
	 * The constructor that you won't be using.
	 * @throws UnsupportedOperationException always.
	 */
	private IO() throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
}
