package com.saransh.vidflowservice.video;

import com.saransh.vidflownetwork.request.video.CommentRequestModel;
import com.saransh.vidflownetwork.request.video.UpdateCommentRequestModel;
import com.saransh.vidflownetwork.response.video.AddCommentResponseModel;
import com.saransh.vidflownetwork.v2.response.video.CommentResponseModel;

/**
 * author: CryptoSingh1337
 */
public interface CommentService {

    @Deprecated
    AddCommentResponseModel addCommentToVideo(String videoId, CommentRequestModel comment);

    CommentResponseModel addCommentToVideoV2(String videoId, com.saransh.vidflownetwork.v2.request.video.CommentRequestModel comment);

    void updateComment(String videoId, String commentId, UpdateCommentRequestModel commentRequestModel);

    void deleteComment(String videoId, String commentId);

    CommentResponseModel updateCommentV2(String videoId, String commentId, com.saransh.vidflownetwork.v2.request.video.UpdateCommentRequestModel updateComment);
}
