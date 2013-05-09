package edu.mta.chatty.bl;

public interface BLRequest<T,Z> {
	void validate(T t) throws IllegalArgumentException;
	Z perform(T t) throws Exception;
}
