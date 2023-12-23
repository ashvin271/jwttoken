package com.jwtathentication.controller;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFPrintSetup;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jwtathentication.entity.User;
import com.jwtathentication.service.UserService;
import com.jwtathentication.utility.ResponseMessage;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.RGBColor;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

@RestController
@RequestMapping(value="/register") 
public class RegController {
	
	 private final Logger log = LoggerFactory.getLogger(RegController.class);
	
	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String home() {
		return "this is my home page";
	}
	
	@PostMapping("/save")
	public ResponseMessage<User> saveUser(@RequestBody User user) {
		return new ResponseMessage<>(HttpStatus.OK.value(), "Successfully Created",
				userService.saveUser(user));
	}
	
	 @GetMapping("/download-pdf")
	    public ResponseEntity<?> downloadPdf(HttpServletResponse resp) throws IOException {
	        
		 log.trace("This is a TRACE level message");
	        log.debug("This is a DEBUG level message");
	        log.info("This is an INFO level message");
	        log.warn("This is a WARN level message");
	        log.error("This is an ERROR level message");
		 try{
			 ByteArrayOutputStream outputStream = generatePdf();
		
	        InputStreamResource resource;
	        
	        PdfReader reader = new PdfReader(outputStream.toByteArray());
            var Pages = reader.getNumberOfPages();
            //MemoryStream ms = new MemoryStream();

            PdfStamper stamper = new PdfStamper(reader, outputStream);
            for (int i = 1; i <= Pages; i++)
            {
                PdfContentByte overContent;
                //Font Signature = FontFactory.getFont("Calibiri", 9, BaseFont.WINANSI, Color.BLACK);
                overContent = stamper.getOverContent(i);
                var helv = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
                overContent.saveState();
                overContent.beginText();
                overContent.setFontAndSize(helv, 10.0f);
                overContent.setColorFill(Color.lightGray);
                overContent.setTextMatrix(PageSize.LETTER.getWidth()  - 60, PageSize.LETTER.getHeight() - (PageSize.LETTER.getHeight() - 20));
               // overContent.set
                overContent.showText((i) + "/" + Pages);
                overContent.endText();
                overContent.restoreState();
            }
            stamper.close();            
            //return ms.ToArray();

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_PDF);
	        headers.setContentDispositionFormData("attachment", "document_with_header_footer.pdf");
	        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
	        resource = new InputStreamResource(inputStream);
	        return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
		 }catch(Exception e){
			 return new ResponseEntity<>("Not found", HttpStatus.BAD_REQUEST);
		 }
	       
	    }
	    
	    private ByteArrayOutputStream generatePdf() throws IOException {
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

	        Document document = new Document(PageSize.A4.rotate(),0,0,0,0);
	        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
	        
//	      
//	        HeaderFooter footer = new HeaderFooter(new Phrase(" "),false);
//            footer.setAlignment(Element.ALIGN_CENTER);
//            footer.setPadding(0);
//            footer.setBorder(0);
//            document.setFooter(footer);
            
           
          
            HeaderFooter1 header=new HeaderFooter1(writer,document);
            writer.setPageEvent(header);
            document.open();
            
        
            
            PdfContentByte canvas = writer.getDirectContent();
            canvas.rectangle(0,340, 500,200);
         
            canvas.setColorFill(Color.decode("#0000"));
            canvas.setColorStroke(RGBColor.green);
            canvas.stroke();
           
	      
	        Font font = new Font(Font.HELVETICA, 12, Font.HELVETICA, Color.BLACK);
	        PdfPTable table1 = new PdfPTable(1);
	        table1.setSpacingAfter(8);
	        table1.setWidthPercentage(90);
	        
	        PdfPCell cell1 = new PdfPCell(new Phrase("List of Batches",font));
	        cell1.setPadding(7);
	        cell1.setBorderColor(Color.lightGray);
		    cell1.setBackgroundColor(Color.lightGray);
	        table1.addCell(cell1);
	       
	        
	        
	        PdfPTable table3 = new PdfPTable(2);
		     table3.setWidthPercentage(90);
		     Font font1 = new Font(Font.HELVETICA, 10, Font.HELVETICA, Color.gray);
		     
		        Phrase phrase = new Phrase();
	            Chunk chunk1 = new Chunk("date ", font1);
	            Chunk chunk2 = new Chunk("DD/MM/YYYY", font);
	            phrase.add(chunk1);
	            phrase.add(chunk2);
	            PdfPCell cellOne1 = new PdfPCell();
	            cellOne1.addElement(phrase);
		        document.add(table1);
		     
		    // PdfPCell cellOne1 = new PdfPCell(new Phrase("date",font1));
		     table3.addCell(cellOne1);
		     
		     Phrase phrase1 = new Phrase();
	            Chunk chunk3 = new Chunk("total batches ", font1);
	            Chunk chunk4 = new Chunk("12", font);
	            phrase1.add(chunk3);
	            phrase1.add(chunk4);
	            PdfPCell cellTwo2 = new PdfPCell();
	            cellTwo2.addElement(phrase1);
		     //PdfPCell cellTwo2 = new PdfPCell(new Phrase("batches",font1));
		     table3.addCell(cellTwo2);
		     document.add(table3);
		   
		     if(document.getPageNumber()==0) {
		    	 PdfContentByte line =writer.getDirectContent();
		    	 line.moveTo(23, 723); // Start at the bottom-left corner
		    	 line.lineTo(566, 725); // Draw a line to the bottom-right corner
//		    	  cb.moveTo(document.left(), document.bottom()-10); // Start at the bottom-left corner
//	              cb.lineTo(document.right(), document.bottom()-10); // Draw a line to the bottom-right corner
		    	 line.stroke(); 
		     }
	           
		     PdfContentByte cb = writer.getDirectContent();
		     cb.saveState();
		     cb.setColorStroke(Color.black);
		     cb.stroke();
		     cb.restoreState();
	        
//	        Font pragraphFont = new Font(Font.HELVETICA, 10, Font.HELVETICA, Color.RED);
//	        String date="MM/DD/YYYY";
//	        int count=12;
//	        Paragraph paragraph=new Paragraph("Transaction Date "+date+"   "+"count "+count, pragraphFont);
//	        paragraph.setSpacingAfter(10);
//	        paragraph.setIndentationLeft(30);   // Left margin
//            paragraph.setIndentationRight(20);
//	        document.add(paragraph);
	       
//	      LineSeparator lineSeparator= new LineSeparator();
//	      lineSeparator.setLineColor(Color.LIGHT_GRAY);
//	      lineSeparator.setLineWidth(2f); // Set the line width in points
//          lineSeparator.setPercentage(92); // Set the line width as a percentage of the page width
//          //lineSeparator.setAlignment(Element.ALIGN_CENTER);
//          lineSeparator.setAlignment(Element.ANCHOR);
//
//          document.add(new Chunk(lineSeparator));
            
	     PdfPTable table = new PdfPTable(25);
	     float[] columnWidths = {1f, 2f, 2f,2f, 2f,2f, 2f,2f, 2f,2f, 2f,2f, 2f,2f, 2f,2f, 2f,2f, 2f,2f, 2f,2f, 2f,2f, 2f}; // Relative column widths
	     table.setWidths(columnWidths);
	     table.setWidthPercentage(90);
	     Font font3 = new Font(Font.TIMES_ROMAN, 2, Font.TIMES_ROMAN, Color.black);
	     
	     PdfPCell cellOne = new PdfPCell(new Phrase("Key",font3));
	     cellOne.setBackgroundColor(new Color(219, 213, 213));
	     cellOne.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	     cellOne.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	     
	     PdfPCell cellTwo = new PdfPCell(new Phrase("Value",font3));
	     cellTwo.setBackgroundColor(new Color(219, 213, 213));
	     cellTwo.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	     cellTwo.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	     
	     PdfPCell cellOne3 = new PdfPCell(new Phrase("Key",font3));
	     cellOne.setBackgroundColor(new Color(219, 213, 213));
	     cellOne.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	     cellOne.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	     
	     PdfPCell cellTwo4 = new PdfPCell(new Phrase("Value",font3));
	     cellTwo.setBackgroundColor(new Color(219, 213, 213));
	     cellTwo.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	     cellTwo.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	     PdfPCell cellOne5 = new PdfPCell(new Phrase("Key",font3));
	     cellOne.setBackgroundColor(new Color(219, 213, 213));
	     cellOne.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	     cellOne.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	     
	     PdfPCell cellTwo6 = new PdfPCell(new Phrase("Value",font3));
	     cellTwo.setBackgroundColor(new Color(219, 213, 213));
	     cellTwo.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	     cellTwo.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	     PdfPCell cellOne7 = new PdfPCell(new Phrase("Key",font3));
	     cellOne.setBackgroundColor(new Color(219, 213, 213));
	     cellOne.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	     cellOne.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	     
	     PdfPCell cellTwo8 = new PdfPCell(new Phrase("Value",font3));
	     cellTwo.setBackgroundColor(new Color(219, 213, 213));
	     cellTwo.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	     cellTwo.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	     PdfPCell cellOne9 = new PdfPCell(new Phrase("Key",font3));
	     cellOne.setBackgroundColor(new Color(219, 213, 213));
	     cellOne.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	     cellOne.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	     
	     PdfPCell cellTwo10 = new PdfPCell(new Phrase("Value",font3));
	     cellTwo.setBackgroundColor(new Color(219, 213, 213));
	     cellTwo.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	     cellTwo.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	     PdfPCell cellOne11 = new PdfPCell(new Phrase("Key",font3));
	     cellOne.setBackgroundColor(new Color(219, 213, 213));
	     cellOne.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	     cellOne.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	     
	     PdfPCell cellTwo12 = new PdfPCell(new Phrase("Value",font3));
	     cellTwo.setBackgroundColor(new Color(219, 213, 213));
	     cellTwo.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	     cellTwo.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	     PdfPCell cellOne13 = new PdfPCell(new Phrase("Key",font3));
	     cellOne.setBackgroundColor(new Color(219, 213, 213));
	     cellOne.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	     cellOne.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	     
	     PdfPCell cellTwo14 = new PdfPCell(new Phrase("Value",font3));
	     cellTwo.setBackgroundColor(new Color(219, 213, 213));
	     cellTwo.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	     cellTwo.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	     PdfPCell cellOne15 = new PdfPCell(new Phrase("Key",font3));
	     cellOne.setBackgroundColor(new Color(219, 213, 213));
	     cellOne.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	     cellOne.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	     
	     PdfPCell cellTwo16 = new PdfPCell(new Phrase("Value",font3));
	     cellTwo.setBackgroundColor(new Color(219, 213, 213));
	     cellTwo.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	     cellTwo.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	     PdfPCell cellOne17 = new PdfPCell(new Phrase("Key",font3));
	     cellOne.setBackgroundColor(new Color(219, 213, 213));
	     cellOne.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	     cellOne.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	     
	     PdfPCell cellTwo18 = new PdfPCell(new Phrase("Value",font3));
	     cellTwo.setBackgroundColor(new Color(219, 213, 213));
	     cellTwo.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	     cellTwo.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	     PdfPCell cellOne19 = new PdfPCell(new Phrase("Key",font3));
	     cellOne.setBackgroundColor(new Color(219, 213, 213));
	     cellOne.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	     cellOne.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	     
	     PdfPCell cellTwo20 = new PdfPCell(new Phrase("Value",font3));
	     cellTwo.setBackgroundColor(new Color(219, 213, 213));
	     cellTwo.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	     cellTwo.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	     PdfPCell cellOne21 = new PdfPCell(new Phrase("Key",font3));
	     cellOne.setBackgroundColor(new Color(219, 213, 213));
	     cellOne.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	     cellOne.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	     
	     PdfPCell cellTwo22 = new PdfPCell(new Phrase("Value",font3));
	     cellTwo.setBackgroundColor(new Color(219, 213, 213));
	     cellTwo.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	     cellTwo.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	     PdfPCell cellOne23 = new PdfPCell(new Phrase("Key",font3));
	     cellOne.setBackgroundColor(new Color(219, 213, 213));
	     cellOne.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	     cellOne.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	     
	     PdfPCell cellOne24 = new PdfPCell(new Phrase("Value",font3));
	     cellTwo.setBackgroundColor(new Color(219, 213, 213));
	     cellTwo.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	     cellTwo.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	     PdfPCell cellOne25 = new PdfPCell(new Phrase("Key",font3));
	     cellOne.setBackgroundColor(new Color(219, 213, 213));
	     cellOne.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	     cellOne.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	     
	    
	     
	     table.addCell(cellOne);
	     table.addCell(cellTwo);
	     table.addCell(cellOne3);
	     table.addCell(cellTwo4);
	     table.addCell(cellOne5);
	     table.addCell(cellTwo6);
	     table.addCell(cellOne7);
	     table.addCell(cellTwo8);
	     table.addCell(cellOne9);
	     table.addCell(cellTwo10);
	     table.addCell(cellOne11);
	     table.addCell(cellTwo12);
	     table.addCell(cellOne13);
	     table.addCell(cellTwo14);
	     table.addCell(cellOne15);
	     table.addCell(cellTwo16);
	     table.addCell(cellOne17);
	     table.addCell(cellTwo18);
	     table.addCell(cellOne19);
	     table.addCell(cellTwo20);
	     table.addCell(cellOne21);
	     table.addCell(cellTwo22);
	     table.addCell(cellOne23);
	     table.addCell(cellOne24);
	     table.addCell(cellOne25);

	     table.setHeaderRows(1);
	     for (int i = 0; i <168; i++) {
//	    	 PdfPCell newOne = new PdfPCell(new Phrase(i+""));
//	    	 newOne.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//	    	 newOne.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
//	    	 
//	    	 PdfPCell newtwo = new PdfPCell(new Phrase(i+""));
//	    	 newtwo.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//	    	 newtwo.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	    	 
	    	 table.addCell(i+"");
	    	 table.addCell(new Phrase("abcsdsdfsfdfds",font3));
	    	 table.addCell(new Phrase("abcsdsdfsfdfds",font3));
	    	 table.addCell(new Phrase("abcsdsdfsfdfds",font3));
	    	 table.addCell(new Phrase("abcsdsdfsfdfds",font3));
	    	 table.addCell(new Phrase("abcsdsdfsfdfds",font3));
	    	 
	    	 table.addCell(new Phrase("abcsdsdfsfdfds",font3));
	    	 table.addCell(new Phrase("abcsdsdfsfdfds",font3));
	    	 table.addCell(new Phrase("abcsdsdfsfdfds",font3));
	    	 table.addCell(new Phrase("abcsdsdfsfdfds",font3));
	    	 table.addCell(new Phrase("abcsdsdfsfdfds",font3));
	    	 
	    	 table.addCell(new Phrase("abcsdsdfsfdfds",font3));
	    	 table.addCell(new Phrase("abcsdsdfsfdfds",font3));
	    	 table.addCell(new Phrase("abcsdsdfsfdfds",font3));
	    	 table.addCell(new Phrase("abcsdsdfsfdfds",font3));
	    	 table.addCell(new Phrase("abcsdsdfsfdfds",font3));
	    	 
	    	 table.addCell(new Phrase("abcsdsdfsfdfds",font3));
	    	 table.addCell(new Phrase("abcsdsdfsfdfds",font3));
	    	 table.addCell(new Phrase("abcsdsdfsfdfds",font3));
	    	 table.addCell(new Phrase("abcsdsdfsfdfds",font3));
	    	 table.addCell(new Phrase("abcsdsdfsfdfds",font3));
	    	 
	    	 table.addCell(new Phrase("abcsdsdfsfdfds",font3));
	    	 table.addCell(new Phrase("abcsdsdfsfdfds",font3));
	    	 table.addCell(new Phrase("abcsdsdfsfdfds",font3));
	    	 table.addCell(new Phrase("abcsdsdfsfdfds",font3));
	    	 
		  }

	     table.setSpacingBefore(15f);
	    // table.setSpacingAfter(15f);
	     document.add(table);
	     document.add(new Paragraph(20));
	     
	     
	    // document.add(new Paragraph(new Chunk(new LineSeparator())));
	    // Font lastPageFont = new Font(Font.HELVETICA, 9, Font.HELVETICA, Color.RED);
//	     PageNumberEventHandler pageNumberEventHandler=new PageNumberEventHandler(writer,document);
//	     writer.setPageEvent(pageNumberEventHandler);
	     LastPageFooterImage lastPageFooterImage=new LastPageFooterImage(writer,document);
         writer.setPageEvent(lastPageFooterImage);
        // document.open();
	 	
	     document.close();
	     return outputStream;
	    }
	    
	    static class HeaderFooter1  extends PdfPageEventHelper {
	    	PdfWriter writer;
	    	Document document;
	    	public HeaderFooter1(PdfWriter writer, Document document){
	    		this.writer=writer;
	    		this.document=document;
	    	}
             
	    	@Override
	    	  public void onStartPage(PdfWriter writer, Document document) {
	    	    // add header image
	    		
	    	    try {
	    	    	
	    	    	Resource resource = new ClassPathResource("\\static\\image\\logo.png");
	    			File file = resource.getFile();
	    	      Image img = Image.getInstance(file.getPath());
	    	      img.scaleToFit(826,1091);
	    	      img.setWidthPercentage(100);
	    	      img.setSpacingBefore(10);
	    	      document.add(img);
	    	      PdfPTable table1 = new PdfPTable(1);
	  	          table1.setSpacingBefore(15);
	  	          PdfPCell cell1 = new PdfPCell();
	  	          cell1.setBorder(0);
	  	          table1.addCell(cell1);
	  	          document.add(table1);
	    	    } catch (Exception x) {
	    	      x.printStackTrace();
	    	    }

	    	  }
	    }
	    
      static class LastPageFooterImage  extends PdfPageEventHelper {
	    	
	    	PdfWriter writer;
	    	Document document;
	    	public LastPageFooterImage(PdfWriter writer, Document document){
	    		this.writer=writer;
	    		this.document=document;
	    	}
	    	
	    	@Override
	    	public void onEndPage(PdfWriter writer, Document document) {
	    		 try {
	    			
		    	    Resource resource = new ClassPathResource("\\static\\image\\logo.png");
		    		File file = resource.getFile();
		    	      Image img = Image.getInstance(file.getPath());
		    	      img.scaleToFit(826,37);
		    	      img.setAbsolutePosition(30, 30);
//		    	      img.setIndentationLeft(30);
//		    	      img.setIndentationRight(30);
		    	      document.add(img);
		    	   
		    	      PdfContentByte cb = writer.getDirectContent();
		    	      cb.setColorStroke(Color.PINK);
		              cb.setLineWidth(4f); // Set the line width as needed
		              cb.moveTo(document.left(), document.bottom()); // Start at the bottom-left corner
		              cb.lineTo(document.right(), document.bottom()); // Draw a line to the bottom-right corner
		              cb.stroke(); 

		    	    } catch (Exception x) {
		    	      x.printStackTrace();
		    	    }

	    	}
             
	    	
	    }
      
      @GetMapping("/download-excel")
      public ResponseEntity<?> getExcelSheet() {
    	  ByteArrayResource resource=null;
    	  HttpHeaders headers = new HttpHeaders();
	      //  headers.setContentType(MediaType.APPLICATION_PDF);
	        //headers.setContentDispositionFormData("attachment", "document_with_header_footer.pdf");
	      //  headers.setContentType("");
	       // headers.setHeader("Content-Disposition", "attachment; filename=data.xlsx");
	       
          try {
        	  headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
  	        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.xlsx");
  	      resource= new ByteArrayResource(getExcel());
  	        return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
          
      }
      
      public byte[] getExcel() throws IOException {
    	  ByteArrayOutputStream output= new ByteArrayOutputStream();
    	    // Create Excel workbook and sheet
          XSSFWorkbook workbook = new XSSFWorkbook();
          XSSFSheet sheet = workbook.createSheet("Data");
          
          XSSFPrintSetup printSetup = sheet.getPrintSetup();
          printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
         
         // Footer footer = sheet.getFooter();  
          //Header he=sheet.getHeader();
         // he.setCenter( "Page " + org.apache.poi.hssf.usermodel.HeaderFooter.page() + " of " + org.apache.poi.hssf.usermodel.HeaderFooter.numPages() );  
          
//          Footer footer =  workbook.getSheetAt(0).getFooter();
//
//          // Set the right section of the footer
//          footer.setRight( "Page " + org.apache.poi.hssf.usermodel.HeaderFooter.page() + " of " + org.apache.poi.hssf.usermodel.HeaderFooter.numPages() );
          sheet.addMergedRegion(new CellRangeAddress(2, // first row (0-based)
                  3, // last row (0-based)
                  1, // first column (0-based)
                  2 // last column (0-based)
          ));
          
          CreationHelper helper = workbook.getCreationHelper();
          Drawing<?> drawing = sheet.createDrawingPatriarch();
          ClientAnchor anchor = helper.createClientAnchor();
          anchor.setCol1(1); // Start column
          anchor.setRow1(2); // Start row
          anchor.setCol2(2); // End column
          anchor.setRow2(3); // End row
          anchor.setDx1(0);
          anchor.setDy1(0);
          anchor.setDx2(0);
          anchor.setDy2(0);

          // Add the image to the cell within the merged region
          Resource resource = new ClassPathResource("\\static\\image\\alogo.png");
  		  File file = resource.getFile();
          FileInputStream imageFile = new FileInputStream(file.getPath());
          byte[] bytes = IOUtils.toByteArray(imageFile);
          int pictureIdx = workbook.addPicture(bytes, workbook.PICTURE_TYPE_PNG);
          Picture picture = drawing.createPicture(anchor, pictureIdx);
          int desiredWidth = 2;
          int desiredHeight = 2;
          picture.resize(desiredWidth, desiredHeight);
        
          XSSFFont font = workbook.createFont();
          font.setBold(true);
          XSSFRow row1 = sheet.createRow(5);
          Cell cell = row1.createCell(1);
          
          RichTextString richText = workbook.getCreationHelper().createRichTextString("AXIS Bank Ltd \n\n Transaction Receipt \n corporate XXXXXXXX Limited <ABCD> \n Recipt Date :25-May-2021");
         
          // Create different font styles
          XSSFFont font1 = workbook.createFont();
          font1.setBold(true);
          font1.setFontHeightInPoints((short) 15);
          font1.setColor(IndexedColors.BLACK.getIndex()); // Font color black

          XSSFFont font2 = workbook.createFont();
          font2.setFontHeightInPoints((short) 11); 
          font2.setBold(true);
          font2.setColor(IndexedColors.BLACK.getIndex()); // Font color red

         
          richText.applyFont(0, 13, font1); 
          richText.applyFont(17, 39, font2); 
          //richText.applyFont(40, 39, font2); 
          cell.setCellValue(richText);

          sheet.addMergedRegion(new CellRangeAddress(5, // first row (0-based)
                  10, // last row (0-based)
                  1, // first column (0-based)
                  10 // last column (0-based)
          ));

          XSSFCellStyle celstyle=workbook.createCellStyle();
          celstyle.setFont(font);
          celstyle.setWrapText(true);
          cell.setCellStyle(celstyle);
         
          
          XSSFRow headerRow = sheet.createRow(12);
          int twips = 800; // Adjust this value as needed
          headerRow.setHeight((short) twips);
  
          
          CellStyle headerStyle=headerStyling(workbook);
          Cell headerCell1 = headerRow.createCell(1);
          headerCell1.setCellValue("Name");
          headerCell1.setCellStyle(headerStyle);
          Cell headerCell2 = headerRow.createCell(2);
          headerCell2.setCellValue("Age");
          headerCell2.setCellStyle(headerStyle);
          
          Cell headerCell3 = headerRow.createCell(3);
          headerCell3.setCellValue("class");
          headerCell3.setCellStyle(headerStyle);
          
          Cell headerCell4 = headerRow.createCell(3);
          headerCell4.setCellValue("time");
          headerCell4.setCellStyle(headerStyle);
          
          Cell headerCell5 = headerRow.createCell(4);
          headerCell5.setCellValue("class");
          headerCell5.setCellStyle(headerStyle);
          
          Cell headerCell6 = headerRow.createCell(5);
          headerCell6.setCellValue("time");
          headerCell6.setCellStyle(headerStyle);
          
          Cell headerCell7 = headerRow.createCell(6);
          headerCell7.setCellValue("checkin");
          headerCell7.setCellStyle(headerStyle);
          
          Cell headerCell8 = headerRow.createCell(7);
          headerCell8.setCellValue("checkOut");
          headerCell8.setCellStyle(headerStyle);
          
          Cell headerCell9 = headerRow.createCell(8);
          headerCell9.setCellValue("checkOut1");
          headerCell9.setCellStyle(headerStyle);
          
          Cell headerCell10 = headerRow.createCell(9);
          headerCell10.setCellValue("checkOut2");
          headerCell10.setCellStyle(headerStyle);
          
          Cell headerCell11 = headerRow.createCell(10);
          headerCell11.setCellValue("checkOut3");
          headerCell11.setCellStyle(headerStyle);

          // Create data rows
          int rowNum = 13;
          int cellHight = 999; // Adjust this value as needed
        
          CellStyle bStyle=bodyStyling(workbook);
          for (int i = 0; i < 50; i++) {
              XSSFRow bodyrow = sheet.createRow(rowNum++);
              bodyrow.setHeight((short) cellHight);
              Cell bodyCell1 = bodyrow.createCell(1);
              bodyCell1.setCellValue("Name");
              bodyCell1.setCellStyle(bStyle);
              Cell bodyCell2 = bodyrow.createCell(2);
              bodyCell2.setCellValue("Age");
              bodyCell2.setCellStyle(bStyle);
              
              Cell bodyCell3 = bodyrow.createCell(3);
              bodyCell3.setCellValue("class");
              bodyCell3.setCellStyle(bStyle);
              
              Cell bodyCell4 = bodyrow.createCell(3);
              bodyCell4.setCellValue("time");
              bodyCell4.setCellStyle(bStyle);
              
              Cell bodyCell5 = bodyrow.createCell(4);
              bodyCell5.setCellValue("class");
              bodyCell5.setCellStyle(bStyle);
              
              Cell bodyCell6 = bodyrow.createCell(5);
              bodyCell6.setCellValue("time");
              bodyCell6.setCellStyle(bStyle);
              
              Cell bodyCell7 = bodyrow.createCell(6);
              bodyCell7.setCellValue("checkin");
              bodyCell7.setCellStyle(bStyle);
              
              Cell bodyCell8 = bodyrow.createCell(7);
              bodyCell8.setCellValue("checkOut");
              bodyCell8.setCellStyle(bStyle);
              
              Cell bodyCell9 = bodyrow.createCell(8);
              bodyCell9.setCellValue("checkOut");
              bodyCell9.setCellStyle(bStyle);
              
              Cell bodyCell10 = bodyrow.createCell(9);
              bodyCell10.setCellValue("checkOut");
              bodyCell10.setCellStyle(bStyle);
              
              Cell bodyCell11 = bodyrow.createCell(10);
              bodyCell11.setCellValue("checkOut");
              bodyCell11.setCellStyle(bStyle);
          }
          
          
          XSSFRow bodyrow = sheet.createRow(rowNum+2);
          Cell footercell = bodyrow.createCell(1);
          footercell.setCellValue("Note: sasdadaddsfsdfsfdfsfsdfsdfdsfdsfdsfdsfdsfdsfsd");
          sheet.addMergedRegion(new CellRangeAddress(rowNum+2, // first row (0-based)
        		rowNum+2, // last row (0-based)
                1, // first column (0-based)
                10 // last column (0-based)
          ));
         
          // Set response headers
//          response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//          response.setHeader("Content-Disposition", "attachment; filename=data.xlsx");

          // Write the workbook to the response output stream
          try {
			workbook.write(output);
			workbook.close();
			return output.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bytes;
          
      }
//      
//      
//      private static void insertImageToCell(Workbook workbook, int rowNum, Drawing drawing) throws IOException {
//           
//          //Loading image from application resource
//    	  Resource resource = new ClassPathResource("\\static\\image\\alogo.png");
//  		  File file = resource.getFile();
//          FileInputStream imageFile = new FileInputStream(file.getPath());
//          byte[] bytes = IOUtils.toByteArray(imageFile);
//          int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
//   
//          ClientAnchor anchor = null;
//          anchor.setCol1(1);
//          anchor.setCol2(2);
//          anchor.setRow1(2);
//          anchor.setRow2(3);
//          drawing.createPicture(anchor, pictureIdx);
//           
//      }
//      
      public CellStyle headerStyling(XSSFWorkbook workbook) {
    	  CellStyle headerStyle = workbook.createCellStyle();
          headerStyle.setAlignment(HorizontalAlignment.CENTER);
          headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

          BorderStyle thickBorder = BorderStyle.THIN;
          short blackColorIndex = IndexedColors.BLACK.getIndex();

          headerStyle.setBorderTop(thickBorder);
          headerStyle.setBorderBottom(thickBorder);
          headerStyle.setBorderLeft(thickBorder);
          headerStyle.setBorderRight(thickBorder);

          XSSFFont headerFont = workbook.createFont();
          headerFont.setBold(true);
          headerFont.setColor(blackColorIndex);
          headerStyle.setFont(headerFont);
    	  return headerStyle;
      }
      
      
      public CellStyle bodyStyling(XSSFWorkbook workbook) {
    	  CellStyle headerStyle = workbook.createCellStyle();
          headerStyle.setAlignment(HorizontalAlignment.CENTER);
          headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

          BorderStyle thickBorder = BorderStyle.THIN;
          short blackColorIndex = IndexedColors.BLACK.getIndex();

          headerStyle.setBorderTop(thickBorder);
          headerStyle.setBorderBottom(thickBorder);
          headerStyle.setBorderLeft(thickBorder);
          headerStyle.setBorderRight(thickBorder);

          XSSFFont headerFont = workbook.createFont();
          headerFont.setColor(blackColorIndex);
          headerStyle.setFont(headerFont);
    	  return headerStyle;
      }
      
      
//      public void getExcel(HttpServletResponse response) throws IOException {
//    	  ByteArrayOutputStream steream=new ByteArrayOutputStream();
//    	  File file = new File("text.xlsx");
//          Workbook workbook = new Workbook(new FileOutputStream(file),"Application","1.0");
//          Worksheet worksheet = workbook.newWorksheet("Data");
//
//    
//
//          // Add the image to the worksheet
//          addImageToWorksheet(worksheet);
//
//          // Create cell styles
//         // Style headerStyle = workbook.newStyle().withFont(Font.boldSystemFont(15)).withColor(Color.BLACK).build();
//         //Style bodyStyle = workbook.newStyle().withFont(Font.systemFont(11)).withColor(Color.BLACK).build();
//
//          // Create headers
//          addHeaders(worksheet);
//
//          // Create data rows
//          addDataRows(worksheet);
//
//          // Set response headers
//          response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//          response.setHeader("Content-Disposition", "attachment; filename=data.xlsx");
//
//          // Write the workbook to the response output stream
//         
//          workbook.finish();
//          try (InputStream inputStream = new FileInputStream(file.getPath());
//                  OutputStream outputStream = response.getOutputStream()) {
//                 byte[] buffer = new byte[4096];
//                 int bytesRead;
//                 while ((bytesRead = inputStream.read(buffer)) != -1) {
//                     outputStream.write(buffer, 0, bytesRead);
//                 }
//             } catch (IOException e) {
//                 e.printStackTrace();
//                 response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//             }
//      }
//
//      private void addImageToWorksheet(Worksheet worksheet) throws IOException {
////    	  Resource resource = new ClassPathResource("\\static\\image\\alogo.png");
////  		  File file = resource.getFile();
////          ImageIcon image = new ImageIcon(file.getPath());
//    	  worksheet.range(2, 3, 1, 2);
//    	  
// 
//       
//      }
//      
//
//      private void addHeaders(Worksheet worksheet) {
//          worksheet.value(1, 1, "Name");
//          worksheet.value(1, 2, "Age");
//          worksheet.value(1, 3, "Class");
//          worksheet.value(1, 4, "Time");
//          worksheet.value(1, 5, "CheckIn");
//          worksheet.value(1, 6, "CheckOut");
//          worksheet.value(1, 7, "CheckOut1");
//          worksheet.value(1, 8, "CheckOut2");
//          worksheet.value(1, 9, "CheckOut3");
//      }
//
//      private void addDataRows(Worksheet worksheet) {
//          int rowNum = 2;
//          int cellHeight = 999; // Adjust as needed
//
//          for (int i = 0; i < 50; i++) {
//              worksheet.value(rowNum, 1, "abcs");
//              worksheet.value(rowNum, 2, "22");
//              worksheet.value(rowNum, 3, "ssd");
//              worksheet.value(rowNum, 4, "11.00");
//              worksheet.value(rowNum, 5, "9.00");
//              worksheet.value(rowNum, 6, "6.00");
//              worksheet.value(rowNum, 7, "CheckOut1");
//              worksheet.value(rowNum, 8, "CheckOut2");
//              worksheet.value(rowNum, 9, "CheckOut3");
//
//              //worksheet.height(rowNum, cellHeight); // Set row height
//
//              rowNum++;
//          }
//      }
      
      

@RequestMapping("/changelevel")
   public String changeLogLevel(@RequestParam String loggerName, @RequestParam String level){
       LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
       ch.qos.logback.classic.Logger logger = loggerName.equalsIgnoreCase("root")?
               loggerContext.getLogger(loggerName):loggerContext.exists(loggerName);
       if( logger !=null){
           logger.setLevel(Level.toLevel(level));
           return "Changed logger: "+loggerName+" to level : "+level;
       } else {
           return "Logger Not Found Make Sure that logger name is correct";
       }
   }    
    
     
	    
}
