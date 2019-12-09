package com.search.engine.util;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtil {

	private static Logger log = Logger.getLogger(JsonUtil.class);

	public static ObjectMapper mapper = configMapper();


	public JsonUtil() {
	}

	/**
	 * 配置ObjectMapper
	 * @param mapper
	 * @return
	 */
	public static ObjectMapper configMapper() {
		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, false);
		//处理空bean
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		//解析器支持解析单引号
		objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		//解析器支持解析结束符
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

		//对象中的空字段就不转换
		//		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		return objectMapper;
	}
	/**
	 * 将字符串转换成json结构
	 * @param json
	 * @param nodeName
	 * @return
	 * @throws Exception
	 */
	public static JsonNode getJsonNode(String json){
		JsonNode jn=null;
		if(json==null||"".equals(json)){
			return jn;
		}
		try {
			jn=mapper.readTree(json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jn;
	}
	/**
	 * 从json中获取某个节点的值
	 * @param json
	 * @param nodeName
	 * @return
	 * @throws Exception
	 */
	public static JsonNode getJsonNode(String json, String nodeName) {
		JsonNode jn = null;
		JsonNode node = null;

		if (json == null && json == "") {
			return node;
		}

		try {
			jn = mapper.readTree(json);
			node = jn.findValue(nodeName);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return node;
	}
	
	/**
	 * 从json中按照路径获取某个节点的值
	 * @param json
	 * @param path
	 */
	public static JsonNode getJsonNodeByPath(String json, String path) {
		JsonNode jn = null;
		JsonNode node = null;
		String[] nodeNames = null;
		
		if (json == null && json == "") {
			return node;
		} else {
			nodeNames = path.split("\\.");
			
			for (String nodeName : nodeNames) {
				try {
					if (StringUtils.contains(nodeName, "[") && StringUtils.contains(nodeName, "]")) {
						//数组元素
						jn = mapper.readTree(json);
						
						if(jn.isArray()) {
							String index = StringUtils.substring(nodeName, StringUtils.indexOf(nodeName, "[")+1, StringUtils.indexOf(nodeName, "]"));
							node = jn.get(Integer.valueOf(index));
							if(node != null) {
								json = node.toString();
							} else {
								break;
							}
						}
					} else {
						//对象元素
						jn = mapper.readTree(json);
						node = jn.findValue(nodeName);
						if(node != null) {
							json = node.toString();
						} else {
							break;
						}
					}
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return node;
	}

	/**
	 * 从json中获取某个节点的值
	 * @param json
	 * @param nodeName
	 * @return String
	 */
	public static String getJsonNodeValue(String json, String nodeName) {
		JsonNode jn = null;
		JsonNode node = null;

		if (json == null && json == "") {
			log.debug("输入的json为空");
		} else {
			try {
				jn = mapper.readTree(json);
				node = jn.findValue(nodeName);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(node != null && !node.isNull()) {
				if (node.isTextual()) {
					return node.asText();
				} else {
					return node.toString();
				}
			}
		}
		
		return null;
	}
	/**
	 * 从json中获取某个节点的值,并将其转成特定对象
	 * @param <T>
	 * @param json
	 * @param nodeName
	 * @param objectClass
	 * @return
	 */
	public static <T> T getJsonNodeToObj(String json, String nodeName, Class<T> objectClass) {
		String jsonNodeValue = getJsonNodeValue(json, nodeName);
		T jsonToObj = jsonToObj(jsonNodeValue, objectClass);
		return jsonToObj;
	}
	/**
	 * 从json中按路径获取某个节点的值
	 * @param json
	 * @param path
	 */
	public static String getJsonNodeValueByPath(String json, String path) {
		JsonNode jn = null;
		JsonNode node = null;
		String[] nodeNames = null;

		if (json == null && json == "") {
			log.debug("输入的json为空");
		} else {
			nodeNames = path.split("\\.");
			
			for (String nodeName : nodeNames) {
				try {
					if (StringUtils.contains(nodeName, "[") && StringUtils.contains(nodeName, "]")) {
						//数组元素
						jn = mapper.readTree(json);
						
						if(jn.isArray()) {
							String index = StringUtils.substring(nodeName, StringUtils.indexOf(nodeName, "[")+1, StringUtils.indexOf(nodeName, "]"));
							node = jn.get(Integer.valueOf(index));
							json = node.toString();
						}
					} else {
						//对象元素
						jn = mapper.readTree(json);
						node = jn.findValue(nodeName);
						json = node.toString();
					}
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if(node != null && !node.isNull()) {
				if (node.isTextual()) {
					return node.asText();
				} else {
					return node.toString();
				}
			}
		}
		
		return null;
	}

	/**
	 * 对象转json字符串
	 * @param obj
	 */
	public static String objToJson(Object obj) {
		if (isNull(obj)) {
			return null;
		}

		String json = null;
		try {
			json = mapper.writeValueAsString(obj);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return json;
	}

	/**
	 * json字符串转对象
	 * @param obj
	 */
	public static <T> T jsonToObj(String json, Class<T> objectClass) {
		if (isNull(json)) {
			return null;
		}

		T obj = null;
		try {
			obj = mapper.readValue(json, objectClass);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return obj;
	}

	/**
	 * json字符串转泛型对象
	 * @param obj
	 */
	public static <T, V> T jsonToGenericObj(String json, Class<T> ObjectClass, Class<V>... genericClasses) {
		if (isNull(json)) {
			return null;
		}

		T obj = null;
		JavaType javaType = getGenericType(ObjectClass, genericClasses); 
		try {
			obj = mapper.readValue(json, javaType);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return obj;
	}

	@SuppressWarnings("deprecation")
	public static JavaType getGenericType(Class<?> ObjectClass, Class<?>... genericClasses) {
		return mapper.getTypeFactory().constructParametricType(ObjectClass, genericClasses);
	}

	/**
	 * 对象转格式良好的json字符串
	 * @param obj
	 */
	public static String objToGoodJson(Object obj) {
		if (isNull(obj)) {
			return null;
		}

		String goodJson = null;
		try {
			goodJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
			//			goodJson = mapper.defaultPrettyPrintingWriter().writeValueAsString(obj);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return goodJson;
	}

	/**
	 * json字符串转格式良好的json字符串
	 * @param jsonStr
	 */
	@SuppressWarnings("unchecked")
	public static String jsonToGoodJson(String jsonStr) {
		if (isNull(jsonStr)) {
			return null;
		}

		String goodJson = null;
		try {
			Map obj = (Map) jsonToObj(jsonStr, Map.class);
			goodJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return goodJson;
	}
	
	/**
	 * 对象转map
	 * @param obj
	 */
	@SuppressWarnings("unchecked")
	public static Map objToMap(Object obj) {
		if (isNull(obj)) {
			return null;
		}

		Map map = null;
		try {
			map = (LinkedHashMap) mapper.readValue(objToJson(obj), LinkedHashMap.class);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return map;
	}

	/**
	 * json字符串转map
	 * @param json
	 */
	@SuppressWarnings("unchecked")
	public static Map jsonToMap(String json) {
		if (isNull(json)) {
			return null;
		}

		Map map = null;
		try {
			map = (LinkedHashMap) mapper.readValue(json, LinkedHashMap.class);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return map;
	}

	public static boolean isNull(Object obj) {
		if (obj == null) {
			return true;
		} else {
			return false;
		}
	}

}