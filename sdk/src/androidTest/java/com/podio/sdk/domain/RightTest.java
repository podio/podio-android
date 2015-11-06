
package com.podio.sdk.domain;

import java.util.Arrays;
import java.util.List;

import android.test.AndroidTestCase;

public class RightTest extends AndroidTestCase {

    public void testValueOf() {
        assertEquals(Right.view, Right.valueOf("view"));
        assertEquals(Right.update, Right.valueOf("update"));
        assertEquals(Right.delete, Right.valueOf("delete"));
        assertEquals(Right.subscribe, Right.valueOf("subscribe"));
        assertEquals(Right.comment, Right.valueOf("comment"));
        assertEquals(Right.rate, Right.valueOf("rate"));
        assertEquals(Right.share, Right.valueOf("share"));
        assertEquals(Right.install, Right.valueOf("install"));
        assertEquals(Right.add_app, Right.valueOf("add_app"));
        assertEquals(Right.add_item, Right.valueOf("add_item"));
        assertEquals(Right.add_file, Right.valueOf("add_file"));
        assertEquals(Right.add_task, Right.valueOf("add_task"));
        assertEquals(Right.add_space, Right.valueOf("add_space"));
        assertEquals(Right.add_status, Right.valueOf("add_status"));
        assertEquals(Right.add_conversation, Right.valueOf("add_conversation"));
        assertEquals(Right.reply, Right.valueOf("reply"));
        //assertEquals(Right.add_filter, Right.valueOf("add_filter"));
        assertEquals(Right.add_widget, Right.valueOf("add_widget"));
        assertEquals(Right.statistics, Right.valueOf("statistics"));
        assertEquals(Right.add_contact, Right.valueOf("add_contact"));
        assertEquals(Right.add_hook, Right.valueOf("add_hook"));
        assertEquals(Right.add_question, Right.valueOf("add_question"));
        assertEquals(Right.add_answer, Right.valueOf("add_answer"));
        assertEquals(Right.add_contract, Right.valueOf("add_contract"));
        assertEquals(Right.add_user, Right.valueOf("add_user"));
        assertEquals(Right.add_user_light, Right.valueOf("add_user_light"));
        assertEquals(Right.move, Right.valueOf("move"));
        assertEquals(Right.export, Right.valueOf("export"));
        assertEquals(Right.reference, Right.valueOf("reference"));
        assertEquals(Right.view_admins, Right.valueOf("view_admins"));
        assertEquals(Right.download, Right.valueOf("download"));
        assertEquals(Right.view_members, Right.valueOf("view_members"));
        assertEquals(Right.auto_join, Right.valueOf("auto_join"));
        assertEquals(Right.grant, Right.valueOf("grant"));
        assertEquals(Right.view_structure, Right.valueOf("view_structure"));
        assertEquals(Right.add_flow, Right.valueOf("add_flow"));
        assertEquals(Right.request_membership, Right.valueOf("request_membership"));

        assertEquals(Right.view, Enum.valueOf(Right.class, "view"));
        assertEquals(Right.update, Enum.valueOf(Right.class, "update"));
        assertEquals(Right.delete, Enum.valueOf(Right.class, "delete"));
        assertEquals(Right.subscribe, Enum.valueOf(Right.class, "subscribe"));
        assertEquals(Right.comment, Enum.valueOf(Right.class, "comment"));
        assertEquals(Right.rate, Enum.valueOf(Right.class, "rate"));
        assertEquals(Right.share, Enum.valueOf(Right.class, "share"));
        assertEquals(Right.install, Enum.valueOf(Right.class, "install"));
        assertEquals(Right.add_app, Enum.valueOf(Right.class, "add_app"));
        assertEquals(Right.add_item, Enum.valueOf(Right.class, "add_item"));
        assertEquals(Right.add_file, Enum.valueOf(Right.class, "add_file"));
        assertEquals(Right.add_task, Enum.valueOf(Right.class, "add_task"));
        assertEquals(Right.add_space, Enum.valueOf(Right.class, "add_space"));
        assertEquals(Right.add_status, Enum.valueOf(Right.class, "add_status"));
        assertEquals(Right.add_conversation, Enum.valueOf(Right.class, "add_conversation"));
        assertEquals(Right.reply, Enum.valueOf(Right.class, "reply"));
       // assertEquals(Right.add_filter, Enum.valueOf(Right.class, "add_filter"));
        assertEquals(Right.add_widget, Enum.valueOf(Right.class, "add_widget"));
        assertEquals(Right.statistics, Enum.valueOf(Right.class, "statistics"));
        assertEquals(Right.add_contact, Enum.valueOf(Right.class, "add_contact"));
        assertEquals(Right.add_hook, Enum.valueOf(Right.class, "add_hook"));
        assertEquals(Right.add_question, Enum.valueOf(Right.class, "add_question"));
        assertEquals(Right.add_answer, Enum.valueOf(Right.class, "add_answer"));
        assertEquals(Right.add_contract, Enum.valueOf(Right.class, "add_contract"));
        assertEquals(Right.add_user, Enum.valueOf(Right.class, "add_user"));
        assertEquals(Right.add_user_light, Enum.valueOf(Right.class, "add_user_light"));
        assertEquals(Right.move, Enum.valueOf(Right.class, "move"));
        assertEquals(Right.export, Enum.valueOf(Right.class, "export"));
        assertEquals(Right.reference, Enum.valueOf(Right.class, "reference"));
        assertEquals(Right.view_admins, Enum.valueOf(Right.class, "view_admins"));
        assertEquals(Right.download, Enum.valueOf(Right.class, "download"));
        assertEquals(Right.view_members, Enum.valueOf(Right.class, "view_members"));
        assertEquals(Right.auto_join, Enum.valueOf(Right.class, "auto_join"));
        assertEquals(Right.grant, Enum.valueOf(Right.class, "grant"));
        assertEquals(Right.view_structure, Enum.valueOf(Right.class, "view_structure"));
        assertEquals(Right.add_flow, Enum.valueOf(Right.class, "add_flow"));
        assertEquals(Right.request_membership, Enum.valueOf(Right.class, "request_membership"));
    }

    public void testValues() {
        Right[] targetArray = Right.values();
        assertNotNull(targetArray);
        assertEquals(40, targetArray.length);

        List<Right> targetList = Arrays.asList(targetArray);

        assertTrue(targetList.contains(Right.view));
        assertTrue(targetList.contains(Right.update));
        assertTrue(targetList.contains(Right.delete));
        assertTrue(targetList.contains(Right.subscribe));
        assertTrue(targetList.contains(Right.comment));
        assertTrue(targetList.contains(Right.rate));
        assertTrue(targetList.contains(Right.share));
        assertTrue(targetList.contains(Right.install));
        assertTrue(targetList.contains(Right.add_app));
        assertTrue(targetList.contains(Right.add_item));
        assertTrue(targetList.contains(Right.add_file));
        assertTrue(targetList.contains(Right.add_task));
        assertTrue(targetList.contains(Right.add_space));
        assertTrue(targetList.contains(Right.add_status));
        assertTrue(targetList.contains(Right.add_conversation));
        assertTrue(targetList.contains(Right.reply));
        //assertTrue(targetList.contains(Right.add_filter));
        assertTrue(targetList.contains(Right.add_widget));
        assertTrue(targetList.contains(Right.statistics));
        assertTrue(targetList.contains(Right.add_contact));
        assertTrue(targetList.contains(Right.add_hook));
        assertTrue(targetList.contains(Right.add_question));
        assertTrue(targetList.contains(Right.add_answer));
        assertTrue(targetList.contains(Right.add_contract));
        assertTrue(targetList.contains(Right.add_user));
        assertTrue(targetList.contains(Right.add_user_light));
        assertTrue(targetList.contains(Right.move));
        assertTrue(targetList.contains(Right.export));
        assertTrue(targetList.contains(Right.reference));
        assertTrue(targetList.contains(Right.view_admins));
        assertTrue(targetList.contains(Right.download));
        assertTrue(targetList.contains(Right.view_members));
        assertTrue(targetList.contains(Right.auto_join));
        assertTrue(targetList.contains(Right.grant));
        assertTrue(targetList.contains(Right.view_structure));
        assertTrue(targetList.contains(Right.add_flow));
        assertTrue(targetList.contains(Right.request_membership));
    }

}
