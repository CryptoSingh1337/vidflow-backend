package com.saransh.vidflowservice.video;

import com.saransh.vidflownetwork.v2.request.video.CommentRequestModel;
import com.saransh.vidflownetwork.v2.request.video.UpdateCommentRequestModel;
import com.saransh.vidflownetwork.v2.response.video.CommentResponseModel;

/**
 * author: CryptoSingh1337
 */
public interface CommentService {

    CommentResponseModel addCommentToVideo(String videoId, CommentRequestModel comment);

    CommentResponseModel updateComment(String videoId, String commentId, UpdateCommentRequestModel updateComment);

    void deleteComment(String videoId, String commentId);
}
