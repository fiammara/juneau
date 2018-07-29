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
package org.apache.juneau.rest.response;

import java.net.*;

import org.apache.juneau.http.annotation.*;

/**
 * Represents an <code>HTTP 308 Permanent Redirect</code> response.
 *
 * <p>
 * The request and all future requests should be repeated using another URI. 307 and 308 parallel the behaviors of 302 and 301, but do not allow the HTTP method to change.
 * So, for example, submitting a form to a permanently redirected resource may continue smoothly.
 */
@Response(code=308, example="'Permanent Redirect'")
public class PermanentRedirect {

	/** Reusable instance. */
	public static final PermanentRedirect INSTANCE = new PermanentRedirect();

	private final URI location;

	/**
	 * Constructor.
	 */
	public PermanentRedirect() {
		this(null);
	}

	/**
	 * Constructor.
	 *
	 * @param location <code>Location</code> header value.
	 */
	public PermanentRedirect(URI location) {
		this.location = location;
	}

	@Override /* Object */
	public String toString() {
		return "Permanent Redirect";
	}

	/**
	 * @return <code>Location</code> header value.
	 */
	@Header(name="Location", description="")
	public URI getLocation() {
		return location;
	}
}