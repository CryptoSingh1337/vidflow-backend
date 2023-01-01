package com.saransh.vidflowservice.mapper;

import com.saransh.vidflowdata.entity.Comment;
import com.saransh.vidflownetwork.request.video.CommentRequestModel;
import com.saransh.vidflownetwork.response.video.AddCommentResponseModel;
import org.mapstruct.Mapper;

/**
 * author: CryptoSingh1337
 */
@Mapper
public interface CommentMapper {

    Comment commentRequestModelToComment(CommentRequestModel commentRequestModel);
    AddCommentResponseModel commentToAddCommentResponseModel(Comment comment);
}
