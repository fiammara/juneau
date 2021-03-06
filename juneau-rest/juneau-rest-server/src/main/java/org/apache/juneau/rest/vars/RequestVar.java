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
package org.apache.juneau.rest.vars;

import org.apache.juneau.*;
import org.apache.juneau.internal.*;
import org.apache.juneau.rest.*;
import org.apache.juneau.svl.*;

/**
 * Request attribute variable resolver.
 *
 * <p>
 * The format for this var is <js>"$R{key1[,key2...]}"</js>.
 * <br>When multiple keys are used, returns the first non-null/empty value.
 *
 * <p>
 * The possible values are:
 * <ul>
 * 	<li><js>"authorityPath"</js> - Value returned by {@link RestRequest#getAuthorityPath()}
 * 	<li><js>"contextPath"</js> - Value returned by {@link RestRequest#getContextPath()}
 * 	<li><js>"method"</js> - Value returned by {@link RestRequest#getMethod()}
 * 	<li><js>"methodDescription"</js> - Value returned by {@link RestRequest#getMethodDescription()}
 * 	<li><js>"methodSummary"</js> - Value returned by {@link RestRequest#getMethodSummary()}
 * 	<li><js>"pathInfo"</js> - Value returned by {@link RestRequest#getPathInfo()}
 * 	<li><js>"requestParentURI"</js> - Value returned by {@link UriContext#getRootRelativePathInfoParent()}
 * 	<li><js>"requestURI"</js> - Value returned by {@link RestRequest#getRequestURI()}
 * 	<li><js>"resourceDescription"</js> - Value returned by {@link RestRequest#getResourceDescription()}
 * 	<li><js>"resourceTitle"</js> - See {@link RestRequest#getResourceTitle()}
 * 	<li><js>"servletParentURI"</js> - Value returned by {@link UriContext#getRootRelativeServletPathParent()}
 * 	<li><js>"servletPath"</js> - See {@link RestRequest#getServletPath()}
 * 	<li><js>"servletURI"</js> - See {@link UriContext#getRootRelativeServletPath()}
 * </ul>
 *
 * <h5 class='section'>Example:</h5>
 * <p class='bcode w800'>
 * 	String resourceTitle = restRequest.resolveVars(<js>"$R{resourceTitle}"</js>);
 * 	String resourceTitleOrDescription = restRequest.resolveVars(<js>"$R{resourceTitle,resourceDescription}"</js>);
 * </p>
 *
 * <ul class='notes'>
 * 	<li>
 * 		This variable resolver requires that a {@link RestRequest} object be set as a context object on the resolver
 * 		or a session object on the resolver session.
 * 	<li>
 * 		For security reasons, nested and recursive variables are not resolved.
 * </ul>
 *
 * <ul class='seealso'>
 * 	<li class='link'>{@doc juneau-rest-server.SvlVariables}
 * </ul>
 */
public class RequestVar extends MultipartResolvingVar {

	private static final String SESSION_req = "req";
	private static final String SESSION_res = "res";


	/** The name of this variable. */
	public static final String NAME = "R";

	/**
	 * Constructor.
	 */
	public RequestVar() {
		super(NAME);
	}

	@Override /* Var */
	protected boolean allowNested() {
		return false;
	}

	@Override /* Var */
	protected boolean allowRecurse() {
		return false;
	}

	@Override /* Var */
	public String resolve(VarResolverSession session, String key) {
		RestRequest req = session.getSessionObject(RestRequest.class, SESSION_req, true);
		char c = StringUtils.charAt(key, 0);
		if (c == 'a') {
			if ("authorityPath".equals(key))
				return req.getAuthorityPath();
		} else if (c == 'c') {
			if ("contextPath".equals(key))
				return req.getContextPath();
		} else if (c == 'm') {
			if ("method".equals(key))
				return req.getMethod();
			if ("methodDescription".equals(key))
				return req.getMethodDescription();
			if ("methodSummary".equals(key))
				return req.getMethodSummary();
		} else if (c == 'p') {
			if ("pathInfo".equals(key))
				return req.getPathInfo();
		} else if (c == 'r') {
			if ("requestParentURI".equals(key))
				return req.getUriContext().getRootRelativePathInfoParent();
			if ("requestURI".equals(key))
				return req.getRequestURI();
			if ("resourceDescription".equals(key))
				return req.getResourceDescription();
			if ("resourceTitle".equals(key))
				return req.getResourceTitle();
		} else if (c == 's') {
			if ("servletClass".equals(key))
				return req.getContext().getResource().getClass().getName();
			if ("servletClassSimple".equals(key))
				return req.getContext().getResource().getClass().getSimpleName();
			if ("servletParentURI".equals(key))
				return req.getUriContext().getRootRelativeServletPathParent();
			if ("servletPath".equals(key))
				return req.getServletPath();
			if ("servletURI".equals(key))
				return req.getUriContext().getRootRelativeServletPath();
			if ("siteName".equals(key))
				return req.getSiteName();
		}
		return req.getAttributes().getString(key);
	}

	@Override /* Var */
	public boolean canResolve(VarResolverSession session) {
		return session.hasSessionObject(SESSION_req) && session.hasSessionObject(SESSION_res);
	}
}