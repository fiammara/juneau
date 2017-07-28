// ***************************************************************************************************************************
// * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements.  See the NOTICE file *
// * distributed with this work for additional information regarding copyright ownership.  The ASF licenses this file        *
// * to you under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance            *
// * with the License.  You may obtain a copy of the License at                                                              *
// *                                                                                                                         *
// *  http://www.apache.org/licenses/LICENSE-2.0                                                                             *
// *                                                                                                                         *
// * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an  *
// * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the        *
// * specific language governing permissions and limitations under the License.                                              *
// ***************************************************************************************************************************
package org.apache.juneau.serializer;

import static org.apache.juneau.internal.StringUtils.*;
import static org.apache.juneau.internal.ReflectionUtils.*;

import java.io.*;

import org.apache.juneau.*;
import org.apache.juneau.annotation.*;
import org.apache.juneau.http.*;

/**
 * Parent class for all Juneau serializers.
 *
 * <h5 class='section'>Description:</h5>
 *
 * Base serializer class that serves as the parent class for all serializers.
 *
 * <p>
 * The purpose of this class is:
 * <ul>
 * 	<li>Maintain a read-only configuration state of a serializer (i.e. {@link SerializerContext}).
 * 	<li>Create session objects used for serializing POJOs (i.e. {@link SerializerSession}).
 * 	<li>Provide convenience methods for serializing POJOs without having to construct session objects.
 * </ul>
 *
 * <p>
 * Subclasses should extend directly from {@link OutputStreamSerializer} or {@link WriterSerializer} depending on
 * whether it's a stream or character based serializer.
 *
 * <h6 class='topic'>@Produces annotation</h6>
 *
 * The media types that this serializer can produce is specified through the {@link Produces @Produces} annotation.
 * <br>
 * However, the media types can also be specified programmatically by overriding the {@link #getMediaTypes()}
 * and {@link #getResponseContentType()} methods.
 */
public abstract class Serializer extends CoreObject {

	private final MediaType[] mediaTypes;
	private final MediaType contentType;

	// Hidden constructors to force subclass from OuputStreamSerializer or WriterSerializer.
	Serializer(PropertyStore propertyStore) {
		super(propertyStore);

		Produces p = getAnnotation(Produces.class, getClass());
		if (p == null)
			throw new FormattedRuntimeException("Class ''{0}'' is missing the @Produces annotation", getClass());

		String[] mt = split(p.value());
		this.mediaTypes = new MediaType[mt.length];
		for (int i = 0; i < mt.length; i++) {
			mediaTypes[i] = MediaType.forString(mt[i]);
		}

		String ct = p.contentType().isEmpty() ? this.mediaTypes[0].toString() : p.contentType();
		contentType = ct.isEmpty() ? null : MediaType.forString(ct);
	}

	@Override /* CoreObject */
	public SerializerBuilder builder() {
		return new SerializerBuilder(propertyStore);
	}

	//--------------------------------------------------------------------------------
	// Abstract methods
	//--------------------------------------------------------------------------------

	/**
	 * Returns <jk>true</jk> if this serializer subclasses from {@link WriterSerializer}.
	 *
	 * @return <jk>true</jk> if this serializer subclasses from {@link WriterSerializer}.
	 */
	public abstract boolean isWriterSerializer();

	/**
	 * Create the session object used for actual serialization of objects.
	 *
	 * @param args
	 * 	Runtime arguments.
	 * 	These specify session-level information such as locale and URI context.
	 * 	It also include session-level properties that override the properties defined on the bean and serializer
	 * 	contexts.
	 * 	<br>If <jk>null</jk>, defaults to {@link SerializerSessionArgs#DEFAULT}.
	 * @return
	 * 	The new session object.
	 * 	<br>Note that you must call {@link SerializerSession#close()} on this object to perform any necessary
	 * 	cleanup.
	 */
	public abstract SerializerSession createSession(SerializerSessionArgs args);


	//--------------------------------------------------------------------------------
	// Convenience methods
	//--------------------------------------------------------------------------------

	/**
	 * Shortcut for calling <code>createSession(<jk>null</jk>)</code>.
	 *
	 * @return
	 * 	The new session object.
	 * 	<br>Note that you must call {@link SerializerSession#close()} on this object to perform any necessary
	 * 	cleanup.
	 */
	public final SerializerSession createSession() {
		return createSession(null);
	}

	/**
	 * Serializes a POJO to the specified output stream or writer.
	 *
	 * <p>
	 * Equivalent to calling <code>serializer.createSession().serialize(o, output);</code>
	 *
	 * @param o The object to serialize.
	 * @param output
	 * 	The output object.
	 * 	<br>Character-based serializers can handle the following output class types:
	 * 	<ul>
	 * 		<li>{@link Writer}
	 * 		<li>{@link OutputStream} - Output will be written as UTF-8 encoded stream.
	 * 		<li>{@link File} - Output will be written as system-default encoded stream.
	 * 		<li>{@link StringBuilder} - Output will be written to the specified string builder.
	 * 	</ul>
	 * 	<br>Stream-based serializers can handle the following output class types:
	 * 	<ul>
	 * 		<li>{@link OutputStream}
	 * 		<li>{@link File}
	 * 	</ul>
	 * @throws SerializeException If a problem occurred trying to convert the output.
	 */
	public final void serialize(Object o, Object output) throws SerializeException {
		SerializerSession s = createSession();
		try {
			s.serialize(o, output);
		} finally {
			s.close();
		}
	}

	/**
	 * Shortcut method for serializing objects directly to either a <code>String</code> or <code><jk>byte</jk>[]</code>
	 * depending on the serializer type.
	 *
	 * @param o The object to serialize.
	 * @return
	 * 	The serialized object.
	 * 	<br>Character-based serializers will return a <code>String</code>
	 * 	<br>Stream-based serializers will return a <code><jk>byte</jk>[]</code>
	 * @throws SerializeException If a problem occurred trying to convert the output.
	 */
	public Object serialize(Object o) throws SerializeException {
		SerializerSession s = createSession();
		try {
			return s.serialize(o);
		} finally {
			s.close();
		}
	}

	//--------------------------------------------------------------------------------
	// Other methods
	//--------------------------------------------------------------------------------

	/**
	 * Returns the media types handled based on the value of the {@link Produces} annotation on the serializer class.
	 *
	 * <p>
	 * This method can be overridden by subclasses to determine the media types programmatically.
	 *
	 * @return The list of media types.  Never <jk>null</jk>.
	 */
	public final MediaType[] getMediaTypes() {
		return mediaTypes;
	}

	/**
	 * Returns the first media type specified on this serializer via the {@link Produces} annotation.
	 *
	 * @return The media type.
	 */
	public final MediaType getPrimaryMediaType() {
		return mediaTypes == null || mediaTypes.length == 0 ? null : mediaTypes[0];
	}

	/**
	 * Optional method that returns the response <code>Content-Type</code> for this serializer if it is different from
	 * the matched media type.
	 *
	 * <p>
	 * This method is specified to override the content type for this serializer.
	 * For example, the {@link org.apache.juneau.json.JsonSerializer.Simple} class returns that it handles media type
	 * <js>"text/json+simple"</js>, but returns <js>"text/json"</js> as the actual content type.
	 * This allows clients to request specific 'flavors' of content using specialized <code>Accept</code> header values.
	 *
	 * <p>
	 * This method is typically meaningless if the serializer is being used stand-alone (i.e. outside of a REST server
	 * or client).
	 *
	 * @return The response content type.  If <jk>null</jk>, then the matched media type is used.
	 */
	public MediaType getResponseContentType() {
		return contentType;
	}
}
