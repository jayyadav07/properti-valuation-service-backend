package mu.mcb.property.evalution.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import mu.mcb.property.evalution.dto.BorrowerDto;
import mu.mcb.property.evalution.dto.CommentsDto;
import mu.mcb.property.evalution.entity.Borrower;
import mu.mcb.property.evalution.entity.Comment;
import mu.mcb.property.evalution.entity.User;
import mu.mcb.property.evalution.repository.BorrowerRepository;
import mu.mcb.property.evalution.repository.CommentRepository;
import mu.mcb.property.evalution.repository.UserRepository;
import mu.mcb.property.evalution.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Override
	public List<CommentsDto> getcommentsByUserId(Long userId) {
		Optional<User> optuser = userRepository.findById(userId);
		if (optuser.isPresent()) {
			List<CommentsDto> commentDtoList;
			Optional<List<Comment>> commentList = commentRepository.findByUserId(userId);
			if (commentList.isPresent() && !CollectionUtils.isEmpty(commentList.get())) {
				commentDtoList = new ArrayList<>();
				commentList.get().forEach(comment -> {
					CommentsDto commentDto = new CommentsDto();
					BeanUtils.copyProperties(comment, commentDto);
					commentDto.setCreatedDate(comment.getCreatedDate());
					commentDto.setUserName(comment.getUser().getName());
					commentDtoList.add(commentDto);
				});
			} else {
				commentDtoList = null;
			}

			return commentDtoList;
		}
		return null;
	}
	
	

	
}
