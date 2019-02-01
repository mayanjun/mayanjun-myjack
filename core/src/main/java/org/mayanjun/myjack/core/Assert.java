/*
 * Copyright 2016-2018 mayanjun.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mayanjun.myjack.core;

import java.util.Collection;
import java.util.Map;

/**
 * Assertion
 * 
 * @author mayanjun
 * @since 0.0.1
 */
public class Assert {
	private Assert() {
	}

	/**
	 *  Assert that an object is {@code null} .
	 * @param object object to be test
	 * @param code status
	 * @param message fail message
     * @param log fail log
     */
	public static void notNull(Object object, Status code, String message, String log) {
		if (object == null)
			throw new ServiceException(code, message, log);
	}

	public static void notNull(Object object, Status code, String message) {
		notNull(object, code, message, null);
	}

	public static void notNull(Object object, Status code) {
		notNull(object, code, null, null);
	}

	public static void notNull(Object object, String message) {
		notNull(object, Status.PARAM_MISS, message, null);
	}

	public static void notNull(Object object, String message, String log) {
		notNull(object, Status.PARAM_MISS, message, log);
	}

	public static void notNull(Object object) {
		notNull(object, Status.PARAM_MISS);
	}

	// /////////////////////////////////////////////////////////////////////////

	public static void isNull(Object object, Status code, String message) {
		isNull(object, code, message, null);
	}

	public static void isNull(Object object, Status code, String message, String log) {
		if (object != null)
			throw new ServiceException(code, message, log);
	}

	public static void isNull(Object object, Status code) {
		isNull(object, code, null, null);
	}

	public static void isNull(Object object, String message) {
		isNull(object, Status.PARAM_ERROR, message, null);
	}

	public static void isNull(Object object, String message, String log) {
		isNull(object, Status.PARAM_ERROR, message, log);
	}

	public static void isNull(Object object) {
		isNull(object, Status.PARAM_ERROR);
	}

	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Assert a boolean expression, throwing {@code ServiceException} if the
	 * test result is {@code false}.
	 * 
	 * <pre class="code">
	 * Assert.isTrue(i &gt; 0, code, &quot;The value must be greater than zero&quot;);
	 * </pre>
	 * 
	 * @param expression
	 *            a boolean expression
	 * @param code
	 *            a instance of {@linkplain Status} to use if the assertion
	 *            fails
	 * @param message
	 *            the exception message to use if the assertion fails
	 */
	public static void isTrue(boolean expression, Status code, String message) {
		isTrue(expression, code, message, null);
	}

	public static void isTrue(boolean expression, Status code, String message, String log) {
		Boolean exp = Boolean.valueOf(expression);
		if (!expression) {
			throw new ServiceException(code, message, log);
		}
	}

	public static void isTrue(boolean expression, Status code) {
		isTrue(expression, code, null, null);
	}

	public static void isTrue(boolean expression, String message) {
		isTrue(expression, Status.PARAM_ERROR, message, null);
	}

	public static void isTrue(boolean expression, String message, String log) {
		isTrue(expression, Status.PARAM_ERROR, message, log);
	}

	public static void isTrue(boolean expression) {
		isTrue(expression, Status.PARAM_ERROR);
	}

	// /////////////////////////////////////////////////////////////////////////

	public static void isFalse(boolean expression, Status code, String message) {
		isFalse(expression, code, message, null);
	}

	public static void isFalse(boolean expression, Status code, String message, String log) {
		if (expression) {
			throw new ServiceException(code, message, log);
		}
	}

	public static void isFalse(boolean expression, Status code) {
		isFalse(expression, code, null, null);
	}

	public static void isFalse(boolean expression, String message) {
		isFalse(expression, Status.PARAM_ERROR, message, null);
	}

	public static void isFalse(boolean expression, String message, String log) {
		isFalse(expression, Status.PARAM_ERROR, message, log);
	}

	public static void isFalse(boolean expression) {
		isFalse(expression, Status.PARAM_ERROR);
	}

	// /////////////////////////////////////////////////////////////////////////

	public static void notEmpty(Map<?, ?> coll, Status code, String message) {
		notEmpty(coll, code, message, null);
	}

	public static void notEmpty(Map<?, ?> coll, Status code, String message, String log) {
		if (coll == null || coll.size() == 0) {
			throw new ServiceException(code, message, log);
		}
	}

	public static void notEmpty(Map<?, ?> coll, String message) {
		notEmpty(coll, Status.PARAM_MISS, message, null);
	}

	public static void notEmpty(Map<?, ?> coll, Status code) {
		notEmpty(coll, code, null, null);
	}

	public static void notEmpty(Map<?, ?> coll, String message, String log) {
		notEmpty(coll, Status.PARAM_MISS, message, log);
	}

	public static void notEmpty(Map<?, ?> coll) {
		notEmpty(coll, Status.PARAM_MISS);
	}

	// /////////////////////////////////////////////////////////////////////////

	public static void notEmpty(Collection<?> coll, Status code) {
		notEmpty(coll, code, null, null);
	}

	public static void notEmpty(Collection<?> coll, Status code, String message) {
		notEmpty(coll, code, message, null);
	}

	public static void notEmpty(Collection<?> coll, Status code, String message, String log) {
		if (coll == null || coll.isEmpty()) {
			throw new ServiceException(code, message, log);
		}
	}

	public static void notEmpty(Collection<?> coll, String message) {
		notEmpty(coll, Status.PARAM_MISS, message, null);
	}

	public static void notEmpty(Collection<?> coll, String message, String log) {
		notEmpty(coll, Status.PARAM_MISS, message, log);
	}

	public static void notEmpty(Collection<?> coll) {
		notEmpty(coll, Status.PARAM_MISS);
	}

	// /////////////////////////////////////////////////////////////////////////

	public static void notEmpty(CharSequence src, Status code, String message) {
		notEmpty(src, code, message, null);
	}

	public static void notEmpty(CharSequence src, Status code, String message, String log) {
		if (src == null || src.length() == 0) {
			throw new ServiceException(code, message, log);
		}
	}

	public static void notEmpty(CharSequence src, Status code) {
		notEmpty(src, code, null, null);
	}

	public static void notEmpty(CharSequence src, String message) {
		notEmpty(src, Status.PARAM_MISS, message, null);
	}

	public static void notEmpty(CharSequence src, String message, String log) {
		notEmpty(src, Status.PARAM_MISS, message, log);
	}

	public static void notEmpty(CharSequence src) {
		notEmpty(src, Status.PARAM_MISS);
	}

	// /////////////////////////////////////////////////////////////////////////

	private static boolean isBlank(CharSequence str) {
		int strLen;
		if(str != null && (strLen = str.length()) != 0) {
			for(int i = 0; i < strLen; ++i) {
				if(!Character.isWhitespace(str.charAt(i))) {
					return false;
				}
			}
			return true;
		} else {
			return true;
		}
	}

	public static void notBlank(CharSequence src, Status code, String message) {
		notBlank(src, code, message, null);
	}

	public static void notBlank(CharSequence src, Status code, String message, String log) {
		if(isBlank(src)) throw new ServiceException(code, message, log);
	}

	public static void notBlank(CharSequence src, Status code) {
		notBlank(src, code, null, null);
	}

	public static void notBlank(CharSequence src, String message) {
		notBlank(src, Status.PARAM_MISS, message, null);
	}

	public static void notBlank(CharSequence src, String message, String log) {
		notBlank(src, Status.PARAM_MISS, message, log);
	}

	public static void notBlank(CharSequence src) {
		notBlank(src, Status.PARAM_MISS);
	}

	// /////////////////////////////////////////////////////////////////////////

	public static void greatThanZero(Number src, Status code, String message, String log) {
		if (src == null || src.longValue() <= 0) throw new ServiceException(code, message, log);
	}

	public static void greatThanZero(Number src, Status code, String message) {
		greatThanZero(src, code, message, null);
	}

	public static void greatThanZero(Number src, Status code) {
		greatThanZero(src, code, null);
	}

	public static void greatThanZero(Number src, String message) {
		greatThanZero(src, Status.PARAM_ERROR, message, null);
	}

	public static void greatThanZero(Number src, String message, String log) {
		greatThanZero(src, Status.PARAM_ERROR, message, log);
	}

	public static void greatThanZero(Number src) {
		greatThanZero(src, Status.PARAM_ERROR);
	}
}
