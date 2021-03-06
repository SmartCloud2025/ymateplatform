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
package net.ymate.platform.mvc.web.view;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import net.ymate.platform.mvc.view.AbstractView;
import net.ymate.platform.mvc.web.WebMVC;
import net.ymate.platform.mvc.web.context.WebContext;
import net.ymate.platform.mvc.web.support.TemplateHelper;

/**
 * <p>
 * AbstractWebView
 * </p>
 * <p>
 * 基于Web应用的MVC视图接口抽象实现类；
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
 *          <td>2012-12-20下午7:06:25</td>
 *          </tr>
 *          </table>
 */
public abstract class AbstractWebView extends AbstractView implements IWebView {

	protected String contentType;

	/* (non-Javadoc)
	 * @see com.ymatesoft.platform.webmvc.view.IView#getContentType()
	 */
	public String getContentType() {
		return contentType;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.view.IWebView#setContentType(java.lang.String)
	 */
	public IWebView setContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.view.IWebView#addDateHeader(java.lang.String, long)
	 */
	public IWebView addDateHeader(String name, long date) {
		WebContext.getResponse().addDateHeader(name, date);
		return this;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.view.IWebView#addHeader(java.lang.String, java.lang.String)
	 */
	public IWebView addHeader(String name, String value) {
		WebContext.getResponse().addHeader(name, value);
		return this;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.web.view.IWebView#addIntHeader(java.lang.String, int)
	 */
	public IWebView addIntHeader(String name, int value) {
		WebContext.getResponse().addIntHeader(name, value);
		return this;
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.view.AbstractView#render()
	 */
	public void render() throws Exception {
		if (WebContext.getResponse().isCommitted()) {
			return;
		}
		renderView();
	}

	/* (non-Javadoc)
	 * @see net.ymate.platform.mvc.view.IView#render(java.io.OutputStream)
	 */
	public void render(OutputStream output) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 * 视图渲染具体操作
	 * 
	 * @throws Exception 抛出任何可能异常
	 */
	protected abstract void renderView() throws Exception;

	/**
	 * @return 返回修正过的模板基准路径并以'/WEB-INF'开始，以'/'结束
	 */
	protected String getBaseViewPath() {
		return TemplateHelper.getRootViewPath();
	}

	/**
	 * 将参数与URL地址进行绑定
	 * @throws UnsupportedEncodingException  URL编码异常
	 */
	protected String bindUrl(String url) throws UnsupportedEncodingException  {
		if (this.getAttributes().isEmpty()) {
			return url;
		}
		StringBuilder _paramSB = new StringBuilder(url);
		if (url.indexOf("?") == -1) {
			_paramSB.append("?");
		} else {
			_paramSB.append("&");
		}
		boolean _flag = true;
		for (Entry<String, Object> entry : this.getAttributes().entrySet()) {
			if (_flag) {
				_flag = false;
			} else {
				_paramSB.append("&");
			}
			_paramSB.append(entry.getKey()).append("=");
			if (entry.getValue() != null && StringUtils.isNotEmpty(entry.getValue().toString())) {
				_paramSB.append(URLEncoder.encode(entry.getValue().toString(), WebMVC.getConfig().getCharsetEncoding()));
			}
		}
		return _paramSB.toString();
	}

}
