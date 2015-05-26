import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;


public class matlabControl {

	public static void main2(String[] args) throws MatlabConnectionException, MatlabInvocationException{
	    //Create a proxy, which we will use to control MATLAB
	    MatlabProxyFactory factory = new MatlabProxyFactory();
	    MatlabProxy proxy = factory.getProxy();

	    //Display 'hello world' just like when using the demo
	    proxy.eval("disp('hello world')");

	    //Disconnect the proxy from MATLAB
	    proxy.disconnect();
	}
	
}
