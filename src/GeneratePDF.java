package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class GeneratePDF {
    private static final String FILES_FOLDER_PATH = "./data/";

    public void init() {
        File directory = new File(FILES_FOLDER_PATH);

        if(!directory.exists()) {
            directory.mkdir();
        }
    }

    public GeneratePDF() {
        init();
    }

    public int getNumbOfLine(String line) {
        int result;
        int maxCharByLine = 92;
        int lineLen = line.length();

        lineLen = lineLen == 0 ? 1 : lineLen;
        result = lineLen / maxCharByLine;
        result += lineLen % maxCharByLine > 0 ? 1 : 0;

        return result;
    }

    public File textToPDF(String fileName, String text, boolean translate) throws FileNotFoundException, DocumentException {
        File file = null;
        String fileFullName = FILES_FOLDER_PATH + fileName + ".pdf";
        Document document = new Document(PageSize.A4, 36, 36, 90, 36);
        PdfWriter writer;

        writer = PdfWriter.getInstance(document, new FileOutputStream(fileFullName));
        HeaderFooterPageEvent event = new HeaderFooterPageEvent();
        Font font1 = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
        Font font2 = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.RED);

        writer.setPageEvent(event);
        document.open();
        Paragraph para;

        int cptLine = 1, cptPara = 0;
        String[] lines = text.split("\n");

        for(int i = 0; i < lines.length; i++) {
            if(cptLine % 39 == 0) {
                document.newPage();
            } else {
                if(translate && !lines[i].equals("")) {
                    if(cptPara % 2 == 0) {
                        para = new Paragraph(lines[i], font1);

                    } else {
                        para = new Paragraph(lines[i],font2);
                    }

                    cptPara++;

                } else {

                    para = new Paragraph(lines[i]);
                }

                if(lines[i].equals("")) {
                    document.add(Chunk.NEWLINE);
                } else if (!lines[i].equals("" )) {
                    para.setAlignment(Element.ALIGN_CENTER);
                    document.add(para);
                }
            }

            cptLine += getNumbOfLine(lines[i]);
        }

        document.close();
        file = new File( fileFullName );
        return file;
    }

    public void getFile(String fileName) {
        File file = new File(fileName);

        if(file.exists())
            System.out.println("File exists");
        else
            System.out.println("file note exist");
    }

    public static void fileToPDF() {
        try {
            File file = new File(FILES_FOLDER_PATH + "lyrics");
            BufferedReader br = new BufferedReader(new FileReader(file));

            Document document = new Document(PageSize.A4, 36, 36, 90, 36);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(FILES_FOLDER_PATH + "iTextHelloWorld.pdf"));
            HeaderFooterPageEvent event = new HeaderFooterPageEvent();
            writer.setPageEvent(event);
            document.open();
            String st;
            Paragraph para;
            int cptLine = 1;

            try {
                while((st = br.readLine() ) != null) {
                    if(cptLine % 42 == 0) {
                        document.newPage();
                    } else {
                        para = new Paragraph(st);
                        para.setAlignment(Element.ALIGN_CENTER);
                        document.add(para);
                    }
                    cptLine++;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // document.newPage();
            // document.add( new Paragraph( "Adding a footer to PDF Document
            // using iText." ) );
            document.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /*public static void main( String[] args ) {
        // fileToPDF();
    }*/
}
