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
package org.apache.juneau.dto.html5;

import org.apache.juneau.annotation.*;
import org.apache.juneau.xml.annotation.*;

/**
 * DTO for an HTML <a class="doclink" href="https://www.w3.org/TR/html5/document-metadata.html#the-style-element">&lt;style&gt;</a> element.
 * <p>
 */
@Bean(typeName="style")
public class Style extends HtmlElementText {

	/**
	 * <a class="doclink" href="https://www.w3.org/TR/html5/document-metadata.html#attr-style-media">media</a> attribute.
	 * Applicable media.
	 * @param media The new value for this attribute.
	 * @return This object (for method chaining).
	 */
	public final Style media(String media) {
		attr("media", media);
		return this;
	}

	/**
	 * <a class="doclink" href="https://www.w3.org/TR/html5/document-metadata.html#attr-style-type">type</a> attribute.
	 * Type of embedded resource.
	 * @param type The new value for this attribute.
	 * @return This object (for method chaining).
	 */
	public final Style type(String type) {
		attr("type", type);
		return this;
	}


	//--------------------------------------------------------------------------------
	// Overridden methods
	//--------------------------------------------------------------------------------

	@Xml(format=XmlFormat.TEXT_PWS)
	@Override /* HtmlElementText */
	public Object getText() {
		return super.getText();
	}

	@Override /* HtmlElement */
	public final Style _class(String _class) {
		super._class(_class);
		return this;
	}

	@Override /* HtmlElement */
	public final Style id(String id) {
		super.id(id);
		return this;
	}

	@Override /* HtmlElementText */
	public final Style text(Object text) {
		super.text(text);
		return this;
	}
}