/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jtalks.jcommune.web.tags;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.AbstractContext;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * @author Mikhail Stryzhonok
 */
public class VelocityMacro extends BodyTagSupport {
    private static VelocityEngine engine = new VelocityEngine(getProperties());

    private static Properties getProperties() {
        Properties properties = new Properties();
        properties.put("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
        properties.put(RuntimeConstants.RESOURCE_LOADER, "classpath");
        properties.put("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        return properties;
    }

    @Override
    public int doEndTag() throws JspException {
        BodyContent bc = getBodyContent();
        if (bc == null) return EVAL_PAGE;

        try {
            JspWriter writer = pageContext.getOut();
            Reader reader = bc.getReader();

            Context ctx = new JSPContext(pageContext);
            engine.evaluate(ctx, writer, "VelocityTag", reader);
        } catch (Exception e) {
            e.printStackTrace();
            throw new JspException(e);
        }

        return EVAL_PAGE;
    }

    private static class JSPContext extends AbstractContext {

        private HashMap<String, Object> map = new HashMap<>();
        private PageContext pageContext = null;

        public JSPContext(PageContext pageContext) {
            this.pageContext = pageContext;
        }

        private Object findValue(String key) {
            Object value = pageContext.getAttribute(key, PageContext.PAGE_SCOPE);
            if (value == null) {
                value = pageContext.getAttribute(key, PageContext.REQUEST_SCOPE);
                if (value == null) {
                    value = pageContext.getAttribute(key, PageContext.SESSION_SCOPE);
                    if (value == null) {
                        value = pageContext.getAttribute(key, PageContext.APPLICATION_SCOPE);
                    }
                }
            }
            return value;
        }

        private Object getByReflection(Object bean, String key) {
            StringBuffer sb = new StringBuffer(key.length() + 3);
            sb.append("get");
            sb.append(key.substring(0, 1).toUpperCase());
            if (key.length() > 1) sb.append(key.substring(1));
            try {
                Method m = bean.getClass().getMethod(sb.toString(), new Class[0]);
                if (m != null) {
                    m.setAccessible(true);
                    return m.invoke(bean);
                }
            } catch(Exception e) {
                // cannot find by reflection... not an exception...
            }
            return null;
        }

        private Object getValue(String key) {
            StringTokenizer st = new StringTokenizer(key, ".");
            if (st.countTokens() == 1) {
                return findValue(st.nextToken());
            } else {
                String first = st.nextToken();
                Object value = findValue(first);
                if (value == null) return null;
                while(st.hasMoreTokens()) {
                    String next = st.nextToken();
                    if (value instanceof Map) {
                        Map map = (Map) value;
                        value = map.get(next);
                    } else {
                        value = getByReflection(value, next);
                    }
                    if (value == null) return null;
                }
                return value;
            }
        }

        public Object internalGet(String key) {
            Object value = map.get(key);
            if (value != null) {
                return value;
            } else  {
                value = getValue(key);
                if (value != null) {
                    map.put(key, value);
                }
            }
            return value;
        }

        public Object internalPut(String key, Object value) {
            return map.put(key, value);
        }

        public boolean internalContainsKey(Object key) {
            return map.containsKey(key);
        }

        public Object [] internalGetKeys() {
            return map.keySet().toArray();
        }

        public Object internalRemove(Object key) {
            return map.remove(key);
        }
    }
}
