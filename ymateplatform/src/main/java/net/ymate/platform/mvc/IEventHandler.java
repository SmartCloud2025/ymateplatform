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
package net.ymate.platform.mvc;

import net.ymate.platform.mvc.context.IRequestContext;
import net.ymate.platform.mvc.view.IView;


/**
 * <p>
 * IEventHandler
 * </p>
 * <p>
 * MVC框架事件处理器;
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
 *          <td>2012-12-3下午3:48:30</td>
 *          </tr>
 *          </table>
 */
public interface IEventHandler {

	/**
	 * MVC框架初始化时将执行此事件回调
	 */
	public void onInitialized();

    /**
     * 当接收到请求时将执行此事件回调，返回非空视图对象将改变本次请求的执行结果
     *
     * @param context
     * @return 返回视图对象
     */
	public IView onRequestReceived(IRequestContext context);

	/**
	 * 当接收到的请求处理完毕时将执行此事件回调
	 *
	 * @param context
	 */
	public void onRequestCompleted(IRequestContext context);

	/**
	 * MVC框架销毁时将执行此事件回调
	 */
	public void onDestroyed();

}
