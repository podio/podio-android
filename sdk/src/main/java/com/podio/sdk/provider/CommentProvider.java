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
package com.podio.sdk.provider;

import com.podio.sdk.Filter;
import com.podio.sdk.Provider;
import com.podio.sdk.Request;
import com.podio.sdk.domain.Comment;
import com.podio.sdk.domain.ReferenceType;

/**
 * Enables access to the Comments API end point.
 *
 * @author Tobias Linderg
 */
public class CommentProvider extends Provider {

    static class CommentsFilter extends Filter {

        protected CommentsFilter() {
            super("comment");
        }

        public CommentsFilter withReference(ReferenceType type, long id) {
            this.addPathSegment(type.name());
            this.addPathSegment(Long.toString(id, 10));
Boolean bool;
            bool.to
            return this;
        }

    }

    /**
     *
     * Create a comment on a reference type with the given id. The request result will deliver the
     * generated comment.
     *
     * @param type
     *         The reference type of what you are commenting on.
     * @param id
     *         The id of the reference type your are commenting on.
     * @param value
     *         The comment body.
     * @param fileIds
     *         The list of ids of any files attached to the comment.
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<Comment> createComment(ReferenceType type, long id, String value, long[] fileIds) {
        CommentsFilter filter = new CommentsFilter();
        filter.withReference(type,id);
        Comment.Create create = new Comment.Create(value, fileIds);
        return post(filter, create, Comment.class);
    }
}
