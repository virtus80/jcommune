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
package org.jtalks.jcommune.service;

import org.jtalks.jcommune.model.entity.Branch;
import org.jtalks.jcommune.model.entity.Topic;
import org.jtalks.jcommune.model.entity.User;
import org.jtalks.jcommune.service.exceptions.MailingFailedException;

/**
 * This service is focused on sending e-mail to the forum users.
 * Notifications, confirmations or e-mail based subscriptions of a various
 * kind should use this service to perform e-mail sending.
 *
 * @author Evgeniy Naumenko
 */
public interface MailService {

    /**
     * Sends a password recovery message for the user with a given email.
     * This method does not generate new password, just sends a message.
     *
     * @param userName    username to be used in a mail
     * @param email       address to mail to
     * @param newPassword new user password to be placed in an email
     * @throws MailingFailedException when mailing failed
     */
    void sendPasswordRecoveryMail(String userName, String email, String newPassword) throws MailingFailedException;

    /**
     * Sends update notification to user specified, e.g. when some new
     * posts were added to the topic. This method won't check if user
     * is subscribed to the particular notification or not.
     *
     * @param user a person to be notified about updates by email
     * @param topic topic changed (to include more detailes in email)
     * @throws MailingFailedException when mailing failed due to some reason
     */
    void sendTopicUpdatesOnSubscription(User user, Topic topic) throws MailingFailedException;

    /**
     * Sends update notification to user specified, e.g. when some new
     * posts were added to the topic. This method won't check if user
     * is subscribed to the particular notification or not.
     *
     * @param user a person to be notified about updates by email
     * @param branch branch changed (to include more detailes in email)
     * @throws MailingFailedException when mailing failed due to some reason
     */
    void sendBranchUpdatesOnSubscription(User user, Branch branch) throws MailingFailedException;
}
