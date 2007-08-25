/*
 * Copyright 2004 original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jmesaweb.controller;

import org.jmesa.util.ItemUtils;
import org.jmesa.view.html.event.RowEvent;

/**
 * @since 2.1
 * @author Jeff Johnston
 */
public class TagRowEvent implements RowEvent {
    public String execute(Object item, int rowcount) {
        Object bean = ItemUtils.getItemValue(item, "bean");
        Object id = ItemUtils.getItemValue(bean, "id");
        return "document.location='http://www.whitehouse.gov/history/presidents?id=" + id + "'";
    }
}