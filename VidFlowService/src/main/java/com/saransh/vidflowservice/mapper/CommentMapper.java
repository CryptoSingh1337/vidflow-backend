package com.saransh.vidflowservice.mapper;

import com.saransh.vidflowdata.entity.Comment;
import com.saransh.vidflownetwork.v2.request.video.CommentRequestModel;
import com.saransh.vidflownetwork.v2.response.video.CommentResponseModel;

/**
 * author: CryptoSingh1337
 */
public interface CommentMapper {

    Comment commentRequestModelToComment(CommentRequestModel commentRequestModel);

    CommentResponseModel commentToCommentResponseModel(Comment comment);
}
