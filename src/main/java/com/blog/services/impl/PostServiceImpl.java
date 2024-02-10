package com.blog.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.blog.entities.Category;
import com.blog.entities.Post;
import com.blog.entities.User;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.payloads.PostDto;
import com.blog.payloads.PostResponse;
import com.blog.repositories.CategoryRepo;
import com.blog.repositories.PostRepo;
import com.blog.repositories.UserRepo;
import com.blog.services.PostService;

@Service
public class PostServiceImpl implements PostService{

	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private CategoryRepo catRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Override
	public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {
		
		Category cat=catRepo.findById(categoryId).orElseThrow(( )->new ResourceNotFoundException("Category", "CatId", categoryId));
		
		User user=userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "userId", userId));
		
		Post post=this.modelMapper.map(postDto, Post.class);
		post.setImageName("default.png");
		post.setAddDate(new Date());
		post.setCategory(cat);
		post.setUser(user);
		Post addedPost=postRepo.save(post);
		return this.modelMapper.map(addedPost, PostDto.class);
		
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {
		
		Post post = this.postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post ", "post id", postId));

//        Category category = this.catRepo.findById(postDto.getCategory().getCategoryId()).get();

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setImageName(postDto.getImageName());
//        post.setCategory(category);


        Post updatedPost = this.postRepo.save(post);
        return this.modelMapper.map(updatedPost, PostDto.class);
	}

	@Override
	public void deletePost(Integer postId) {
		Post post=postRepo.findById(postId)
				.orElseThrow(()->new ResourceNotFoundException("Post", "post Id", postId));
		this.postRepo.delete(post);
	}


	
	@Override
	public PostResponse getAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        PageRequest p = PageRequest.of(pageNumber, pageSize,sort);

        Page<Post> pagePost = this.postRepo.findAll(p);

        List<Post> allPosts = pagePost.getContent();

        List<PostDto> postDtos = allPosts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();

        postResponse.setContent(postDtos);
        postResponse.setPageNumber(pagePost.getNumber());
        postResponse.setPageSize(pagePost.getSize());
        postResponse.setTotalElements(pagePost.getTotalElements());

        postResponse.setTotalPages(pagePost.getTotalPages());
        postResponse.setLastPage(pagePost.isLast());

        return postResponse;
    
	}

	@Override
	public PostDto getPost(Integer postId) {
		Post post = this.postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "post id", postId));
		System.out.println(post.getAddDate());
        PostDto dtoPost=this.modelMapper.map(post, PostDto.class);
        System.out.println("dtoDate: "+dtoPost.getAddedDate());
        
        dtoPost.setAddedDate(post.getAddDate());
        System.out.println("dtoDate after set: "+dtoPost.getAddedDate());
        return dtoPost;
	}

	@Override
	public List<PostDto> getPostsByCategory(Integer catId) {
		Category cat= catRepo.findById(catId)
				.orElseThrow(()->new ResourceNotFoundException("Category", "catId", catId));
		
		List<Post> list=postRepo.findByCategory(cat);
		
		List<PostDto> dtoList = list.stream().map((post)->this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		
		return dtoList;
	}

	@Override
	public List<PostDto> getPostsByUser(Integer userId) {
		  User user = this.userRepo.findById(userId)
	                .orElseThrow(() -> new ResourceNotFoundException("User ", "userId ", userId));
	        List<Post> posts = this.postRepo.findByUser(user);

	        List<PostDto> postDtos = posts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
	                .collect(Collectors.toList());

	        return postDtos;
	}

	@Override
	public List<PostDto> searchPosts(String keyword) {
		List<Post> posts = postRepo.searchByTitle("%"+keyword+"%");
		List<PostDto> postsDto=posts.stream().map((post)->modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		return postsDto;
	}

}
