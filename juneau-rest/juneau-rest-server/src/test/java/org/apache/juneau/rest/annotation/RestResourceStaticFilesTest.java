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
package org.apache.juneau.rest.annotation;

import static org.apache.juneau.http.HttpMethodName.*;

import org.apache.juneau.rest.mock.*;
import org.junit.*;
import org.junit.runners.*;

/**
 * Tests that validate the behavior of @RestResource(staticFiles).
 */
@SuppressWarnings({"javadoc"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RestResourceStaticFilesTest {

	//====================================================================================================
	// Basic tests
	//====================================================================================================

	@RestResource(staticFiles={"xdocs:xdocs","xdocs2:xdocs2:{Foo:'Bar'}"})
	public static class A {
		@RestMethod(name=GET)
		public String test() {
			return null;
		}
	}
	static MockRest a = MockRest.create(A.class);

	@Test
	public void a01() throws Exception {
		a.request("GET", "/xdocs/test.txt").execute().assertBodyContains("OK-1");
		a.request("GET", "/xdocs/xsubdocs/test.txt").execute().assertBodyContains("OK-2");
	}
	@Test
	public void a02_preventPathTraversals() throws Exception {
		a.request("GET", "/xdocs/xsubdocs/../test.txt?noTrace=true").execute().assertStatus(404);
		a.request("GET", "/xdocs/xsubdocs/%2E%2E/test.txt?noTrace=true").execute().assertStatus(404);
	}

	//====================================================================================================
	// Static files with response headers.
	//====================================================================================================

	@RestResource(staticFiles={"xdocs:xdocs:{Foo:'Bar'}"})
	public static class B {
		@RestMethod(name=GET)
		public String test() {
			return null;
		}
	}
	static MockRest b = MockRest.create(B.class);

	@Test
	public void b01() throws Exception {
		b.request("GET", "/xdocs/test.txt").execute().assertHeader("Foo","Bar").assertBodyContains("OK-1");
	}
}