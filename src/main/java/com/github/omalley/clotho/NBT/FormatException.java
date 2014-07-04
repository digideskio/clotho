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

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The generic "this data is wonky" exception.
 * @author LB
 */
public class FormatException extends Exception {
	/**
	 * The tag(s) that caused the problem, if any.
	 */
	List<Tag> t = new ArrayList<>();

	/**
	 * Instantiates this exception with the given message.
	 * @param msg The message to be seen with this exception.
	 * @param tags The tag(s) (if any) that were malformed.
	 */
	public FormatException(String msg, Tag... tags)
	{
		super(msg);
		t.addAll(Arrays.asList(tags));
	}
	/**
	 * Instantiates this exception with the given message and cause.
	 * @param msg The message to be seen with this exception.
	 * @param cause The exception that caused the trouble in the first place.
	 * @param tags The tag(s) (if any) that were malformed.
	 */
	public FormatException(String msg, Throwable cause, Tag... tags)
	{
		super(msg, cause);
		t.addAll(Arrays.asList(tags));
	}
	/**
	 * Instantiates this exception with just the causing exception.
	 * @param cause The exception that caused the trouble in the first place.
	 * @param tags The tag(s) (if any) that were malformed.
	 */
	public FormatException(Throwable cause, Tag... tags)
	{
		super(cause);
		t.addAll(Arrays.asList(tags));
	}

	/**
	 * Returns the tag(s) (if any) that were malformed.
	 * @return The tag(s) (if any) that were malformed.
	 */
	public Tag[] Tags()
	{
		return t.toArray(new Tag[0]);
	}
	/**
	 * Adds tags to the list of the malformed tags.
	 * @param tags The tags that were malformed.
	 */
	public void Add(Tag... tags)
	{
		t.addAll(Arrays.asList(tags));
	}
}
