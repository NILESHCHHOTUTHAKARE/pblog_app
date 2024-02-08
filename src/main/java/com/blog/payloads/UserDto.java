package com.blog.payloads;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
	
	private int id;
//	@NotBlank
	@NotEmpty
	@Size(min = 4,message = "Name must be atleast of 4 chars !!")
	private String name;

//	@Email(message = "Email address is not valid !!")
//	@NotEmpty(message = "Email is required !!")
	private String email;

	@NotEmpty
//	@Size(min = 3, max = 10, message = "Password must be min of 3 chars and max of 10 chars !!")
	private String password;

	//if you will not give message then default message will be generated
	@NotEmpty(message = "write something")
	private String about;
	
		
}
