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
package org.jtalks.jcommune.model.validation.validators;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;
import org.jtalks.jcommune.model.entity.Poll;
import org.jtalks.jcommune.model.entity.PollItem;
import org.jtalks.jcommune.model.validation.annotations.PollFilledCompletely;
import org.springframework.util.CollectionUtils;

/**
 * 
 * @author Anuar_Nurmakanov
 *
 */
public class PollFilledCompleteletValidator implements ConstraintValidator<PollFilledCompletely, Poll> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(PollFilledCompletely constraintAnnotation) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(Poll poll, ConstraintValidatorContext context) {
        String pollTitle = poll.getTitle();
        List<PollItem> pollItems = poll.getPollItems();
        boolean isTitleFilled = StringUtils.isNotBlank(pollTitle);
        boolean isItemsFilled = !CollectionUtils.isEmpty(pollItems);
        //
        boolean isFilledCompletely = true;
        if (isTitleFilled && !isItemsFilled) {
            isFilledCompletely = false;
        } else if(!isTitleFilled && isItemsFilled) {
            isFilledCompletely = false;
        }
        return isFilledCompletely;
    }
}
