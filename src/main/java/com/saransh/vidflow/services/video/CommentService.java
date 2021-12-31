package com.saransh.vidflow.services.video;

import com.saransh.vidflow.model.request.video.CommentRequestModel;
import com.saransh.vidflow.model.request.video.UpdateCommentRequestModel;
import com.saransh.vidflow.model.response.video.AddCommentResponseModel;

/**
 * author: CryptoSingh1337
 */
public interface CommentService {

    AddCommentResponseModel addCommentToVideo(String videoId, CommentRequestModel comment);
    void updateComment(String videoId, String commentId, UpdateCommentRequestModel commentRequestModel);
    void deleteComment(String videoId, String commentId);
}
