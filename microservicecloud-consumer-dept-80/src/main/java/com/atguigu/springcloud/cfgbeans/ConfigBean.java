package com.atguigu.springcloud.cfgbeans;
 
import java.util.Random;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import com.netflix.loadbalancer.RetryRule;
 
@Configuration
public class ConfigBean
{
  @Bean
  @LoadBalanced //负载均衡
  public RestTemplate getRestTemplate()
  {
   return new RestTemplate();
  }
  
  	@Bean
  	public IRule myRule(){
  		//return new RandomRule();//更换负载均衡算法为随机
  		return new RetryRule();//如果一个服务器挂掉,对这个挂掉的服务器访问三次要是还访问不行,三次以后就不访问了
  		
  	}
}
 
 

