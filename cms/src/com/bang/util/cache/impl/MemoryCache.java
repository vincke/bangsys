/**
 * Copyright 2015-2025 FLY的狐狸(email:bang@sina.com qq:369191470).
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
 *
 */

package com.bang.util.cache.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.bang.util.cache.Cache;

/**
 * 内存
 *
 * 2015年4月26日 下午8:24:11
 */
public class MemoryCache implements Cache {

	protected String name;
	protected Map<String, Object> map = new ConcurrentHashMap<String, Object>();

	public MemoryCache() {
	}

	public String name() {
		return name;
	}

	public MemoryCache name(String name) {
		this.name = name;
		return this;
	}

	public MemoryCache add(String key, Object value) {
		map.put(key, value);
		return this;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		return (T) map.get(key);
	}

	public Object remove(String key) {
		return map.remove(key);
	}

	public void clear() {
		map.clear();
	}

	public int size() {
		return map.size();
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> list() {
		if (map.size() == 0) {
			return null;
		}

		List<T> list = new ArrayList<T>();
		for (Object obj : map.values()) {
			list.add((T) obj);
		}
		return list;
	}

}
