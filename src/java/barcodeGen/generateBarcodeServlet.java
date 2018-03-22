package barcodeGen;

import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
//import net.sf.jasperreports.engine.JasperPrint;
//import org.apache.log4j.Logger;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
//import java.io.ByteArrayOutputStream;
//import java.util.Map;
//import oracle.jdbc.*;
import java.sql.*;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;

import java.util.HashMap;
import java.util.Map;

import IDautomationPDFE.PDF417Encoder;

//import IDautomationPDFE.PDF417Encoder;

import java.util.ArrayList;

import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfFormField;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.TextField;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.BaseField;

import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.servlet.http.Part;
import javax.servlet.ServletException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.File;


import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.fontbox.FontBoxFont;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;

import javax.servlet.ServletContext;

/**
 * Servlet that generate barcode in PDF file
 */
@WebServlet(name = "generateBarcodeServlet", urlPatterns = {"/upload"})
@MultipartConfig
public class generateBarcodeServlet
	extends HttpServlet
{
	public generateBarcodeServlet()
	{
		super();
	}

	 
        protected void processLabelRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
            //generate barcode label
            
            Connection conn = null;
            String barcodeContent = request.getParameter("barcodeContent");
            String barcodeDesc = request.getParameter("barcodeDesc");
            System.out.println("barcodeContent=" + barcodeContent);
            try
		{
                    
                       String rptType = "D:\\GenerateBarcodes_CopyFromMyComputer_082117\\GenerateBarcodes_CopyFromMyComputer_082117\\web\\WEB-INF\\barcodeReports\\BarcodeLabelReport.jasper";
                       Map<String, Object> params = new HashMap<String, Object>();
                       params.put("barcodeContent", barcodeContent);
                       params.put("barcodeDesc", barcodeDesc);
                       System.out.println("before fillReport");   
                       JasperPrint jp = JasperFillManager.fillReport(rptType, params, conn); 
                       System.out.println("after fillReport");   
		       byte[] pdfasbytes = JasperExportManager.exportReportToPdf(jp);
		       response.setContentType("application/pdf");
		       response.setContentLength(pdfasbytes.length);
		       response.setHeader("Content-disposition", "inline; filename=\"report.pdf\"");
                       response.setIntHeader("Refresh", 10);
                                               
                       ByteArrayInputStream byteIS = new ByteArrayInputStream(pdfasbytes);
                       
                       System.out.println("after byteIS");   
                        
                       byte[] buf = new byte[10240];
	               int nRead = 0;
	                 
	               response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	               response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	                 
	               //Send the Byte Array to the Client
	               while((nRead = byteIS.read(buf)) != -1)
	               {
	                     response.getOutputStream().write(buf, 0, nRead);
	               }
                       System.out.println("After ByteIS read");    
	               //Flush the output stream
	               response.getOutputStream().flush();
	               //Close the output stream
	               response.getOutputStream().close();
                       
                       System.out.println("end try");   
                      
		}
            catch(Exception e)
            {
                System.out.println(e.toString());
            }    
            finally {
               try {  
                    if (conn != null)
		    {
                         conn.close();
                         conn = null;
                    }
               } catch (Exception ex)  
               {
                      
               } 
                  
            }
        } 
        
        protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
            response.setContentType("text/html;charset=UTF-8");

            // Create path components to save the file
            //MT july 11 2017 final String path = request.getParameter("destination");
            final String path = "E:\\TestUploadPDF_ForBarcode_070617";
            final Part filePart = request.getPart("file");
            final String fileName = getFileName(filePart);
            
            String barcodeID = request.getParameter("barcodeID");
            String memberNum = request.getParameter("memberNum");       
            
            if (request.getParameter("barcodeID") == null)
            {
                System.out.println("Test july 112017 barocdeID is null");
            }    
            else
            {
                System.out.println("Test July 11 2017 barcodeID is NOT null");
            }   
            System.out.println("July 11 2017 barcodeID=" + barcodeID);
            String llx = request.getParameter("llx");
            String lly = request.getParameter("lly");
            String urx = request.getParameter("urx");
            String ury = request.getParameter("ury");
            
            String noofpage = request.getParameter("noofpage");
            
            String caption = request.getParameter("caption");
            if (caption == null)
            {
                System.out.println("caption is NULL");
                
            }
            else
            {
                System.out.println("caption is NOT NULL");
            }    
            System.out.println("caption=" + caption);
            
            System.out.println("path=" + path);
            System.out.println("fileName=" + fileName);
            
            OutputStream out = null;
            InputStream filecontent = null;
            System.out.println("Debug Before");
            PrintWriter writer = response.getWriter();
            System.out.println("Debug After");

            try {
                System.out.println("Debug 1");
                out = new FileOutputStream(new File(path + File.separator + fileName));
                System.out.println("Debug 2");
                filecontent = filePart.getInputStream();
                System.out.println("out=" + out.toString());
                int read = 0;
                final byte[] bytes = new byte[1024];

                while ((read = filecontent.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                writer.println("New file " + fileName + " created at " + path);
                System.out.println("New file " + fileName + " created at " + path);
                
                String paramStr = "?path=" + path + "&fileName=" + fileName + "&barcodeID=" + barcodeID + "&memberNum=" + memberNum + "&llx=" + llx + "&lly=" + lly + "&urx=" + urx + "&ury=" + ury + "&caption=" + caption + "&noofpage=" + noofpage;
                response.sendRedirect(request.getContextPath() + "/redirectBarcodeServlet" + paramStr);
                
                //MT July 10 2017
                //writer = null;
                //LOGGER.log(Level.INFO, "File{0}being uploaded to {1}", new Object[]{fileName, path});
                
                //MT July 7 2017 
                /*
                 * get the newly created pdf file(PDF FORM) PdfReader(src)
                 * get a stamper that will take this PDF Form and add the barcode to it and output the file to the same path
                 * may need to solve the 2 page problem
                */
                /*String pdfString = path + File.separator + fileName;
                PdfReader reader = new PdfReader(pdfString);
                System.out.println("pdfString=" + pdfString);
                ByteArrayInputStream byteIS = null;
                ByteArrayOutputStream baos = null;    
                baos = new ByteArrayOutputStream();
                PdfStamper stamper = new PdfStamper(reader, baos);
                                
                String dest = path + File.separator + "ver2_" + fileName ;
                //MT July 10 2017 PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
                
		
                
                PdfWriter pwriter = stamper.getWriter();
                
                PDF417Encoder pdfe = new PDF417Encoder();
                //TextField barcodeField = new TextField(pwriter, new Rectangle(36, 760, 144, 790), "barcodeField");
                TextField barcodeField = new TextField(pwriter, new Rectangle(0, 0, 230, 30), "barcodeField"); 
                barcodeField.setText(pdfe.fontEncode("TF-5678"));
                System.out.println("barcodeField=" + barcodeField.toString());
                //final BaseFont bf = BaseFont.createFont( "E:\\Barcodes_BERS\\GenerateBarcodes\\web\\WEB-INF\\classes\\IDautomationPDF417n3.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED ); 
                final BaseFont bf = BaseFont.createFont( "E:\\Barcodes_BERS\\GenerateBarcodes\\web\\WEB-INF\\classes\\IDautomationPDF417n3.ttf", BaseFont.CP1252, BaseFont.EMBEDDED ); 
                //Font bf = FontFactory.getFont("E:\\Barcodes_BERS\\GenerateBarcodes\\web\\WEB-INF\\classes\\IDautomationPDF417n3.ttf", BaseFont.CP1252, true, 8);
                
                //barcodeField.setOptions(TextField.MULTILINE);
                //public boolean setFieldProperty(java.lang.String field,java.lang.String name, int value, int[] inst)
               
                barcodeField.setFont(bf);
                barcodeField.setFontSize(8);
                
                //barcodeField.setAlignment(Element.ALIGN_LEFT);
                barcodeField.setAlignment(Element.ALIGN_CENTER);
                //barcodeField.setAlignment(Element.ALIGN_CENTER | Element.ALIGN_BOTTOM);
                //barcodeField.setAlignment(Element.ALIGN_BOTTOM);
                
                //barcodeField.setOptions(TextField.MULTILINE);
                barcodeField.setOptions(TextField.READ_ONLY | TextField.MULTILINE);
                
                System.out.println("barcode get Text=" + barcodeField.getText());
                PdfFormField barcodeFormField = barcodeField.getTextField();
     
                stamper.addAnnotation(barcodeFormField, 1);
                //form.setFieldProperty("f", "fflags", PdfFormField.FF_MULTILINE, null);
                AcroFields form = stamper.getAcroFields();
                //form.setFieldProperty("barcodeField", "setfflags", PdfFormField.FF_MULTILINE, null);
		//form.setField("barcodeFormField", pdfe.fontEncode("TF-5678"));
                
                //MT add to make sure no problem Jan 8 2014
		stamper.setFormFlattening(true);
                
                stamper.close();
                
                System.out.println("Before MyByteArray");
                byte[] myByteArray = new byte[0];
                myByteArray = baos.toByteArray();
		baos = null;
		reader = null; 
                       
                byteIS = new ByteArrayInputStream(myByteArray);
            
		byte[] buf = new byte[10240];
		int nRead = 0;
		response.setContentType("application/pdf");
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setHeader("Content-disposition", "inline; filename=\"report.pdf\"");
		//need to set to the final byte array length
		response.setContentLength(myByteArray.length);
                
                System.out.println("before while byteIS read");
                
		//Send the Byte Array to the Client
		while((nRead = byteIS.read(buf)) != -1)
		{
		   response.getOutputStream().write(buf, 0, nRead);
		}
                System.out.println("After while byteIS read");
		//Flush the output stream
		response.getOutputStream().flush();
		//Close the output stream
		response.getOutputStream().close();
                
                System.out.println("End try before catch");
              */  
            } catch (FileNotFoundException fne) {
                writer.println("You either did not specify a file to upload or are "
                + "trying to upload a file to a protected or nonexistent "
                + "location.");
                writer.println("<br/> ERROR: " + fne.getMessage());
                System.out.println("inside FileNotFoundException");
                //LOGGER.log(Level.SEVERE, "Problems during file upload. Error: {0}", new Object[]{fne.getMessage()});
            } 
            catch (Exception e)        
            {
                System.out.println("Exception=" + e.toString());
            }    
            finally {
                if (out != null) {
                  out.close();
                }
                if (filecontent != null) {
                  filecontent.close();
                }
                if (writer != null) {
                  writer.close();
                }
            }
      }

      private String getFileName(final Part part) {
         final String partHeader = part.getHeader("content-disposition");
        // LOGGER.log(Level.INFO, "Part Header = {0}", partHeader);
         for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring( content.indexOf('=') + 1).trim().replace("\"", "");
            }
         }
         return null;
      }

      
        @Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException
	{
		doPost(request, response);
	}  
      
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws IOException
	{
		//ServletOutputStream outstream = response.getOutputStream();
                 ServletOutputStream outstream = null;
                Connection conn = null;
                
		try
		{
	               //DriverManager.registerDriver((new oracle.jdbc.driver.OracleDriver()));
                       
                       //conn = DriverManager.getConnection( "jdbc:oracle:thin:@10.10.136.28:1521:PRUTSTW", "MIRANDA", "newOppVal");	
                       
                       //MT July 5 2017 Generate Barcode using Jasper Report 
                       /*String rptType = "E:\\Barcodes_BERS\\GenerateBarcodes\\web\\WEB-INF\\barcodeReports\\barcode_generate.jasper";
                       //Map<String, Object> parameters = null;  
                       Map<String, Object> params = new HashMap<String, Object>();
                       params.put("barcodeID", "QF1234- -");
                       params.put("spaceStr", " ");
                       JasperPrint jp = JasperFillManager.fillReport(rptType, params, conn); 
                       
		       byte[] pdfasbytes = JasperExportManager.exportReportToPdf(jp);
		       response.setContentType("application/pdf");
		       response.setContentLength(pdfasbytes.length);
		       response.setHeader("Content-disposition",
				"inline; filename=\"report.pdf\"");
		       outstream.write(pdfasbytes);
                       */
                       //MT July 6 2017 , first see which form the data come from
                       
                       String myFormInfo = request.getParameter("formInfo");
                       String fileformat = request.getParameter("fileformat");
                       System.out.println("fileformat=" + fileformat);
                       System.out.println("myFormInfo=" + myFormInfo);                               
                       
                        
                       if (myFormInfo.equalsIgnoreCase("NoForm"))        
                       {    
                       System.out.println("inside NoForm");    
                       //MT July 14 2017
                       if (fileformat.equalsIgnoreCase("pdf"))  
                       { 
                       //July 5 2017 Generate Barcode using Acrobat and iText
                       PdfReader reader = null;
		       PdfStamper stamper;
		       byte[] myByteArray = new byte[0];

		       ByteArrayInputStream byteIS = null;
                       
                       PDF417Encoder pdfe = new PDF417Encoder();
		       String pdfString = "E:\\Barcodes_BERS\\GenerateBarcodes\\web\\WEB-INF\\classes\\barcodeTemplate.pdf";
                       
                       
                       ByteArrayOutputStream baos = null;
                       reader = new PdfReader(pdfString);
		       baos = new ByteArrayOutputStream();
		       stamper = new PdfStamper(reader, baos);
		       AcroFields form = stamper.getAcroFields();
                       
                       
                       String dataToEncode = request.getParameter("barcodeID");
                       System.out.println("dataToEncode=" + dataToEncode);
		       form.setField("FormBarCode", pdfe.fontEncode(dataToEncode.trim().concat("- - ")));
                       //form.setField("FormBarCode", pdfe.fontEncode(myFormInfo.trim().concat("- - ")));
		       form.setField("FormBarCodeCaption", dataToEncode.trim().concat("- - "));
                       //form.setField("FormBarCodeCaption", myFormInfo.trim().concat("- - "));
		       form.setFieldProperty("FormBarCodeCaption", "setfflags", PdfFormField.FF_READ_ONLY, null);
                       System.out.println("after setField property");
                       //MT add to make sure no problem Jan 8 2014
		       stamper.setFormFlattening(true);

		       stamper.close();
		       myByteArray = baos.toByteArray();
		       baos = null;
		       reader = null; 
                       
                       byteIS = new ByteArrayInputStream(myByteArray);

		       byte[] buf = new byte[10240];
		       int nRead = 0;
		       response.setContentType("application/pdf");
                       //response.setContentType("image/png");
		       response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		       response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		       response.setHeader("Content-disposition", "inline; filename=\"report.pdf\"");
		       //need to set to the final byte array length
		       response.setContentLength(myByteArray.length);

		       //Send the Byte Array to the Client
                       System.out.println("before response");
		       while((nRead = byteIS.read(buf)) != -1)
		       {
			    response.getOutputStream().write(buf, 0, nRead);
		       }
                       System.out.println("after response");
		       //Flush the output stream
		       response.getOutputStream().flush();
		       //Close the output stream
		       response.getOutputStream().close();
                       } //pdf
                       //else if (fileformat.equalsIgnoreCase("png") || fileformat.equalsIgnoreCase("JPEG") || fileformat.equalsIgnoreCase("GIF")) 
                       else if (fileformat.equalsIgnoreCase("png") || fileformat.equalsIgnoreCase("JPEG") ) 
                       {
                            //MT 07 18 2017 need to generate the pdf before convert to png
                            PdfReader reader = null;
                            PdfStamper stamper;
                            //byte[] myByteArray = new byte[0];

		            //ByteArrayInputStream byteIS = null;
                       
                            PDF417Encoder pdfe = new PDF417Encoder();
		            String pdfString = "E:\\Barcodes_BERS\\GenerateBarcodes\\web\\WEB-INF\\classes\\barcodeTemplate_LargeFont.pdf";
                       
                       
                            //ByteArrayOutputStream baos = null;
                            reader = new PdfReader(pdfString);
		            //baos = new ByteArrayOutputStream();
		            //stamper = new PdfStamper(reader, baos);
                            String dataToEncode = request.getParameter("barcodeID");
                            
                            String pdfDir = "E:\\TestPNG_July2017\\";
                            File f = new File(pdfDir + dataToEncode + "_071817.pdf");
                            FileOutputStream os = new FileOutputStream(f);
                            stamper = new PdfStamper(reader, os);
		            AcroFields form = stamper.getAcroFields();
                       
                       
                            
                            System.out.println("dataToEncode=" + dataToEncode);
		            form.setField("FormBarCode", pdfe.fontEncode(dataToEncode.trim().concat("- - ")));
                            //form.setField("FormBarCode", pdfe.fontEncode(myFormInfo.trim().concat("- - ")));
		            form.setField("FormBarCodeCaption", dataToEncode.trim().concat("- - "));
                            //form.setField("FormBarCodeCaption", myFormInfo.trim().concat("- - "));
		            form.setFieldProperty("FormBarCodeCaption", "setfflags", PdfFormField.FF_READ_ONLY, null);
                            System.out.println("after setField property");
                            //MT add to make sure no problem Jan 8 2014
		            //MT July 20 2017 stamper.setFormFlattening(true);

		            stamper.close();
		            //myByteArray = baos.toByteArray();
		            //baos = null;
		            reader = null; 
                       
                            //byteIS = new ByteArrayInputStream(myByteArray);
                           
                            System.out.println("inside png");
                            //Loading an existing PDF document
                            //File file = new File("E:\\TestPNG_July2017\\TF2204_071417.pdf");
                            //PDDocument document = PDDocument.load(file);
                            PDDocument document = PDDocument.load(f);
                            System.out.println("after load file");
                            //Instantiating the PDFRenderer class
                            PDFRenderer renderer = new PDFRenderer(document);
                            System.out.println("after renderer");
                            //Rendering an image from the PDF document
                            BufferedImage image = null;
                            try {
                                System.out.println("before renderImage");
                                //MT July 21 2017 image = renderer.renderImage(0);
                                image = renderer.renderImageWithDPI(0, 1200);
                                System.out.println("after renderImage");
                            } 
                            catch(Exception e)
                            {
                                System.out.println("BufferedImage Exception=" + e.toString());
                            }    
                            System.out.println("after BUfferedImage");
                            //Writing the image to a file
                            //MT July 17 2017 ImageIO.write(image, "PNG", new File("E:\\TestPNG_July2017\\TF2204_071417.png"));
                            String filename = "";
                            if (fileformat.equalsIgnoreCase("png"))    
                            {    
                                ImageIO.write(image, "PNG", new File("E:\\TestPNG_July2017\\TF2204_071817.png"));
                                filename = "E:\\TestPNG_July2017\\TF2204_071817.png";
                            }
                            else if (fileformat.equalsIgnoreCase("JPEG"))
                            {
                                ImageIO.write(image, "JPEG", new File("E:\\TestPNG_July2017\\TF2204_071817.jpg"));
                                filename = "E:\\TestPNG_July2017\\TF2204_071817.jpg";
                            }    
                            //else if (fileformat.equalsIgnoreCase("GIF"))
                            //{
                            //    ImageIO.write(image, "GIF", new File("E:\\TestPNG_July2017\\TF2204_071817.gif"));
                            //    filename = "E:\\TestPNG_July2017\\TF2204_071817.gif";
                            //}
                            
                            //  fileformat.equalsIgnoreCase("GIF")
                            //.write(image, "JPEG", new File("E:\\TestPNG_July2017\\TF2204_071417.jpg"));
                            
                            String[]  readNames = ImageIO.getReaderFormatNames();
                            String[]  writeNames = ImageIO.getWriterFormatNames();
                            
                            System.out.println("readNames= ");
                            for (String s: readNames) {           
                                System.out.println(s); 
                                System.out.println(" "); 
                            }
                            System.out.println("\n");
                            

                            System.out.println("writeNames= ");
                            for (String s: writeNames) {           
                                System.out.println(s); 
                                System.out.println(" "); 
                            }
                            System.out.println("\n");
   
                            System.out.println("after ImageIO"); 
                            System.out.println("Image created");
       
                            //Closing the document
                            document.close();  
                            //Now output the png file to screen
                            
                            
                            ServletContext cntx= request.getServletContext();
                            // Get the absolute path of the image
                            //String filename = cntx.getRealPath("E:\\TestPNG_July2017\\TF2204_071417.png");
                            //String filename = "E:\\TestPNG_July2017\\TF2204_071817.gif";
                            // retrieve mimeType dynamically
                            String mime = cntx.getMimeType(filename);
                            if (mime == null) {
                                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                                return;
                            }

                            response.setContentType(mime);
                            File file = new File(filename);
                            response.setContentLength((int)file.length());

                            FileInputStream in = new FileInputStream(file);
                            OutputStream out = response.getOutputStream();

                            // Copy the contents of the file to the output stream
                            byte[] buf = new byte[1024];
                            int count = 0;
                            while ((count = in.read(buf)) >= 0) {
                                out.write(buf, 0, count);
                            }
                            out.close();
                            in.close();
                                                       
                            
                            
                       }//png
                       
                       } //no form
                       else if (myFormInfo.equalsIgnoreCase("HasForm"))
                       {
                          System.out.println("inside HasForm"); 
                          processRequest(request, response);
                       } //has form   
                       else if (myFormInfo.equalsIgnoreCase("label"))
                       {
                           System.out.println("Inside label");   
                           processLabelRequest(request, response);
                       }// generate barcode label    
                       
			
                       
		}
		//catch(ReportException re)
		//{
			//log.error(re);
		//	outstream.println(re.getMessage());
		//}
		catch(ClassCastException cce)
		{
			//log.error(cce);
			outstream.println(cce.getMessage());
		}
		catch (Exception e)        
                {
                    System.out.println("Exception=" + e.toString());
                }
                finally {
                  try {  
                    if (conn != null)
		    {
                         conn.close();
                         conn = null;
                    }
                  } catch (Exception ex)  
                  {
                      
                  }    
                }
	}

	
}

