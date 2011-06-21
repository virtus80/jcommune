/**
 * Copyright (C) 2011  jtalks.org Team
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
 * Also add information on how to contact you by electronic and paper mail.
 * Creation date: Apr 12, 2011 / 8:05:19 PM
 * The jtalks.org Project
 */
package org.jtalks.jcommune.web.controller;

import java.util.HashMap;
import java.util.Map;


import org.jtalks.jcommune.service.TopicService;
import org.jtalks.jcommune.service.exceptions.NotFoundException;
import org.springframework.web.servlet.ModelAndView;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Matchers.anyLong;

import static org.springframework.test.web.ModelAndViewAssert.assertViewName;
import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeValues;

/**
 * This is test for <code>PostController<code> class.
 * Test should cover view resolution and logic validation.
 *
 * @author Osadchuck Eugeny
 */
public class PostControllerTest {
    private TopicService topicService;
    private PostController controller;

    @BeforeMethod
    public void init() {
        topicService = mock(TopicService.class);
        controller = new PostController(topicService);
    }


    @Test
    public void confirmTest() {
        long topicId = 1;
        long branchId = 1;
        long postId = 5;

        ModelAndView actualMav = controller.deleteConfirmPage(topicId, postId, branchId);

        assertViewName(actualMav, "deletePost");
        Map<String, Object> expectedModel = new HashMap<String, Object>();
        expectedModel.put("topicId", topicId);
        expectedModel.put("postId", postId);
        expectedModel.put("branchId", branchId);
        assertModelAttributeValues(actualMav, expectedModel);

    }

    @Test
    public void deleteTest() throws NotFoundException {
        long topicId = 1;
        long postId = 5;
        long branchId = 1;

        ModelAndView actualMav = controller.delete(topicId, postId, branchId);

        assertViewName(actualMav, "redirect:/branch/1/topic/" + topicId + ".html");
        verify(topicService, times(1)).deletePost(topicId, postId);
    }
}
