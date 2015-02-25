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

import com.podio.sdk.internal.Utils;

/**
 * A Java representation of the VotingDTO API domain object.
 *
 * @author Tobias Lindberg
 */
public class Voting {

    public static enum VotingKind {
        answer,
        fivestar,
        unknown // Custom value to handle errors.
    }
    private final String kind = null;

    private final String question = null;

    private final Long voting_id = null;

    public long getVotingId() {
        return Utils.getNative(voting_id, -1L);
    }

    public VotingKind getKind() {
        try {
            return VotingKind.valueOf(kind);
        } catch (NullPointerException e) {
            return VotingKind.unknown;
        } catch (IllegalArgumentException e) {
            return VotingKind.unknown;
        }
    }

    public String getQuestion() {
        return question;
    }
}
