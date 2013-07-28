package edu.mta.chatty.service;


/**
 * Servlet implementation class UploadService
 */
public class UploadGroupImageService extends BaseUploadService {
	private static final long serialVersionUID = 1L;


	@Override
	protected void perform(String id, String image) throws Exception {
		bl.groups.updateImage(id, image);		
	}
    
	
	
}
