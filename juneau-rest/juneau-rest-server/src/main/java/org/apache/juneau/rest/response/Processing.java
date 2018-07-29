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

import org.apache.juneau.http.annotation.*;

/**
 * Represents an <code>HTTP 102 Processing</code> response.
 *
 * <p>
 * A WebDAV request may contain many sub-requests involving file operations, requiring a long time to complete the request.
 * This code indicates that the server has received and is processing the request, but no response is available yet.
 * This prevents the client from timing out and assuming the request was lost.
 */
@Response(code=102, example="'Processing'")
public class Processing {

	/** Reusable instance. */
	public static final Processing INSTANCE = new Processing();

	@Override /* Object */
	public String toString() {
		return "Processing";
	}
}