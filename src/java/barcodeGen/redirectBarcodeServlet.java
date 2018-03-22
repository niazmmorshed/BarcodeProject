/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package barcodeGen;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import IDautomationPDFE.PDF417Encoder;
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

import java.io.File;

/**
 *
 * @author mting
 */
@WebServlet(name = "redirectBarcodeServlet", urlPatterns = {"/redirectBarcodeServlet"})
public class redirectBarcodeServlet extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //response.setContentType("text/html;charset=UTF-8");
        //PrintWriter out = response.getWriter();
        try {
            // TODO output your page here
            String path = request.getParameter("path");
            String fileName = request.getParameter("fileName");
            String barcodeID = request.getParameter("barcodeID");
            String memberNum = request.getParameter("memberNum");
                        
            
            //out.println("<html>");
            //out.println("<head>");
            //out.println("<title>Servlet redirectBarcodeServlet</title>");  
            //out.println("</head>");
            //out.println("<body>");
            //out.println("<h1>Servlet redirectBarcodeServlet at " + request.getContextPath () + " " + path + " " + fileName + "</h1>");
            //out.println("</body>");
            //out.println("</html>");
            
            String pdfString = path + File.separator + fileName;
            PdfReader reader = new PdfReader(pdfString);
            System.out.println("pdfString=" + pdfString);
            ByteArrayInputStream byteIS = null;
            ByteArrayOutputStream baos = null;    
            baos = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, baos);
                                
            String dest = path + File.separator + "ver2_" + fileName ;
            //MT July 10 2017 PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
                
            PdfWriter pwriter = stamper.getWriter();
                
            //int llx = 0;    //lower left x
            int llx = 26;    //lower left x
            //int lly = 0;    //lower left y
            int lly = 22;    //lower left y
            int urx = 230;  //upper right x
            int ury = 35;   //upper right y
            
            String llxStr = request.getParameter("llx");
            String llyStr = request.getParameter("lly");
            String urxStr = request.getParameter("urx");
            String uryStr = request.getParameter("ury");
            
            String noofpage = request.getParameter("noofpage");
            int npage = Integer.parseInt(noofpage);            
            //Integer.parseInt(request.getParameter("llx"));
            //check if user enter values for the coordinates
            llx = (java.util.regex.Pattern.matches("\\d+", llxStr)) ? Integer.parseInt(llxStr) : llx ;
            lly = (java.util.regex.Pattern.matches("\\d+", llyStr)) ? Integer.parseInt(llyStr) : lly ;
            urx = (java.util.regex.Pattern.matches("\\d+", urxStr)) ? Integer.parseInt(urxStr) : urx ;
            ury = (java.util.regex.Pattern.matches("\\d+", uryStr)) ? Integer.parseInt(uryStr) : ury ;            
            
            PDF417Encoder pdfe = new PDF417Encoder();
            //TextField barcodeField = new TextField(pwriter, new Rectangle(0, 0, 230, 30), "barcodeField"); 
            TextField barcodeField = new TextField(pwriter, new Rectangle(llx, lly, urx, ury), "barcodeField"); 
            
            String dashStr = memberNum.trim().equals("") ? "- - " : "-" ;
            barcodeField.setText(pdfe.fontEncode(barcodeID.trim() + dashStr + memberNum.trim()));
            
            System.out.println("barcodeField=" + barcodeField.toString());
            System.out.println("output=" + barcodeID + "-" + memberNum);
            final BaseFont bf = BaseFont.createFont( "E:\\Barcodes_BERS\\GenerateBarcodes\\web\\WEB-INF\\classes\\IDautomationPDF417n3.ttf", BaseFont.CP1252, BaseFont.EMBEDDED ); 
                                            
            barcodeField.setFont(bf);
            barcodeField.setFontSize(7);
            barcodeField.setAlignment(Element.ALIGN_LEFT);
            barcodeField.setOptions(TextField.READ_ONLY | TextField.MULTILINE);
            
            System.out.println("barcode get Text=" + barcodeField.getText());
            PdfFormField barcodeFormField = barcodeField.getTextField();
            for (int i = 1; i <= npage; i++)
            {
                stamper.addAnnotation(barcodeFormField, i);
            }    
            
            //Caption part
            
            if (request.getParameter("caption").equalsIgnoreCase("yes"))
            {    
                TextField captionField = new TextField(pwriter, new Rectangle(llx, lly-16, urx-80, ury-5), "captionField"); 
                String captionStr = barcodeID.trim() + dashStr + memberNum.trim();
                captionField.setText(captionStr);
                final BaseFont af = BaseFont.createFont( "C:\\Windows\\Fonts\\arial.ttf", BaseFont.CP1252, BaseFont.EMBEDDED ); 
                captionField.setFont(af);
                captionField.setFontSize(9);
                captionField.setAlignment(Element.ALIGN_CENTER);
                captionField.setOptions(TextField.READ_ONLY);
                PdfFormField captionFormField = captionField.getTextField();
                for (int c = 1; c <= npage; c++)
                {    
                    stamper.addAnnotation(captionFormField, c);
                }    
            }
                        
            AcroFields form = stamper.getAcroFields();
              
            //MT add to make sure no problem Jan 8 2014
	    stamper.setFormFlattening(true);
                
            stamper.close();
                
            System.out.println("Before MyByteArray");
            byte[] myByteArray = new byte[0];
            myByteArray = baos.toByteArray();
	    baos = null;
	    reader = null; 
            
            //For save file to dest location
            //public FileInputStream(String name) throws FileNotFoundException
            //public File(String pathname)
            
            //for screen output           
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
            
        } 
        catch (Exception e)         
        {
            System.out.println("Exception=" + e.toString());
        }    
        finally {            
            //out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
