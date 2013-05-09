package edu.mta.chatty.bl;

public class BLExecuter<T,Z> {
	
	public Z execute(BLRequest<T,Z> request, T t) throws Exception{
		request.validate(t);
		return request.perform(t);
	}
	
}
