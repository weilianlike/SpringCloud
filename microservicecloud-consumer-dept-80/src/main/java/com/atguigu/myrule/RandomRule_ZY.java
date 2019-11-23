 
package com.atguigu.myrule;
 
import java.util.List;
import java.util.Random;
 
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;


/*
 * 自定义负载均衡算法
 * 每个服务器调五次
 * 
 * */
public class RandomRule_ZY extends AbstractLoadBalancerRule {
 
// total = 0 //当total = 5 以后 我们指针才能往下走
	//index = 0  //当前对外提供服务的服务器地址
		//total 需要重置归为0,但是已经达到过一个5次,所以我们index(服务器) = 1
			// 当index大于3,重置归0  因为我们只有三台服务器
  private int total = 0;    //总共被调用的次数，目前要求每台被调用5次
  private int currentIndex = 0;//当前提供服务的机器号
  
    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            return null;
        }
        Server server = null;
 
        while (server == null) {
            if (Thread.interrupted()) {
                return null;
            }
            List<Server> upList = lb.getReachableServers();
            List<Server> allList = lb.getAllServers();
 
            int serverCount = allList.size();
            if (serverCount == 0) {
                /*
                 * No servers. End regardless of pass, because subsequent passes
                 * only get more restrictive.
                 */
                return null;
            }
 
            
//            int index = rand.nextInt(serverCount);
//            server = upList.get(index);
            
            /*
             * 
             * 
             * */
            if(total < 5)
            {
            server = upList.get(currentIndex);//当前server是0号机
            total++;
            }else {
            total = 0;
            currentIndex++;
            if(currentIndex >= upList.size())//服务机是否大于服务机数
            {
              currentIndex = 0;
            }
            
            }
            
            
            
 
            if (server == null) {
                /*
                 * The only time this should happen is if the server list were
                 * somehow trimmed. This is a transient condition. Retry after
                 * yielding.
                 */
                Thread.yield();
                continue;
            }
 
            if (server.isAlive()) {
                return (server);
            }
 
            // Shouldn't actually happen.. but must be transient or a bug.
            server = null;
            Thread.yield();
        }
 
        return server;
 
    }
 
  @Override
  public Server choose(Object key) {
   return choose(getLoadBalancer(), key);
  }
 
  @Override
  public void initWithNiwsConfig(IClientConfig clientConfig) {
   
  }
}
 

