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
package org.apache.juneau;

import static org.apache.juneau.BeanTraverseContext.*;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import org.apache.juneau.http.*;
import org.apache.juneau.reflect.*;
import org.apache.juneau.svl.*;

/**
 * Builder class for building instances of bean traversals.
 */
public class BeanTraverseBuilder extends BeanContextBuilder {

	/**
	 * Constructor, default settings.
	 */
	public BeanTraverseBuilder() {
		super();
	}

	/**
	 * Constructor.
	 *
	 * @param ps The initial configuration settings for this builder.
	 */
	public BeanTraverseBuilder(PropertyStore ps) {
		super(ps);
	}

	//-----------------------------------------------------------------------------------------------------------------
	// Properties
	//-----------------------------------------------------------------------------------------------------------------

	/**
	 * Configuration property:  Automatically detect POJO recursions.
	 *
	 * <p>
	 * Specifies that recursions should be checked for during traversal.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		Checking for recursion can cause a small performance penalty.
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanTraverseContext#BEANTRAVERSE_detectRecursions}
	 * </ul>
	 *
	 * @param value
	 * 	The new value for this property.
	 * 	<br>The default is <jk>false</jk>.
	 * @return This object (for method chaining).
	 */
	public BeanTraverseBuilder detectRecursions(boolean value) {
		return set(BEANTRAVERSE_detectRecursions, value);
	}

	/**
	 * Configuration property:  Automatically detect POJO recursions.
	 *
	 * <p>
	 * Shortcut for calling <code>detectRecursions(<jk>true</jk>)</code>.
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanTraverseContext#BEANTRAVERSE_detectRecursions}
	 * </ul>
	 *
	 * @return This object (for method chaining).
	 */
	public BeanTraverseBuilder detectRecursions() {
		return set(BEANTRAVERSE_detectRecursions, true);
	}

	/**
	 * Configuration property:  Ignore recursion errors.
	 *
	 * <p>
	 * If <jk>true</jk>, when we encounter the same object when traversing a tree, we set the value to <jk>null</jk>.
	 * Otherwise, an exception is thrown.
	 *
	 * <ul class='notes'>
	 * 	<li>
	 * 		Checking for recursion can cause a small performance penalty.
	 * </ul>
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanTraverseContext#BEANTRAVERSE_ignoreRecursions}
	 * </ul>
	 *
	 * @param value
	 * 	The new value for this property.
	 * 	<br>The default is <jk>false</jk>.
	 * @return This object (for method chaining).
	 */
	public BeanTraverseBuilder ignoreRecursions(boolean value) {
		return set(BEANTRAVERSE_ignoreRecursions, value);
	}

	/**
	 * Configuration property:  Ignore recursion errors.
	 *
	 * <p>
	 * Shortcut for calling <code>ignoreRecursions(<jk>true</jk>)</code>.
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanTraverseContext#BEANTRAVERSE_ignoreRecursions}
	 * </ul>
	 *
	 * @return This object (for method chaining).
	 */
	public BeanTraverseBuilder ignoreRecursions() {
		return set(BEANTRAVERSE_ignoreRecursions, true);
	}

	/**
	 * Configuration property:  Initial depth.
	 *
	 * <p>
	 * The initial indentation level at the root.
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanTraverseContext#BEANTRAVERSE_initialDepth}
	 * </ul>
	 *
	 * @param value
	 * 	The new value for this property.
	 * 	<br>The default is <c>0</c>.
	 * @return This object (for method chaining).
	 */
	public BeanTraverseBuilder initialDepth(int value) {
		return set(BEANTRAVERSE_initialDepth, value);
	}

	/**
	 * Configuration property:  Max traversal depth.
	 *
	 * <p>
	 * Abort traversal if specified depth is reached in the POJO tree.
	 * <br>If this depth is exceeded, an exception is thrown.
	 * <br>This prevents stack overflows from occurring when trying to traverse models with recursive references.
	 *
	 * <ul class='seealso'>
	 * 	<li class='jf'>{@link BeanTraverseContext#BEANTRAVERSE_maxDepth}
	 * </ul>
	 *
	 * @param value
	 * 	The new value for this property.
	 * 	<br>The default is <c>100</c>.
	 * @return This object (for method chaining).
	 */
	public BeanTraverseBuilder maxDepth(int value) {
		return set(BEANTRAVERSE_maxDepth, value);
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder annotations(Annotation...values) {
		super.annotations(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder beanClassVisibility(Visibility value) {
		super.beanClassVisibility(value);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder beanConstructorVisibility(Visibility value) {
		super.beanConstructorVisibility(value);
		return this;
	}

	@Override /* BeanContextBuilder */
	@Deprecated
	public BeanTraverseBuilder beanDictionary(Class<?>...values) {
		super.beanDictionary(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	@Deprecated
	public BeanTraverseBuilder beanDictionary(Object...values) {
		super.beanDictionary(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	@Deprecated
	public BeanTraverseBuilder beanDictionaryReplace(Class<?>...values) {
		super.beanDictionaryReplace(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	@Deprecated
	public BeanTraverseBuilder beanDictionaryReplace(Object...values) {
		super.beanDictionaryReplace(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	@Deprecated
	public BeanTraverseBuilder beanDictionaryRemove(Class<?>...values) {
		super.beanDictionaryRemove(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	@Deprecated
	public BeanTraverseBuilder beanDictionaryRemove(Object...values) {
		super.beanDictionaryRemove(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder beanFieldVisibility(Visibility value) {
		super.beanFieldVisibility(value);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder beanFilters(Class<?>...values) {
		super.beanFilters(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder beanFilters(Object...values) {
		super.beanFilters(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder beanFiltersReplace(Class<?>...values) {
		super.beanFiltersReplace(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder beanFiltersReplace(Object...values) {
		super.beanFiltersReplace(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder beanFiltersRemove(Class<?>...values) {
		super.beanFiltersRemove(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder beanFiltersRemove(Object...values) {
		super.beanFiltersRemove(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder beanMapPutReturnsOldValue(boolean value) {
		super.beanMapPutReturnsOldValue(value);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder beanMapPutReturnsOldValue() {
		super.beanMapPutReturnsOldValue();
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder beanMethodVisibility(Visibility value) {
		super.beanMethodVisibility(value);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder beansRequireDefaultConstructor(boolean value) {
		super.beansRequireDefaultConstructor(value);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder beansRequireDefaultConstructor() {
		super.beansRequireDefaultConstructor();
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder beansRequireSerializable(boolean value) {
		super.beansRequireSerializable(value);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder beansRequireSerializable() {
		super.beansRequireSerializable();
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder beansRequireSettersForGetters(boolean value) {
		super.beansRequireSettersForGetters(value);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder beansRequireSettersForGetters() {
		super.beansRequireSettersForGetters();
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder beansRequireSomeProperties(boolean value) {
		super.beansRequireSomeProperties(value);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder beanTypePropertyName(String value) {
		super.beanTypePropertyName(value);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder bpi(Class<?> beanClass, String value) {
		super.bpi(beanClass, value);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder bpi(Map<String,String> values) {
		super.bpi(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder bpi(String beanClassName, String value) {
		super.bpi(beanClassName, value);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder bpx(Class<?> beanClass, String properties) {
		super.bpx(beanClass, properties);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder bpx(Map<String,String> values) {
		super.bpx(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder bpx(String beanClassName, String value) {
		super.bpx(beanClassName, value);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder bpro(Class<?> beanClass, String value) {
		super.bpro(beanClass, value);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder bpro(Map<String,String> values) {
		super.bpro(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder bpro(String beanClassName, String value) {
		super.bpro(beanClassName, value);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder bpwo(Class<?> beanClass, String properties) {
		super.bpwo(beanClass, properties);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder bpwo(Map<String,String> values) {
		super.bpwo(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder bpwo(String beanClassName, String value) {
		super.bpwo(beanClassName, value);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder debug() {
		super.debug();
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder dictionary(Class<?>...values) {
		super.dictionary(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder dictionary(Object...values) {
		super.dictionary(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder dictionaryReplace(Class<?>...values) {
		super.dictionaryReplace(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder dictionaryReplace(Object...values) {
		super.dictionaryReplace(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder dictionaryRemove(Class<?>...values) {
		super.dictionaryRemove(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder dictionaryRemove(Object...values) {
		super.dictionaryRemove(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public <T> BeanTraverseBuilder example(Class<T> c, T o) {
		super.example(c, o);
		return this;
	}

	@Override /* BeanContextBuilder */
	public <T> BeanTraverseBuilder exampleJson(Class<T> c, String value) {
		super.exampleJson(c, value);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder ignoreInvocationExceptionsOnGetters(boolean value) {
		super.ignoreInvocationExceptionsOnGetters(value);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder ignoreInvocationExceptionsOnGetters() {
		super.ignoreInvocationExceptionsOnGetters();
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder ignoreInvocationExceptionsOnSetters(boolean value) {
		super.ignoreInvocationExceptionsOnSetters(value);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder ignoreInvocationExceptionsOnSetters() {
		super.ignoreInvocationExceptionsOnSetters();
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder ignorePropertiesWithoutSetters(boolean value) {
		super.ignorePropertiesWithoutSetters(value);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder ignoreUnknownBeanProperties(boolean value) {
		super.ignoreUnknownBeanProperties(value);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder ignoreUnknownBeanProperties() {
		super.ignoreUnknownBeanProperties();
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder ignoreUnknownNullBeanProperties(boolean value) {
		super.ignoreUnknownNullBeanProperties(value);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder implClass(Class<?> interfaceClass, Class<?> implClass) {
		super.implClass(interfaceClass, implClass);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder implClasses(Map<String,Class<?>> values) {
		super.implClasses(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder locale(Locale value) {
		super.locale(value);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder mediaType(MediaType value) {
		super.mediaType(value);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder notBeanClasses(Class<?>...values) {
		super.notBeanClasses(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder notBeanClasses(Object...values) {
		super.notBeanClasses(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder notBeanClassesReplace(Class<?>...values) {
		super.notBeanClassesReplace(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder notBeanClassesReplace(Object...values) {
		super.notBeanClassesReplace(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder notBeanClassesRemove(Class<?>...values) {
		super.notBeanClassesRemove(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder notBeanClassesRemove(Object...values) {
		super.notBeanClassesRemove(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder notBeanPackages(Object...values) {
		super.notBeanPackages(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder notBeanPackages(String...values) {
		super.notBeanPackages(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder notBeanPackagesReplace(String...values) {
		super.notBeanPackagesReplace(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder notBeanPackagesReplace(Object...values) {
		super.notBeanPackagesReplace(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder notBeanPackagesRemove(String...values) {
		super.notBeanPackagesRemove(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder notBeanPackagesRemove(Object...values) {
		super.notBeanPackagesRemove(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder pojoSwaps(Class<?>...values) {
		super.pojoSwaps(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder pojoSwaps(Object...values) {
		super.pojoSwaps(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder pojoSwapsReplace(Class<?>...values) {
		super.pojoSwapsReplace(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder pojoSwapsReplace(Object...values) {
		super.pojoSwapsReplace(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder pojoSwapsRemove(Class<?>...values) {
		super.pojoSwapsRemove(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder pojoSwapsRemove(Object...values) {
		super.pojoSwapsRemove(values);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder sortProperties(boolean value) {
		super.sortProperties(value);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder sortProperties() {
		super.sortProperties();
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder timeZone(TimeZone value) {
		super.timeZone(value);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder useEnumNames(boolean value) {
		super.useEnumNames(value);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder useEnumNames() {
		super.useEnumNames();
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder useInterfaceProxies(boolean value) {
		super.useInterfaceProxies(value);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder useJavaBeanIntrospector(boolean value) {
		super.useJavaBeanIntrospector(value);
		return this;
	}

	@Override /* BeanContextBuilder */
	public BeanTraverseBuilder useJavaBeanIntrospector() {
		super.useJavaBeanIntrospector();
		return this;
	}

	@Override /* ContextBuilder */
	public BeanTraverseBuilder set(String name, Object value) {
		super.set(name, value);
		return this;
	}

	@Override /* ContextBuilder */
	public BeanTraverseBuilder set(Map<String,Object> properties) {
		super.set(properties);
		return this;
	}

	@Override /* ContextBuilder */
	public BeanTraverseBuilder add(Map<String,Object> properties) {
		super.add(properties);
		return this;
	}

	@Override /* ContextBuilder */
	public BeanTraverseBuilder addTo(String name, Object value) {
		super.addTo(name, value);
		return this;
	}

	@Override /* ContextBuilder */
	public BeanTraverseBuilder addTo(String name, String key, Object value) {
		super.addTo(name, key, value);
		return this;
	}

	@Override /* ContextBuilder */
	public BeanTraverseBuilder removeFrom(String name, Object value) {
		super.removeFrom(name, value);
		return this;
	}

	@Override /* ContextBuilder */
	public BeanTraverseBuilder apply(PropertyStore copyFrom) {
		super.apply(copyFrom);
		return this;
	}

	@Override /* ContextBuilder */
	public BeanTraverseBuilder applyAnnotations(AnnotationList al, VarResolverSession vrs) {
		super.applyAnnotations(al, vrs);
		return this;
	}

	@Override /* ContextBuilder */
	public BeanTraverseBuilder applyAnnotations(Class<?>...fromClasses) {
		super.applyAnnotations(fromClasses);
		return this;
	}

	@Override /* ContextBuilder */
	public BeanTraverseBuilder applyAnnotations(Method...fromMethods) {
		super.applyAnnotations(fromMethods);
		return this;
	}

	@Override /* Context */
	public BeanTraverseContext build() {
		return null;
	}
}