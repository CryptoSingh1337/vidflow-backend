package com.saransh.vidflow.services.video;

import com.saransh.vidflow.model.request.video.CommentRequestModel;
import com.saransh.vidflow.model.response.video.AddCommentResponseModel;

/**
 * author: CryptoSingh1337
 */
public interface CommentService {

    AddCommentResponseModel addCommentToVideo(String videoId, CommentRequestModel comment);
}
