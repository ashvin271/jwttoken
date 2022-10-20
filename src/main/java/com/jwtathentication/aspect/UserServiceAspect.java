package com.jwtathentication.aspect;

import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.jwtathentication.entity.User;

@Aspect
@Component
public class UserServiceAspect {

	// first * declare  any return type
	// if you want run point cut for all method then use after * serviceimpl class
	// if you want run point cut for single method then pass method name after class 
	// (..) dot dot pass to number of parameter 
	
	/*
	 * @Before("execution(* com.jwtathentication.serviceimpl.UserServiceImpl.*(..))"
	 * ) public void getUsers(JoinPoint joinPoint) {
	 * System.out.println("before method "+joinPoint.getSignature()); }
	 * 
	 * @After("execution(* com.jwtathentication.serviceimpl.UserServiceImpl.*(..))")
	 * public void getUsers1(JoinPoint joinPoint) {
	 * System.out.println("after method "+joinPoint.getSignature()); }
	 */
	
	@Pointcut("execution(* com.jwtathentication.serviceimpl.UserServiceImpl.saveUser(..))")
	public void saveUserPointCut() {}
	
	@Pointcut("execution(* com.jwtathentication.serviceimpl.UserServiceImpl.getAllUsers())")
	public void getUserPointCut() {}
	
	@Before("saveUserPointCut()")
	public void  getUsers(JoinPoint joinPoint) {
		System.out.println("before method "+joinPoint.getSignature());
	}
	
	@After("getUserPointCut()")
	public void  getUsers1(JoinPoint joinPoint) {
		System.out.println("after method "+joinPoint.getSignature());
	}
	
	@AfterReturning(value="execution(* com.jwtathentication.serviceimpl.UserServiceImpl.getAllUsers())",returning ="userList")
	public void  afterReturn(JoinPoint joinPoint,List<User> userList) {
		System.out.println("return method ");
		userList.forEach(a-> System.out.println(a.getFirstName()));
	}
}
