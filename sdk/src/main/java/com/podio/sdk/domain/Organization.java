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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.podio.sdk.internal.Utils;

public class Organization {

    public static enum ContractStatus {
        none, partial, full, undefined
    }

    public static enum PriceTier {
        basic, plus, premium, undefined
    }

    public static enum Role {
        admin, regular, light, undefined
    }

    public static enum Segment {
        education, undefined
    }

    public static enum Status {
        active, inactive, deleted, undefined
    }

    public static enum Type {
        free, sponsored, premium, undefined
    }

    private final Boolean allow_add_space = null;
    private final Boolean premium = null;
    private final File image = null;
    private final Integer grants_count = null;
    private final Integer rank = null;
    private final Integer segment_size = null;
    private final Integer user_limit = null;
    private final List<String> rights = null;
    private final Long logo = null;
    private final Long leadinator_item_id = null;
    private final Long org_id = null;
    private final Long sales_agent_id = null;
    private final Space[] spaces = null;
    private final String contract_status = null;
    private final String created_on = null;
    private final String name = null;
    private final String role = null;
    private final String segment = null;
    private final String status = null;
    private final String tier = null;
    private final String type = null;
    private final String url = null;
    private final String url_label = null;
    private final String[] domains = null;
    private final Profile created_by = null;
    private final Profile sales_agent = null;

    public boolean doAllowCreateNewSpace() {
        return allow_add_space;
    }

    public List<Space> getActiveSpaces() {
        ArrayList<Space> activeSpaces = new ArrayList<Space>();
        for (Space space : getAllSpaces()) {
            if (space.isArchived()) {
                continue;
            }
            activeSpaces.add(space);
        }
        return activeSpaces;
    }

    public List<Space> getAllSpaces() {
        return Utils.notEmpty(spaces) ? Arrays.asList(spaces) : new ArrayList<Space>();
    }

    public ContractStatus getContractStatus() {
        try {
            return ContractStatus.valueOf(contract_status);
        } catch (NullPointerException e) {
            return ContractStatus.undefined;
        } catch (IllegalArgumentException e) {
            return ContractStatus.undefined;
        }
    }

    public Profile getCreatedBy() {
        return created_by;
    }

    public Date getCreatedDate() {
        return Utils.parseDateTime(created_on);
    }

    public String getCreatedDateString() {
        return created_on;
    }

    public List<String> getDomains() {
        return Utils.notEmpty(domains) ? Arrays.asList(domains) : new ArrayList<String>();
    }

    public int getGrantCount() {
        return Utils.getNative(grants_count, 0);
    }

    public long getId() {
        return Utils.getNative(org_id, -1L);
    }

    public File getImage() {
        return image;
    }

    public long getLeadinatorItemId() {
        return Utils.getNative(leadinator_item_id, -1L);
    }

    public long getLogoId() {
        return Utils.getNative(logo, -1L);
    }

    public String getName() {
        return name;
    }

    public int getRank() {
        return Utils.getNative(rank, 0);
    }

    public Role getRole() {
        try {
            return Role.valueOf(role);
        } catch (NullPointerException e) {
            return Role.undefined;
        } catch (IllegalArgumentException e) {
            return Role.undefined;
        }
    }

    public Profile getSalesAgent() {
        return sales_agent;
    }

    public long getSalesAgentId() {
        return Utils.getNative(sales_agent_id, -1L);
    }

    public Segment getSegment() {
        try {
            return Segment.valueOf(segment);
        } catch (NullPointerException e) {
            return Segment.undefined;
        } catch (IllegalArgumentException e) {
            return Segment.undefined;
        }
    }

    public int getSegmentSize() {
        return Utils.getNative(segment_size, -1);
    }

    public Status getStatus() {
        try {
            return Status.valueOf(status);
        } catch (NullPointerException e) {
            return Status.undefined;
        } catch (IllegalArgumentException e) {
            return Status.undefined;
        }
    }

    public PriceTier getTier() {
        try {
            return PriceTier.valueOf(tier);
        } catch (NullPointerException e) {
            return PriceTier.undefined;
        } catch (IllegalArgumentException e) {
            return PriceTier.undefined;
        }
    }

    public Type getType() {
        try {
            return Type.valueOf(type);
        } catch (NullPointerException e) {
            return Type.undefined;
        } catch (IllegalArgumentException e) {
            return Type.undefined;
        }
    }

    public String getUrl() {
        return url;
    }

    public String getUrlLabel() {
        return url_label;
    }

    public int getUserLimit() {
        return Utils.getNative(user_limit, 5);
    }

    /**
     * Checks whether the list of rights the user has for this Organization contains <em>all</em>
     * the given permissions.
     *
     * @param rights
     *         The list of permissions to check for.
     *
     * @return Boolean true if all given permissions are granted for this Organization. Boolean
     * false otherwise.
     */
    public boolean hasAllRights(Right... rights) {
        if (Utils.isEmpty(this.rights) && Utils.isEmpty(rights)) {
            // The user has no rights and wants to verify that.
            return true;
        }

        if (Utils.notEmpty(this.rights) && Utils.notEmpty(rights)) {
            for (Right right : rights) {
                if (!this.rights.contains(right.name())) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    /**
     * Checks whether the list of rights the user has for this Organization contains <em>any</em> of
     * the given permissions.
     *
     * @param rights
     *         The list of permissions to check any single one for.
     *
     * @return Boolean true if any given permission is granted for this Organization. Boolean false
     * otherwise.
     */
    public boolean hasAnyRights(Right... rights) {
        if (Utils.isEmpty(this.rights) && Utils.isEmpty(rights)) {
            // The user has no rights and wants to verify that.
            return true;
        }

        if (Utils.notEmpty(this.rights) && Utils.notEmpty(rights)) {
            for (Right right : rights) {
                if (this.rights.contains(right.name())) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isPremium() {
        return Utils.getNative(premium, false);
    }
}
