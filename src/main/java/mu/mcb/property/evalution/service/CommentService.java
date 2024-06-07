package mu.mcb.property.evalution.service;

import java.util.List;

import mu.mcb.property.evalution.dto.CommentsDto;

public interface CommentService {

	List<CommentsDto> getcommentsByUserId(Long iserId);

}
