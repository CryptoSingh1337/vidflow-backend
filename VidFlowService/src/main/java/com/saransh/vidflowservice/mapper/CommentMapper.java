package com.saransh.vidflowservice.mapper;

import com.saransh.vidflowdata.entity.Comment;
import com.saransh.vidflownetwork.request.video.CommentRequestModel;
import com.saransh.vidflownetwork.response.video.AddCommentResponseModel;
import com.saransh.vidflownetwork.v2.response.video.CommentResponseModel;
import org.mapstruct.Mapper;

/**
 * author: CryptoSingh1337
 */
@Mapper
public interface CommentMapper {

    @Deprecated
    Comment commentRequestModelToComment(CommentRequestModel commentRequestModel);

    Comment commentRequestModelToCommentV2(com.saransh.vidflownetwork.v2.request.video.CommentRequestModel commentRequestModel);

    @Deprecated
    AddCommentResponseModel commentToCommentResponseModel(Comment comment);

    CommentResponseModel commentToCommentResponseModelV2(Comment comment);
}
