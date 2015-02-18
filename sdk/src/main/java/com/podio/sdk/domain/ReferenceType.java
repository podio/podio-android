/*
 *  Copyright (C) 2014 Copyright Citrix Systems, Inc.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of
 *  this software and associated documentation files (the "Software"), to deal in
 *  the Software without restriction, including without limitation the rights to
 *  use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 *  of the Software, and to permit persons to whom the Software is furnished to
 *  do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package com.podio.sdk.domain;

public enum ReferenceType {
    action,
    alert,
    answer,
    app,
    app_field,
    app_revision,
    auth_client,
    batch,
    bulletin,
    comment,
    condition,
    condition_set,
    connection,
    contract,
    conversation,
    embed,
    extension,
    extension_installation,
    file,
    file_service,
    flow,
    flow_condition,
    flow_effect,
    form,
    grant,
    hook,
    icon,
    identity,
    integration,
    invoice,
    item,
    item_participation,
    item_revision,
    item_transaction,
    label,
    linked_account,
    live,
    location,
    message,
    notification,
    org,
    org_member,
    partner,
    payment,
    profile,
    promotion,
    question,
    question_answer,
    rating,
    share,
    share_install,
    space,
    space_member,
    space_member_request,
    status,
    subscription,
    system,
    tag,
    task,
    task_action,
    user,
    view,
    vote,
    voting,
    widget,
    unknown     // Custom value to handle errors.
}
