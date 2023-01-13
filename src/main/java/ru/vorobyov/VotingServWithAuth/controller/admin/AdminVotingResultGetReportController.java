package ru.vorobyov.VotingServWithAuth.controller.admin;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocument1;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.vorobyov.VotingServWithAuth.entities.Vote;
import ru.vorobyov.VotingServWithAuth.entities.Voting;
import ru.vorobyov.VotingServWithAuth.services.interfaces.VoteService;
import ru.vorobyov.VotingServWithAuth.services.interfaces.VotingService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Controller
public class AdminVotingResultGetReportController {
    private final VotingService votingService;
    private final VoteService voteService;

    private int count;

    public AdminVotingResultGetReportController(VotingService votingService, VoteService voteService) {
        this.votingService = votingService;
        this.voteService = voteService;
    }

    @RequestMapping(value = "admin/voting/result/report", method= RequestMethod.GET)
    public ResponseEntity<?> zipFiles() throws IOException, InvalidFormatException {

        for (Voting voting : votingService.findAll()){
            try {
                printBilluten(voting);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            printReport(voting);
        }

        //simple file list
        ArrayList<XWPFDocument> documents = new ArrayList<>();
        File dir = new File("documentsResult/");
        File f = new File("ResultFile.docx");

        for (File file : Objects.requireNonNull(dir.listFiles())){
            String path = file.getPath();
            documents.add(new XWPFDocument(OPCPackage.open(Files.newInputStream(new File(path).toPath()))));
        }

        XWPFDocument doc = mergeDocuments(documents.stream());
        String path = "ResultFile.docx";
        doc.write(Files.newOutputStream(Paths.get(path)));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; filename=\"report.docx\"");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        InputStreamResource resource = new InputStreamResource(Files.newInputStream(f.toPath()));

        for (File file : Objects.requireNonNull(dir.listFiles())){
            if (file.isFile())
                file.delete();
        }

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(f.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    public XWPFDocument mergeDocuments(Stream<XWPFDocument> documents) {
        XWPFDocument mergedDocuments = new XWPFDocument();
        CTDocument1 mergedCTDocument = mergedDocuments.getDocument();
        mergedCTDocument.unsetBody();  // to remove blank first page in merged document
        documents.forEach(srcDocument -> {
            CTDocument1 srcCTDocument= srcDocument.getDocument();
            if (srcCTDocument != null) {
                CTBody srcCTBody = srcCTDocument.getBody();
                if (srcCTBody != null) {
                    CTBody mergedCTBody = mergedCTDocument.addNewBody();
                    mergedCTBody.set(srcCTBody);
                }
            }
        });
        return mergedDocuments;
    }

    private void printBilluten(Voting voting) throws Exception {
        File file = new File("documentTemplates/BillutenTemplate.docx");
        Iterable<Vote> votes = voteService.findAll().get();
        if (votes == null)
           throw new Exception("Votes not found");
        for(Vote vote: votes){
            if (vote.getVoting().getId() == voting.getId()){
                count++;
                try {
                    XWPFDocument doc = new XWPFDocument(OPCPackage.open(file));
                    for (XWPFParagraph p : doc.getParagraphs()) {
                        List<XWPFRun> runs = p.getRuns();
                        if (runs != null)
                            for (XWPFRun r : runs) {
                                if (r.getText(0) != null){
                                    r.setFontSize(14);
                                    r.setFontFamily("Times New Roman");
                                    String text = verifyBilluten(r.getText(0), vote);
                                    text = verifyParagraph(text, voting);
                                    r.setText(text, 0);
                                }
                            }
                    }
                    for (XWPFTable tbl : doc.getTables())
                        for (XWPFTableRow row : tbl.getRows())
                            for (XWPFTableCell cell : row.getTableCells())
                                for (XWPFParagraph p : cell.getParagraphs())
                                    for (XWPFRun r : p.getRuns()) {
                                        if (r.getText(0) != null){
                                            r.setFontSize(14);
                                            r.setFontFamily("Times New Roman");
                                            String text = verifyBilluten(r.getText(0), vote);
                                            if (text != null) {
                                                r.setText(text, 0);
                                            }
                                        }
                                    }
                    String path = "documentsResult/Billuten_" + count + ".docx";
                    doc.write(Files.newOutputStream(Paths.get(path)));
                } catch (InvalidFormatException | IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void printReport(Voting voting){

        try (FileInputStream fileInputStream = new FileInputStream("documentTemplates/ReportTemplate.docx")) {

            // открываем файл и считываем его содержимое в объект XWPFDocument
            XWPFDocument docxFile = new XWPFDocument(OPCPackage.open(fileInputStream));

            try (XWPFDocument doc = new XWPFDocument()) {
                // печатаем содержимое всех параграфов документа
                List<XWPFParagraph> paragraphs = docxFile.getParagraphs();
                for (XWPFParagraph p : paragraphs) {

                    String pResult = verifyParagraph(p.getText(), voting);
                    XWPFParagraph p1 = doc.createParagraph();
                    p1.setAlignment(ParagraphAlignment.BOTH);

                    XWPFRun r1 = p1.createRun();
                    r1.setBold(false);
                    r1.setItalic(false);
                    r1.setFontSize(14);
                    r1.setFontFamily("Times New Roman");
                    r1.setText(pResult);
                }
                // save it to .docx file
                new File("documentsResult/").mkdirs();
                count++;
                String path = "documentsResult/Report_" + count + ".docx";
                try (FileOutputStream out = new FileOutputStream(path)) {
                    doc.write(out);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String verifyParagraph(String p, Voting voting){
        return p.replace("votingQuestion", voting.getTheme())
                .replace("votingMembers", String.valueOf(voting.getUserSize()))
                .replace("voicesFor", String.valueOf(voting.getYes())).replace("voicesVersus", String.valueOf(voting.getNo()))
                .replace("voicesNeutral", String.valueOf(voting.getNeutral()))
                .replace("voicesBroken", String.valueOf(voting.getBroken()));
    }

    private String verifyBilluten(String p, Vote vote){
        if (p != null){
            return p.replace("VoicesFor", String.valueOf(vote.getYes()))
                    .replace("VoicesVersus", String.valueOf(vote.getNo()))
                    .replace( "VoicesNeutral", String.valueOf(vote.getNeutral()));
        }
        return null;
    }
}
