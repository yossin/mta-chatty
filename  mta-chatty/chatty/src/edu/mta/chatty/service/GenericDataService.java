package edu.mta.chatty.service;

import java.io.Writer;

import edu.mta.chatty.contract.GenericDataResponse;

public class GenericDataService extends BaseService<Void> {
	private static final long serialVersionUID = 1L;


	@Override
	protected Void create() {
		return null;
	}

	@Override
	protected void perform(Writer writer, Void t) throws Exception {
		GenericDataResponse data = bl.generic.getGenericData();
		writeJsonResponse(writer, data);
	}

}
