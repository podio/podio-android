
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
    unknown;     // Custom value to handle errors.

    public static ReferenceType getType(String type) {
        try {
            return ReferenceType.valueOf(type);
        } catch (NullPointerException e) {
            return ReferenceType.unknown;
        } catch (IllegalArgumentException e) {
            return ReferenceType.unknown;
        }
    }
}
