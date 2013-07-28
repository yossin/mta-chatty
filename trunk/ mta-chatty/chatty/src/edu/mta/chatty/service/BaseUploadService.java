package edu.mta.chatty.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import edu.mta.chatty.Constants;
import edu.mta.chatty.bl.BL;
import edu.mta.chatty.util.IOHelper;

/**
 * Servlet implementation class UploadService
 */
public abstract class BaseUploadService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//final static private Logger logger = Logger.getLogger(BaseUploadService.class.getName());
	@Resource(name = Constants.DataSource)
	private DataSource ds;
    protected BL bl;
    @Override
    public void init() throws ServletException {
    	super.init();
		bl = new BL(ds);
    }
    

	
	protected void save(InputStream in, String fileName) throws IOException{
		FileOutputStream file = new FileOutputStream(fileName);
		IOHelper.copy(in, file);
	}
	
	protected String getId(HttpServletRequest request) throws Exception{
		String uri = request.getRequestURI();
		String id = uri.substring(uri.lastIndexOf("/")+1);
		return id;
	}
	protected String save(HttpServletRequest request, String id) throws Exception{
		String imageDirName =getServletContext().getRealPath("image");
		
        List<FileItem> items;
		items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
        for (FileItem item : items) {
            if (item.isFormField() == false) {
                String filename = item.getName();
                InputStream filecontent = item.getInputStream();
                String ext = filename.substring(filename.lastIndexOf(".")+1);
                String idImageName=String.format("image/%s.%s", id, ext);
        		String imageName = String.format("%s/%s.%s",imageDirName, id, ext);
                save(filecontent, imageName);
                // update bl with buddyImageName 
    			return idImageName;
            }
        }
        return null;
	}
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String id= getId(request);
			String imageName = save(request, id);
			perform(id, imageName);
		} catch (Exception e) {
			throw new ServletException("unable to save image",e);
		}	
			
	
	}
	
	protected abstract void perform(String id, String image) throws Exception;
	
}
