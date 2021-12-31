package com.saransh.vidflow.mapper;

import com.saransh.vidflow.domain.Comment;
import com.saransh.vidflow.model.request.video.CommentRequestModel;
import com.saransh.vidflow.model.response.video.AddCommentResponseModel;
import org.mapstruct.Mapper;

/**
 * author: CryptoSingh1337
 */
@Mapper
public interface CommentMapper {

    Comment commentRequestModelToComment(CommentRequestModel commentRequestModel);
    AddCommentResponseModel commentToAddCommentResponseModel(Comment comment);
}
