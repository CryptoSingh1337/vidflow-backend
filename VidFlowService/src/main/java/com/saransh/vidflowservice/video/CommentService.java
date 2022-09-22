package com.saransh.vidflowservice.video;

import com.saransh.vidflownetwork.request.video.CommentRequestModel;
import com.saransh.vidflownetwork.request.video.UpdateCommentRequestModel;
import com.saransh.vidflownetwork.response.video.AddCommentResponseModel;

/**
 * author: CryptoSingh1337
 */
public interface CommentService {

    AddCommentResponseModel addCommentToVideo(String videoId, CommentRequestModel comment);
    void updateComment(String videoId, String commentId, UpdateCommentRequestModel commentRequestModel);
    void deleteComment(String videoId, String commentId);
}
