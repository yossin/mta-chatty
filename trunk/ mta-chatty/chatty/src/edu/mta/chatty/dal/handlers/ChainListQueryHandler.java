package edu.mta.chatty.dal.handlers;

public interface ChainListQueryHandler<T> {
	ListQueryHandler<T> handler();
	<N>ChainListQueryHandler<N> next();
}
