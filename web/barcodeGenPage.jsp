<%--<%@  page contentType="text/html" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="f" %>

<%@ taglib uri="/WEB-INF/tlds/Reference.tld" prefix="r"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>--%>

<html>
    <head>
        <link rel="stylesheet" type="text/css" href="/bersd/newlook.css"/>
        <script type="text/javascript" src="/bersd/globalfunctions.js"></script>
		<script type="text/javascript" src="/bersd/ajaxBase.js"></script>
        <title>Barcode Generate Main Page</title>
		<style type="text/css">
			td { text-align: left; vertical-align: top; }
		</style>
    </head>
    <body>
		
		<br>
    <center>       <h1>
                   <%--<font color="#3CB371">--%>
                   <font color="#2F74D0">
                   Barcode Generate Main Page</font></h1> </center>
 	<center>
		<table border="0" cellspacing="5">
			<tr>
				<td>
					<%--<html:form method="post" action="/generateBarcodeServlet">
						<html:errors/>
						<div style="text-align: center; width: 420px">
							<fieldset>
								<legend><span class="pagetitle">Generate Barcode Only</span></legend>
								<table>
									<tr>
										<td class="fieldname">Barcode ID</td>
										<td class="tabledata" style="text-align: left;">
                                                                                <html:text size="10" maxlength="10" property="barcodeID"/>    
                                                                               	</td>
									</tr>
									<tr>
										<td colspan="2" style="text-align: center;">
											<html:submit styleClass="standardbutton" />
										</td>
									</tr>
								</table>
							</fieldset>
						</div>
					</html:form>--%>
                                        <table border="10" bordercolor="#2F74D0" cellpadding="10" cellspacing="5">
                                            <tr>
                                                <td>
                                         <h2><font color="#2F74D0">Generate Barcode Only</font></h2> 
                                         <form method="post" action="/GenerateBarcodes/upload">
                                            barcode ID:<br>
                                            <input type="text" name="barcodeID" value=""><br>
                                            <%--Member Number:<br>
                                            <input type="text" name="memberNum" value=""><br><br>--%>
                                            <input type="hidden" name="formInfo" value="NoForm"><br>
                                            File Format:<br>
                                             <select name="fileformat">
                                                <option value="pdf">PDF</option>
                                                <option value="png">PNG</option>
                                                <option value="JPEG">JPEG</option>
                                                <%--<option value="GIF">GIF</option>--%>
                                             </select> 
                                            <br><input type="submit" value="Submit">
                                         </form> 
                                                </td>
                                                
                                                <td>
                                         <h2><font color="#2F74D0">Generate Barcode Label</font></h2> 
                                         <form method="post" action="/GenerateBarcodes/barcodeLabel">
                                            Barcode ID:<br>
                                            <input type="text" name="barcodeContent" value=""><br>
                                            Barcode Description:<br>
                                            <input type="text" name="barcodeDesc" value=""><br>
                                            <input type="hidden" name="formInfo" value="label"><br>
                                            <br><input type="submit" value="Submit">
                                         </form> 
                                               </td>    
                                            </tr>
                                        </table>
                                         <br>
                                        <table border="10" bordercolor="#2F74D0" cellpadding="10" cellspacing="5"><tr><td> 
                                        <h2><font color="#2F74D0">Upload PDF Form and generate Barcode</font></h2> 
                                        <%--<form method="POST" action="upload" enctype="multipart/form-data" >--%>
                                        <form method="POST" action="upload" enctype="multipart/form-data" >
                                            
                                       <font color="#2F74D0"><b> Step 1 Upload PDF File : </b></font>
                                            <input type="file" name="file" id="file" /> <br>
                                            <%--Step 2 Enter Destination (location to store a copy of your PDF file and the final PDF file with barcode):
                                            <input type="text" value="" name="destination"/>
                                            </br>--%>
                                       <font color="#2F74D0"><b> Step 2 Enter barcode ID (required)/ Member Number(optional) </b></font><br>
                                            barcode ID:<br>
                                            <input type="text" name="barcodeID" value=""><br>
                                            Member Number:<br>
                                            <input type="text" name="memberNum" value=""><br>
                                            <input type="hidden" name="formInfo" value="HasForm">
                                        <font color="#2F74D0"><b>Step 3 Enter barcode position or use default location: </b></font> <br>
                                            lower left x &nbsp;
                                            <input type="text" name="llx" value="default"><br>
                                            lower left y &nbsp;
                                            <input type="text" name="lly" value="default"><br>
                                            upper right x
                                            <input type="text" name="urx" value="default"><br>
                                            upper right y
                                            <input type="text" name="ury" value="default"><br>
                                        <font color="#2F74D0"><b>Step 4 Enter your choice of Barcode Caption </b></font>
                                            <input type="radio" name="caption" value="yes"> Yes
                                            <input type="radio" name="caption" value="no" checked> No<br>
                                        <font color="#2F74D0"><b>Step 5 Enter number of pages </b></font>
                                            <input type="text" name="noofpage" value=""><br>
                                            <br><input type="submit" value="Upload File and generate Barcode" name="upload" id="upload" />
                                        </form>
				</td></tr></table>
				</td>
				<td>&nbsp;</td>
			</tr>
		</table>
	</center>

	
</body>
</html>
