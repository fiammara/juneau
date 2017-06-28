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
package org.apache.juneau.internal;

import java.io.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import org.apache.juneau.*;
import org.apache.juneau.utils.*;

/**
 * Class-related utility methods.
 */
public final class ClassUtils {

	/**
	 * Given the specified list of objects, return readable names for the class types of the objects.
	 *
	 * @param o The objects.
	 * @return An array of readable class type strings.
	 */
	public static ObjectList getReadableClassNames(Object[] o) {
		ObjectList l = new ObjectList();
		for (int i = 0; i < o.length; i++)
			l.add(o[i] == null ? "null" : getReadableClassName(o[i].getClass()));
		return l;
	}

	/**
	 * Shortcut for calling <code><jsm>getReadableClassName</jsm>(c.getName())</code>
	 *
	 * @param c The class.
	 * @return A readable class type name, or <jk>null</jk> if parameter is <jk>null</jk>.
	 */
	public static String getReadableClassName(Class<?> c) {
		if (c == null)
			return null;
		return getReadableClassName(c.getName());
	}

	/**
	 * Shortcut for calling <code><jsm>getReadableClassName</jsm>(c.getClass().getName())</code>
	 *
	 * @param o The object whose class we want to render.
	 * @return A readable class type name, or <jk>null</jk> if parameter is <jk>null</jk>.
	 */
	public static String getReadableClassNameForObject(Object o) {
		if (o == null)
			return null;
		return getReadableClassName(o.getClass().getName());
	}

	/**
	 * Converts the specified class name to a readable form when class name is a special construct like <js>"[[Z"</js>.
	 *
	 * <h5 class='section'>Example:</h5>
	 * <p class='bcode'>
	 * 	<jsm>getReadableClassName</jsm>(<js>"java.lang.Object"</js>);  <jc>// Returns "java.lang.Object"</jc>
	 * 	<jsm>getReadableClassName</jsm>(<js>"boolean"</js>);  <jc>// Returns "boolean"</jc>
	 * 	<jsm>getReadableClassName</jsm>(<js>"[Z"</js>);  <jc>// Returns "boolean[]"</jc>
	 * 	<jsm>getReadableClassName</jsm>(<js>"[[Z"</js>);  <jc>// Returns "boolean[][]"</jc>
	 * 	<jsm>getReadableClassName</jsm>(<js>"[Ljava.lang.Object;"</js>);  <jc>// Returns "java.lang.Object[]"</jc>
	 * 	<jsm>getReadableClassName</jsm>(<jk>null</jk>);  <jc>// Returns null</jc>
	 * </p>
	 *
	 * @param className The class name.
	 * @return A readable class type name, or <jk>null</jk> if parameter is <jk>null</jk>.
	 */
	public static String getReadableClassName(String className) {
		if (className == null)
			return null;
		if (! StringUtils.startsWith(className, '['))
			return className;
		int depth = 0;
		for (int i = 0; i < className.length(); i++) {
			if (className.charAt(i) == '[')
				depth++;
			else
				break;
		}
		char type = className.charAt(depth);
		String c;
		switch (type) {
			case 'Z': c = "boolean"; break;
			case 'B': c = "byte"; break;
			case 'C': c = "char"; break;
			case 'D': c = "double"; break;
			case 'F': c = "float"; break;
			case 'I': c = "int"; break;
			case 'J': c = "long"; break;
			case 'S': c = "short"; break;
			default: c = className.substring(depth+1, className.length()-1);
		}
		StringBuilder sb = new StringBuilder(c.length() + 2*depth).append(c);
		for (int i = 0; i < depth; i++)
			sb.append("[]");
		return sb.toString();
	}

	/**
	 * Converts the string generated by {@link #getReadableClassName(Class)} back into a {@link Class}.
	 *
	 * <p>
	 * Generics are stripped from the string since they cannot be converted to a class.
	 *
	 * @param cl The classloader to use to load the class.
	 * @param name The readable class name.
	 * @return The class object.
	 * @throws ClassNotFoundException
	 */
	public static Class<?> getClassFromReadableName(ClassLoader cl, String name) throws ClassNotFoundException {
		return cl.loadClass(name);
	}

	/**
	 * Returns <jk>true</jk> if <code>parent</code> is a parent class of <code>child</code>.
	 *
	 * @param parent The parent class.
	 * @param child The child class.
	 * @param strict If <jk>true</jk> returns <jk>false</jk> if the classes are the same.
	 * @return <jk>true</jk> if <code>parent</code> is a parent class of <code>child</code>.
	 */
	public static boolean isParentClass(Class<?> parent, Class<?> child, boolean strict) {
		return parent.isAssignableFrom(child) && ((!strict) || ! parent.equals(child));
	}

	/**
	 * Returns <jk>true</jk> if <code>parent</code> is a parent class or the same as <code>child</code>.
	 *
	 * @param parent The parent class.
	 * @param child The child class.
	 * @return <jk>true</jk> if <code>parent</code> is a parent class or the same as <code>child</code>.
	 */
	public static boolean isParentClass(Class<?> parent, Class<?> child) {
		return isParentClass(parent, child, false);
	}

	/**
	 * Returns <jk>true</jk> if <code>parent</code> is a parent class or the same as <code>child</code>.
	 *
	 * @param parent The parent class.
	 * @param child The child class.
	 * @return <jk>true</jk> if <code>parent</code> is a parent class or the same as <code>child</code>.
	 */
	public static boolean isParentClass(Class<?> parent, Type child) {
		if (child instanceof Class)
			return isParentClass(parent, (Class<?>)child);
		return false;
	}

	/**
	 * Comparator for use with {@link TreeMap TreeMaps} with {@link Class} keys.
	 */
	public final static class ClassComparator implements Comparator<Class<?>>, Serializable {

		private static final long serialVersionUID = 1L;

		@Override /* Comparator */
		public int compare(Class<?> object1, Class<?> object2) {
			return object1.getName().compareTo(object2.getName());
		}
	}

	/**
	 * Returns the signature of the specified method.
	 *
	 * <p>
	 * For no-arg methods, the signature will be a simple string such as <js>"toString"</js>.
	 * For methods with one or more args, the arguments will be fully-qualified class names (e.g.
	 * <js>"append(java.util.StringBuilder,boolean)"</js>)
	 *
	 * @param m The methods to get the signature on.
	 * @return The methods signature.
	 */
	public static String getMethodSignature(Method m) {
		StringBuilder sb = new StringBuilder(m.getName());
		Class<?>[] pt = m.getParameterTypes();
		if (pt.length > 0) {
			sb.append('(');
			for (int i = 0; i < pt.length; i++) {
				if (i > 0)
					sb.append(',');
				sb.append(getReadableClassName(pt[i]));
			}
			sb.append(')');
		}
		return sb.toString();
	}

	private final static Map<Class<?>, Class<?>>
		pmap1 = new HashMap<Class<?>, Class<?>>(),
		pmap2 = new HashMap<Class<?>, Class<?>>();
	static {
		pmap1.put(boolean.class, Boolean.class);
		pmap1.put(byte.class, Byte.class);
		pmap1.put(short.class, Short.class);
		pmap1.put(char.class, Character.class);
		pmap1.put(int.class, Integer.class);
		pmap1.put(long.class, Long.class);
		pmap1.put(float.class, Float.class);
		pmap1.put(double.class, Double.class);
		pmap2.put(Boolean.class, boolean.class);
		pmap2.put(Byte.class, byte.class);
		pmap2.put(Short.class, short.class);
		pmap2.put(Character.class, char.class);
		pmap2.put(Integer.class, int.class);
		pmap2.put(Long.class, long.class);
		pmap2.put(Float.class, float.class);
		pmap2.put(Double.class, double.class);
	}

	/**
	 * If the specified class is a primitive (e.g. <code><jk>int</jk>.<jk>class</jk></code>) returns it's wrapper class
	 * (e.g. <code>Integer.<jk>class</jk></code>).
	 *
	 * @param c The class.
	 * @return The wrapper class, or <jk>null</jk> if class is not a primitive.
	 */
	public static Class<?> getPrimitiveWrapper(Class<?> c) {
		return pmap1.get(c);
	}

	/**
	 * If the specified class is a primitive wrapper (e.g. <code><jk>Integer</jk>.<jk>class</jk></code>) returns it's
	 * primitive class (e.g. <code>int.<jk>class</jk></code>).
	 *
	 * @param c The class.
	 * @return The primitive class, or <jk>null</jk> if class is not a primitive wrapper.
	 */
	public static Class<?> getPrimitiveForWrapper(Class<?> c) {
		return pmap2.get(c);
	}

	/**
	 * If the specified class is a primitive (e.g. <code><jk>int</jk>.<jk>class</jk></code>) returns it's wrapper class
	 * (e.g. <code>Integer.<jk>class</jk></code>).
	 *
	 * @param c The class.
	 * @return The wrapper class if it's primitive, or the same class if class is not a primitive.
	 */
	public static Class<?> getWrapperIfPrimitive(Class<?> c) {
		if (! c.isPrimitive())
			return c;
		return pmap1.get(c);
	}

	/**
	 * Returns <jk>true</jk> if the specified class has the {@link Deprecated @Deprecated} annotation on it.
	 *
	 * @param c The class.
	 * @return <jk>true</jk> if the specified class has the {@link Deprecated @Deprecated} annotation on it.
	 */
	public static boolean isNotDeprecated(Class<?> c) {
		return ! c.isAnnotationPresent(Deprecated.class);
	}

	/**
	 * Returns <jk>true</jk> if the specified method has the {@link Deprecated @Deprecated} annotation on it.
	 *
	 * @param m The method.
	 * @return <jk>true</jk> if the specified method has the {@link Deprecated @Deprecated} annotation on it.
	 */
	public static boolean isNotDeprecated(Method m) {
		return ! m.isAnnotationPresent(Deprecated.class);

	}

	/**
	 * Returns <jk>true</jk> if the specified constructor has the {@link Deprecated @Deprecated} annotation on it.
	 *
	 * @param c The constructor.
	 * @return <jk>true</jk> if the specified constructor has the {@link Deprecated @Deprecated} annotation on it.
	 */
	public static boolean isNotDeprecated(Constructor<?> c) {
		return ! c.isAnnotationPresent(Deprecated.class);
	}

	/**
	 * Returns <jk>true</jk> if the specified class is public.
	 *
	 * @param c The class.
	 * @return <jk>true</jk> if the specified class is public.
	 */
	public static boolean isPublic(Class<?> c) {
		return Modifier.isPublic(c.getModifiers());
	}

	/**
	 * Returns <jk>true</jk> if the specified class is public.
	 *
	 * @param c The class.
	 * @return <jk>true</jk> if the specified class is public.
	 */
	public static boolean isStatic(Class<?> c) {
		return Modifier.isStatic(c.getModifiers());
	}

	/**
	 * Returns <jk>true</jk> if the specified class is abstract.
	 *
	 * @param c The class.
	 * @return <jk>true</jk> if the specified class is abstract.
	 */
	public static boolean isAbstract(Class<?> c) {
		return Modifier.isAbstract(c.getModifiers());
	}

	/**
	 * Returns <jk>true</jk> if the specified method is public.
	 *
	 * @param m The method.
	 * @return <jk>true</jk> if the specified method is public.
	 */
	public static boolean isPublic(Method m) {
		return Modifier.isPublic(m.getModifiers());
	}

	/**
	 * Returns <jk>true</jk> if the specified method is static.
	 *
	 * @param m The method.
	 * @return <jk>true</jk> if the specified method is static.
	 */
	public static boolean isStatic(Method m) {
		return Modifier.isStatic(m.getModifiers());
	}

	/**
	 * Returns <jk>true</jk> if the specified constructor is public.
	 *
	 * @param c The constructor.
	 * @return <jk>true</jk> if the specified constructor is public.
	 */
	public static boolean isPublic(Constructor<?> c) {
		return Modifier.isPublic(c.getModifiers());
	}

	/**
	 * Returns the specified annotation on the specified method.
	 *
	 * <p>
	 * Similar to {@link Method#getAnnotation(Class)}, but searches up the parent hierarchy for the annotation
	 * defined on parent classes and interfaces.
	 *
	 * <p>
	 * Normally, annotations defined on methods of parent classes and interfaces are not inherited by the child methods.
	 * This utility method gets around that limitation by searching the class hierarchy for the "same" method
	 * (i.e. the same name and arguments).
	 *
	 * @param a The annotation to search for.
	 * @param m The method to search.
	 * @return The annotation, or <jk>null</jk> if it wasn't found.
	 */
	public static <T extends Annotation> T getMethodAnnotation(Class<T> a, Method m) {
		return getMethodAnnotation(a, m.getDeclaringClass(), m);
	}

	/**
	 * Returns the specified annotation on the specified method.
	 *
	 * <p>
	 * Similar to {@link Method#getAnnotation(Class)}, but searches up the parent hierarchy for the annotation defined
	 * on parent classes and interfaces.
	 *
	 * <p>
	 * Normally, annotations defined on methods of parent classes and interfaces are not inherited by the child methods.
	 * This utility method gets around that limitation by searching the class hierarchy for the "same" method
	 * (i.e. the same name and arguments).
	 *
	 * @param a The annotation to search for.
	 * @param c
	 * 	The child class to start searching from.
	 * 	Note that it can be a descendant class of the actual declaring class of the method passed in.
	 * 	This allows you to find annotations on methods overridden by the method passed in.
	 * @param method The method to search.
	 * @return The annotation, or <jk>null</jk> if it wasn't found.
	 */
	public static <T extends Annotation> T getMethodAnnotation(Class<T> a, Class<?> c, Method method) {
		for (Method m : c.getDeclaredMethods()) {
			if (isSameMethod(method, m)) {
				T t = m.getAnnotation(a);
				if (t != null)
					return t;
			}
		}
		Class<?> pc = c.getSuperclass();
		if (pc != null) {
			T t = getMethodAnnotation(a, pc, method);
			if (t != null)
				return t;
		}
		for (Class<?> ic : c.getInterfaces()) {
			T t = getMethodAnnotation(a, ic, method);
			if (t != null)
				return t;
		}
		return null;
	}

	private static boolean isSameMethod(Method m1, Method m2) {
		return m1.getName().equals(m2.getName()) && Arrays.equals(m1.getParameterTypes(), m2.getParameterTypes());
	}

	/**
	 * Locates the no-arg constructor for the specified class.
	 *
	 * <p>
	 * Constructor must match the visibility requirements specified by parameter 'v'.
	 * If class is abstract, always returns <jk>null</jk>.
	 * Note that this also returns the 1-arg constructor for non-static member classes.
	 *
	 * @param c The class from which to locate the no-arg constructor.
	 * @param v The minimum visibility.
	 * @return The constructor, or <jk>null</jk> if no no-arg constructor exists with the required visibility.
	 */
	@SuppressWarnings({"rawtypes","unchecked"})
	public static final <T> Constructor<T> findNoArgConstructor(Class<T> c, Visibility v) {
		int mod = c.getModifiers();
		if (Modifier.isAbstract(mod))
			return null;
		boolean isMemberClass = c.isMemberClass() && ! isStatic(c);
		for (Constructor cc : c.getConstructors()) {
			mod = cc.getModifiers();
			if (cc.getParameterTypes().length == (isMemberClass ? 1 : 0) && v.isVisible(mod) && isNotDeprecated(cc))
				return v.transform(cc);
		}
		return null;
	}

	/**
	 * Finds the real parameter type of the specified class.
	 *
	 * @param c The class containing the parameters (e.g. PojoSwap&lt;T,S&gt;)
	 * @param index The zero-based index of the parameter to resolve.
	 * @param oc The class we're trying to resolve the parameter type for.
	 * @return The resolved real class.
	 */
	public static Class<?> resolveParameterType(Class<?> c, int index, Class<?> oc) {

		// We need to make up a mapping of type names.
		Map<Type,Type> typeMap = new HashMap<Type,Type>();
		while (c != oc.getSuperclass()) {
			extractTypes(typeMap, oc);
			oc = oc.getSuperclass();
		}

		ParameterizedType opt = (ParameterizedType)oc.getGenericSuperclass();
		Type actualType = opt.getActualTypeArguments()[index];

		if (typeMap.containsKey(actualType))
			actualType = typeMap.get(actualType);

		if (actualType instanceof Class) {
			return (Class<?>)actualType;

		} else if (actualType instanceof GenericArrayType) {
			Class<?> cmpntType = (Class<?>)((GenericArrayType)actualType).getGenericComponentType();
			return Array.newInstance(cmpntType, 0).getClass();

		} else if (actualType instanceof TypeVariable) {
			TypeVariable<?> typeVariable = (TypeVariable<?>)actualType;
			List<Class<?>> nestedOuterTypes = new LinkedList<Class<?>>();
			for (Class<?> ec = oc.getEnclosingClass(); ec != null; ec = ec.getEnclosingClass()) {
				try {
					Class<?> outerClass = oc.getClass();
					nestedOuterTypes.add(outerClass);
					Map<Type,Type> outerTypeMap = new HashMap<Type,Type>();
					extractTypes(outerTypeMap, outerClass);
					for (Map.Entry<Type,Type> entry : outerTypeMap.entrySet()) {
						Type key = entry.getKey(), value = entry.getValue();
						if (key instanceof TypeVariable) {
							TypeVariable<?> keyType = (TypeVariable<?>)key;
							if (keyType.getName().equals(typeVariable.getName()) && isInnerClass(keyType.getGenericDeclaration(), typeVariable.getGenericDeclaration())) {
								if (value instanceof Class)
									return (Class<?>)value;
								typeVariable = (TypeVariable<?>)entry.getValue();
							}
						}
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			throw new RuntimeException("Could not resolve type: " + actualType);
		} else {
			throw new RuntimeException("Invalid type found in resolveParameterType: " + actualType);
		}
	}

	private static boolean isInnerClass(GenericDeclaration od, GenericDeclaration id) {
		if (od instanceof Class && id instanceof Class) {
			Class<?> oc = (Class<?>)od;
			Class<?> ic = (Class<?>)id;
			while ((ic = ic.getEnclosingClass()) != null)
				if (ic == oc)
					return true;
		}
		return false;
	}

	private static void extractTypes(Map<Type,Type> typeMap, Class<?> c) {
		Type gs = c.getGenericSuperclass();
		if (gs instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType)gs;
			Type[] typeParameters = ((Class<?>)pt.getRawType()).getTypeParameters();
			Type[] actualTypeArguments = pt.getActualTypeArguments();
			for (int i = 0; i < typeParameters.length; i++) {
				if (typeMap.containsKey(actualTypeArguments[i]))
					actualTypeArguments[i] = typeMap.get(actualTypeArguments[i]);
				typeMap.put(typeParameters[i], actualTypeArguments[i]);
			}
		}
	}

	/**
	 * Finds a public method with the specified parameters.
	 *
	 * @param c The class to look for the method.
	 * @param name The method name.
	 * @param returnType
	 * 	The return type of the method.
	 * 	Can be a super type of the actual return type.
	 * 	For example, if the actual return type is <code>CharSequence</code>, then <code>Object</code> will match but
	 * 	<code>String</code> will not.
	 * @param parameterTypes
	 * 	The parameter types of the method.
	 * 	Can be subtypes of the actual parameter types.
	 * 	For example, if the parameter type is <code>CharSequence</code>, then <code>String</code> will match but
	 * 	<code>Object</code> will not.
	 * @return The matched method, or <jk>null</jk> if no match was found.
	 */
	public static Method findPublicMethod(Class<?> c, String name, Class<?> returnType, Class<?>...parameterTypes) {
		for (Method m : c.getMethods()) {
			if (isPublic(m) && m.getName().equals(name)) {
				Class<?> rt = m.getReturnType();
				if (isParentClass(returnType, rt)) {
					Class<?>[] pt = m.getParameterTypes();
					if (pt.length == parameterTypes.length) {
						boolean matches = true;
						for (int i = 0; i < pt.length; i++) {
							if (! isParentClass(pt[i], parameterTypes[i])) {
								matches = false;
								break;
							}
						}
						if (matches)
							return m;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Finds a public constructor with the specified parameters without throwing an exception.
	 *
	 * @param c The class to search for a constructor.
	 * @param parameterTypes
	 * 	The parameter types in the constructor.
	 * 	Can be subtypes of the actual constructor argument types.
	 * @return The matching constructor, or <jk>null</jk> if constructor could not be found.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Constructor<T> findPublicConstructor(Class<T> c, Class<?>...parameterTypes) {
		for (Constructor<?> n : c.getConstructors()) {
			if (isPublic(n)) {
				Class<?>[] pt = n.getParameterTypes();
				if (pt.length == parameterTypes.length) {
					boolean matches = true;
					for (int i = 0; i < pt.length; i++) {
						if (! isParentClass(pt[i], parameterTypes[i])) {
							matches = false;
							break;
						}
					}
					if (matches)
						return (Constructor<T>)n;
				}
			}
		}
		return null;
	}

	/**
	 * Finds the public constructor that can take in the specified arguments.
	 *
	 * @param c The class we're trying to construct.
	 * @param args The arguments we want to pass into the constructor.
	 * @return
	 * 	The constructor, or <jk>null</jk> if a public constructor could not be found that takes in the specified
	 * 	arguments.
	 */
	public static <T> Constructor<T> findPublicConstructor(Class<T> c, Object...args) {
		return findPublicConstructor(c, getClasses(args));
	}

	/**
	 * Returns the class types for the specified arguments.
	 *
	 * @param args The objects we're getting the classes of.
	 * @return The classes of the arguments.
	 */
	public static Class<?>[] getClasses(Object...args) {
		Class<?>[] pt = new Class<?>[args.length];
		for (int i = 0; i < args.length; i++)
			pt[i] = args[i] == null ? null : args[i].getClass();
		return pt;
	}

// This code is inherently unsafe (but still potentially useful?)
//
//	/**
//	 * Converts class name strings to ClassMeta objects.
//	 *
//	 * <h5 class='section'>Example:</h5>
//	 * <ul>
//	 * 	<li><js>"java.lang.String"</js>
//	 * 	<li><js>"com.foo.sample.MyBean[]"</js>
//	 * 	<li><js>"java.util.HashMap<java.lang.String,java.lang.Integer>"</js>
//	 * 	<li><js>"[Ljava.lang.String;"</js> (i.e. the value of <code>String[].<jk>class</jk>.getName()</code>)
//	 * </ul>
//	 *
//	 * @param s The class name.
//	 * @return The ClassMeta corresponding to the class name string.
//	 */
//	protected final ClassMeta<?> getClassMetaFromString(String s) {
//		int d = 0;
//		if (s == null || s.isEmpty())
//			return null;
//
//		// Check for Class.getName() on array class types.
//		if (s.charAt(0) == '[') {
//			try {
//				return getClassMeta(findClass(s));
//			} catch (ClassNotFoundException e) {
//				throw new RuntimeException(e);
//			}
//		}
//
//		int i1 = 0;
//		int i2 = 0;
//		int dim = 0;
//		List<ClassMeta<?>> p = null;
//		for (int i = 0; i < s.length(); i++) {
//			char c = s.charAt(i);
//			if (c == '<') {
//				if (d == 0) {
//					i1 = i;
//					i2 = i+1;
//					p = new LinkedList<ClassMeta<?>>();
//				}
//				d++;
//			} else if (c == '>') {
//				d--;
//				if (d == 0 && p != null)
//					p.add(getClassMetaFromString(s.substring(i2, i)));
//			} else if (c == ',' && d == 1) {
//				if (p != null)
//					p.add(getClassMetaFromString(s.substring(i2, i)));
//				i2 = i+1;
//			}
//			if (c == '[') {
//				if (i1 == 0)
//					i1 = i;
//				dim++;
//			}
//		}
//		if (i1 == 0)
//			i1 = s.length();
//		try {
//			String name = s.substring(0, i1).trim();
//			char x = name.charAt(0);
//			Class<?> c = null;
//			if (x >= 'b' && x <= 's') {
//				if (x == 'b' && name.equals("boolean"))
//					c = boolean.class;
//				else if (x == 'b' && name.equals("byte"))
//					c = byte.class;
//				else if (x == 'c' && name.equals("char"))
//					c = char.class;
//				else if (x == 'd' && name.equals("double"))
//					c = double.class;
//				else if (x == 'i' && name.equals("int"))
//					c = int.class;
//				else if (x == 'l' && name.equals("long"))
//					c = long.class;
//				else if (x == 's' && name.equals("short"))
//					c = short.class;
//				else
//					c = findClass(name);
//			} else {
//				c = findClass(name);
//			}
//
//			ClassMeta<?> cm = getClassMeta(c);
//
//			if (p != null) {
//				if (cm.isMap())
//					cm = new ClassMeta(c, this).setKeyType(p.get(0)).setValueType(p.get(1));
//				if (cm.isCollection())
//					cm = new ClassMeta(c, this).setElementType(p.get(0));
//			}
//
//			while (dim > 0) {
//				cm = new ClassMeta(Array.newInstance(cm.getInnerClass(), 0).getClass(), this);
//				dim--;
//			}
//
//			return cm;
//		} catch (ClassNotFoundException e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	private Class<?> findClass(String name) throws ClassNotFoundException {
//		return classLoader == null ? Class.forName(name) : Class.forName(name, true, classLoader);
//	}

	/**
	 * Returns a {@link MethodInfo} bean that describes the specified method.
	 *
	 * @param m The method to describe.
	 * @return The bean with information about the method.
	 */
	public static MethodInfo getMethodInfo(Method m) {
		return new MethodInfo(m);
	}

	/**
	 * Returns {@link MethodInfo} beans that describe the specified methods.
	 *
	 * @param m The methods to describe.
	 * @return The beans with information about the methods.
	 */
	public static MethodInfo[] getMethodInfo(Collection<Method> m) {
		MethodInfo[] mi = new MethodInfo[m.size()];
		int i = 0;
		for (Method mm : m)
			mi[i++] = getMethodInfo(mm);
		return mi;
	}

	/**
	 * Simple bean that shows the name, parameter types, and return type of a method.
	 */
	@SuppressWarnings("javadoc")
	public static class MethodInfo {
		public final String methodName;
		public final String[] parameterTypes;
		public final String returnType;

		MethodInfo(Method m) {
			methodName = m.getName();
			Type[] pt = m.getGenericParameterTypes();
			parameterTypes = new String[pt.length];
			for (int i  = 0; i < pt.length; i++)
				parameterTypes[i] = BeanContext.DEFAULT.getClassMeta(pt[i]).toString();
			returnType = BeanContext.DEFAULT.getClassMeta(m.getGenericReturnType()).toString();
		}
	}

	/**
	 * Creates an instance of the specified class without throwing exceptions.
	 *
	 * @param c The class to cast to.
	 * @param c2
	 * 	The class to instantiate.
	 * 	Can also be an instance of the class.
	 * @param args The arguments to pass to the constructor.
	 * @return The new class instance, or <jk>null</jk> if the class was <jk>null</jk> or is abstract or an interface.
	 * @throws RuntimeException if constructor could not be found or called.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(Class<T> c, Object c2, Object...args) {
		if (c2 == null)
			return null;
		if (c2 instanceof Class) {
			try {
				Class<?> c3 = (Class<?>)c2;
				if (c3.isInterface() || isAbstract(c3))
					return null;
				if (args.length == 0)
					return (T)c3.newInstance();
				return (T)c3.getConstructor(getClasses(args)).newInstance(args);
			} catch (Exception e) {
				throw new FormattedRuntimeException(e, "Could not instantiate class {0}", c.getName());
			}
		} else if (isParentClass(c, c2.getClass())) {
			return (T)c2;
		} else {
			throw new FormattedRuntimeException("Object of type {0} found but was expecting {1}.", c2.getClass(), c.getClass());
		}
	}

	/**
	 * Returns all the fields in the specified class and all parent classes.
	 *
	 * @param c The class to get all fields on.
	 * @param parentFirst Order them in parent-class-to-child-class order, otherwise child-class-to-parent-class order.
	 * @return An iterable of all fields in the specified class.
	 */
	@SuppressWarnings("rawtypes")
	public static Iterable<Field> getAllFields(final Class c, final boolean parentFirst) {
		return new Iterable<Field>() {
			@Override
			public Iterator<Field> iterator() {
				return new Iterator<Field>(){
					final Iterator<Class<?>> classIterator = getParentClasses(c, parentFirst, false);
					Field[] fields = classIterator.hasNext() ? classIterator.next().getDeclaredFields() : new Field[0];
					int fIndex = 0;
					Field next;

					@Override
					public boolean hasNext() {
						prime();
						return next != null;
					}

					private void prime() {
						if (next == null) {
							while (fIndex >= fields.length) {
								if (classIterator.hasNext()) {
									fields = classIterator.next().getDeclaredFields();
									fIndex = 0;
								} else {
									fIndex = -1;
								}
			 				}
							if (fIndex != -1)
								next = fields[fIndex++];
						}
					}

					@Override
					public Field next() {
						prime();
						Field f = next;
						next = null;
						return f;
					}

					@Override
					public void remove() {
					}
				};
			}
		};
	}

	/**
	 * Returns all the methods in the specified class and all parent classes.
	 *
	 * @param c The class to get all methods on.
	 * @param parentFirst Order them in parent-class-to-child-class order, otherwise child-class-to-parent-class order.
	 * @return An iterable of all methods in the specified class.
	 */
	@SuppressWarnings("rawtypes")
	public static Iterable<Method> getAllMethods(final Class c, final boolean parentFirst) {
		return new Iterable<Method>() {
			@Override
			public Iterator<Method> iterator() {
				return new Iterator<Method>(){
					final Iterator<Class<?>> classIterator = getParentClasses(c, parentFirst, true);
					Method[] methods = classIterator.hasNext() ? classIterator.next().getDeclaredMethods() : new Method[0];
					int mIndex = 0;
					Method next;

					@Override
					public boolean hasNext() {
						prime();
						return next != null;
					}

					private void prime() {
						if (next == null) {
							while (mIndex >= methods.length) {
								if (classIterator.hasNext()) {
									methods = classIterator.next().getDeclaredMethods();
									mIndex = 0;
								} else {
									mIndex = -1;
								}
			 				}
							if (mIndex != -1)
								next = methods[mIndex++];
						}
					}

					@Override
					public Method next() {
						prime();
						Method m = next;
						next = null;
						return m;
					}

					@Override
					public void remove() {
					}
				};
			}
		};
	}

	/**
	 * Returns a list of all the parent classes of the specified class including the class itself.
	 *
	 * @param c The class to retrieve the parent classes.
	 * @param parentFirst In parent-to-child order, otherwise child-to-parent.
	 * @param includeInterfaces Include interfaces.
	 * @return An iterator of parent classes in the class hierarchy.
	 */
	public static Iterator<Class<?>> getParentClasses(final Class<?> c, boolean parentFirst, boolean includeInterfaces) {
		List<Class<?>> l = getParentClasses(new ArrayList<Class<?>>(), c, parentFirst, includeInterfaces);
		return l.iterator();
	}

	private static List<Class<?>> getParentClasses(List<Class<?>> l, Class<?> c, boolean parentFirst, boolean includeInterfaces) {
		if (parentFirst) {
			if (includeInterfaces)
				for (Class<?> i : c.getInterfaces())
					l.add(i);
			if (c.getSuperclass() != Object.class && c.getSuperclass() != null)
				getParentClasses(l, c.getSuperclass(), parentFirst, includeInterfaces);
			l.add(c);
		} else {
			l.add(c);
			if (c.getSuperclass() != Object.class && c.getSuperclass() != null)
				getParentClasses(l, c.getSuperclass(), parentFirst, includeInterfaces);
			if (includeInterfaces)
				for (Class<?> i : c.getInterfaces())
					l.add(i);
		}
		return l;
	}

	/**
	 * Returns the default value for the specified primitive class.
	 *
	 * @param primitiveClass The primitive class to get the default value for.
	 * @return The default value, or <jk>null</jk> if the specified class is not a primitive class.
	 */
	public static Object getPrimitiveDefault(Class<?> primitiveClass) {
		return primitiveDefaultMap.get(primitiveClass);
	}

	private static final Map<Class<?>,Object> primitiveDefaultMap = Collections.unmodifiableMap(
		new AMap<Class<?>,Object>()
			.append(Boolean.TYPE, false)
			.append(Character.TYPE, (char)0)
			.append(Short.TYPE, (short)0)
			.append(Integer.TYPE, 0)
			.append(Long.TYPE, 0l)
			.append(Float.TYPE, 0f)
			.append(Double.TYPE, 0d)
			.append(Byte.TYPE, (byte)0)
			.append(Boolean.class, false)
			.append(Character.class, (char)0)
			.append(Short.class, (short)0)
			.append(Integer.class, 0)
			.append(Long.class, 0l)
			.append(Float.class, 0f)
			.append(Double.class, 0d)
			.append(Byte.class, (byte)0)
	);
}
