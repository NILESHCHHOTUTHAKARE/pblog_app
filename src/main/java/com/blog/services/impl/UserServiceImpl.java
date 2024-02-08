package com.blog.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.entities.User;
import com.blog.payloads.UserDto;
import com.blog.repositories.UserRepo;
import com.blog.services.UserService;
import com.blog.exceptions.*;

@Service
public class UserServiceImpl implements UserService  {
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired 
	private ModelMapper modelMapper;
	
	
	@Override
	public UserDto createUser(UserDto user) {
		
		User userSaved=userRepo.save(dtoToUser(user));
		return userToDto(userSaved);
	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {

		User user=userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","Id",userId));
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		user.setAbout(userDto.getAbout());
		User updatedUser=userRepo.save(user);
		return userToDto(updatedUser);
	}

	@Override
	public UserDto getUserById(Integer userId) {
		User user=userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "Id", userId));
		return userToDto(user);
	}

	@Override
	public List<UserDto> getAllUser() {
		// TODO Auto-generated method stub
		List<User> userList=userRepo.findAll();
//		List<UserDto> dtoList= new ArrayList<UserDto>();
//		for(User user:userList) {
//			
//			dtoList.add(userToDto(user));
//		}
		List<UserDto>dtoList=userList.stream().map(e->userToDto(e)).collect(Collectors.toList());
		return dtoList;
	}

	@Override
	public void deleteUser(Integer userId) {
		User user=userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "Id", userId));
		userRepo.delete(user);
		
	}
	
	
	private User dtoToUser(UserDto userDto) {
		User user= this.modelMapper.map(userDto, User.class);
//		User user=new User();
//		user.setId(userDto.getId());
//		user.setName(userDto.getName());
//		user.setEmail(userDto.getEmail());
//		user.setPassword(userDto.getPassword());
//		user.setAbout(userDto.getAbout());
		return user;
	}
	
	private UserDto userToDto(User user) {
		UserDto userDto =this.modelMapper.map(user, UserDto.class);
		return userDto;
	}

}
















