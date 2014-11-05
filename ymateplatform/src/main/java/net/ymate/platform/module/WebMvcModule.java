/*
 * Copyright 2007-2107 the original author or authors.
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
package net.ymate.platform.module;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.ymate.platform.base.AbstractModule;
import net.ymate.platform.commons.lang.BlurObject;
import net.ymate.platform.commons.util.ClassUtils;
import net.ymate.platform.commons.util.ResourceUtils;
import net.ymate.platform.commons.util.RuntimeUtils;
import net.ymate.platform.mvc.MVC;
import net.ymate.platform.mvc.filter.IFilter;
import net.ymate.platform.mvc.web.IWebErrorHandler;
import net.ymate.platform.mvc.web.IWebEventHandler;
import net.ymate.platform.mvc.web.IWebMultipartHandler;
import net.ymate.platform.mvc.web.WebMVC;
import net.ymate.platform.mvc.web.impl.WebMvcConfig;
import net.ymate.platform.plugin.IPluginExtraParser;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * WebMvcModule
 * </p>
 * <p>
 * WebMvc模块加载器接口实现类；
 * </p>
 * 
 * @author 刘镇(suninformation@163.com)
 * @version 0.0.0
 *          <table style="border:1px solid gray;">
 *          <tr>
 *          <th width="100px">版本号</th><th width="100px">动作</th><th
 *          width="100px">修改人</th><th width="100px">修改时间</th>
 *          </tr>
 *          <!-- 以 Table 方式书写修改历史 -->
 *          <tr>
 *          <td>0.0.0</td>
 *          <td>创建类</td>
 *          <td>刘镇</td>
 *          <td>2012-12-23下午7:05:33</td>
 *          </tr>
 *          </table>
 */
public class WebMvcModule extends AbstractModule {

	/* (non-Javadoc)
	 * @see net.ymate.platform.module.base.AbstractModule#initialize(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public void initialize(Map<String, String> moduleCfgs) throws Exception {
		IWebEventHandler _eventHandler = ClassUtils.impl(moduleCfgs.get("base.event_handler_class"), IWebEventHandler.class, WebMvcModule.class);
		IPluginExtraParser _extraParser = ClassUtils.impl(moduleCfgs.get("base.plugin_extra_parser_class"), IPluginExtraParser.class, WebMvcModule.class);
		IWebErrorHandler _errorHandler = ClassUtils.impl(moduleCfgs.get("base.error_handler_class"), IWebErrorHandler.class, WebMvcModule.class);
		IWebMultipartHandler _multipartHandler = ClassUtils.impl(moduleCfgs.get("base.multipart_handler_class"), IWebMultipartHandler.class, WebMvcModule.class);
		//
		Locale _locale = MVC.localeFromStr(moduleCfgs.get("base.locale"), null);
		boolean _i18n = new BlurObject(StringUtils.defaultIfEmpty(moduleCfgs.get("base.i18n"), "false")).toBooleanValue();
		String _charsetEncoding = StringUtils.defaultIfEmpty(moduleCfgs.get("base.charset_encoding"), "UTF-8");
		//
		List<Class<IFilter>> _extraFilters = new ArrayList<Class<IFilter>>();
		for (String _extraFilter : StringUtils.split(StringUtils.trimToEmpty(moduleCfgs.get("base.extra_filters")), "|")) {
			Class<?> _filterClass = ResourceUtils.loadClass(_extraFilter, WebMvcModule.class);
			if (_filterClass != null && ClassUtils.isInterfaceOf(_filterClass, IFilter.class)) {
				_extraFilters.add((Class<IFilter>) _filterClass);
			}
		}
		//
		Map<String, String> _extendParams = new HashMap<String, String>();
		for (String _cfgKey : moduleCfgs.keySet()) {
			if (_cfgKey.startsWith("params")) {
				_extendParams.put(StringUtils.substring(_cfgKey, 7), moduleCfgs.get(_cfgKey));
			}
		}
		//
		String _pluginHome = moduleCfgs.get("base.plugin_home");
		if (StringUtils.isNotBlank(_pluginHome)) {
			if (_pluginHome.startsWith("/WEB-INF/")) {
				File _pluginHomeFile = new File(RuntimeUtils.getRootPath(), StringUtils.substringAfter(_pluginHome, "/WEB-INF/"));
				if (_pluginHomeFile.exists() && _pluginHomeFile.isDirectory()) {
					_pluginHome = _pluginHomeFile.getPath();
				}
			} else if (_pluginHome.contains("${user.dir}")) {
				_pluginHome = doParseVariableUserDir(_pluginHome);
			}
		}
		//
		WebMvcConfig _config = new WebMvcConfig(_eventHandler, _extraParser, _errorHandler, _locale, _i18n, _charsetEncoding, _pluginHome,  _extendParams, StringUtils.split(moduleCfgs.get("base.controller_packages"), '|'));
		//
		_config.setMultipartHandlerClassImpl(_multipartHandler);
		_config.setRestfulModel(new BlurObject(StringUtils.defaultIfEmpty(moduleCfgs.get("base.restful_model"), "false")).toBooleanValue());
		_config.setConventionModel(new BlurObject(StringUtils.defaultIfEmpty(moduleCfgs.get("base.convention_model"), "true")).toBooleanValue());
        _config.setConventionUrlrewrite(new BlurObject(StringUtils.defaultIfEmpty(moduleCfgs.get("base.convention_urlrewrite"), "false")).toBooleanValue());
		_config.setUrlSuffix(StringUtils.defaultIfEmpty(moduleCfgs.get("base.url_suffix"), ""));
		_config.setViewPath(StringUtils.defaultIfEmpty(moduleCfgs.get("base.view_path"), ""));
		_config.setExtraFilters(_extraFilters);
		_config.setUploadTempDir(StringUtils.defaultIfEmpty(moduleCfgs.get("upload.temp_dir"), System.getProperty("java.io.tmpdir")));
		_config.setUploadFileSizeMax(new BlurObject(StringUtils.defaultIfEmpty(moduleCfgs.get("upload.file_size_max"), "-1")).toIntValue());
		_config.setUploadTotalSizeMax(new BlurObject(StringUtils.defaultIfEmpty(moduleCfgs.get("upload.total_size_max"), "-1")).toIntValue());
		_config.setUploadSizeThreshold(new BlurObject(StringUtils.defaultIfEmpty(moduleCfgs.get("upload.size_threshold"), "10240")).toIntValue());
		//
		_config.setCookiePrefix(StringUtils.defaultIfEmpty(moduleCfgs.get("cookie.prefix"), ""));
		_config.setCookieDomain(StringUtils.defaultIfEmpty(moduleCfgs.get("cookie.domain"), ""));
		_config.setCookiePath(StringUtils.defaultIfEmpty(moduleCfgs.get("cookie.path"), "/"));
		_config.setCookieAuthKey(StringUtils.defaultIfEmpty(moduleCfgs.get("cookie.auth_key"), ""));
		//
		WebMVC.initialize(_config);
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.module.base.AbstractModule#destroy()
	 */
	public void destroy() throws Exception {
		WebMVC.destory();
	}

}
