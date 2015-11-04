
package com.podio.sdk.provider;

import com.podio.sdk.Filter;
import com.podio.sdk.Provider;
import com.podio.sdk.Request;
import com.podio.sdk.domain.Comment;
import com.podio.sdk.domain.ReferenceType;

/**
 * Enables access to the Comments API end point.
 *
 */
public class CommentProvider extends Provider {

    static class CommentsFilter extends Filter {

        protected CommentsFilter() {
            super("comment");
        }

        public CommentsFilter withReference(ReferenceType type, long id) {
            this.addPathSegment(type.name());
            this.addPathSegment(Long.toString(id, 10));

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
