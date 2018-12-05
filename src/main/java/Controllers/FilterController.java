/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Domain.Record;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.util.FileManager;

/**
 *
 * @author Veronica Marinescu
 */
public class FilterController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public boolean isLastHour(String date, String time) throws ParseException {

        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
        String inputDate = sm.format(sm.parse(date));
        String now = sm.format(Calendar.getInstance().getTime());

        int hour = LocalDateTime.now().getHour();
        int minutes = LocalDateTime.now().getMinute();

        int hourTest = Integer.parseInt(time.substring(0, 2));
        int minutesTest = Integer.parseInt(time.substring(3, 5));

        //System.out.println("DATES: " + inputDate + " TESTING FOR " + now + ". HOURS: " + hour + ":" + minutes + " TESTING FOR " + hourTest + ":" + minutesTest);

        if (inputDate.equals(now)) {
            if (hour == hourTest) {
                //System.out.println("True");
                return true;
            } else if (hourTest == hour - 1) {
                if (minutesTest >= minutes) {
                    return true;
                }
            }

        }
        //System.out.println("False");
        return false;
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        response.setContentType("text/html;charset=UTF-8");

        String hospital = request.getParameter("hospitalInput");
        String coordinates = request.getParameter("coordinatesInput");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Model model = FileManager.get().loadModel("C:\\Anul 4\\Semantic Web\\SmartCity\\SmartCityRDF.rdf");

        String queryString1
                // = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                = "PREFIX record: <http://swproject2.org/record/>"
                + "SELECT ?name ?type ?location ?coordinates ?date ?time ?duration "
                + "WHERE {"
                + "?x record:name ?name."
                + "?x record:type ?type."
                + "?x record:location ?location."
                + "?x record:coordinates ?coordinates."
                + "?x record:date ?date."
                + "?x record:time ?time."
                + "?x record:duration ?duration. FILTER (?location='" + hospital + "' && ?type='hospital') "
                + "} ORDER BY DESC(?date) ";

        String queryString2 = "PREFIX record: <http://swproject2.org/record/>"
                + "SELECT ?name ?type ?location ?coordinates ?date ?time ?duration "
                + "WHERE {"
                + "?x record:name ?name."
                + "?x record:type ?type."
                + "?x record:location ?location."
                + "?x record:coordinates ?coordinates."
                + "?x record:date ?date."
                + "?x record:time ?time."
                + "?x record:duration ?duration. FILTER (?coordinates='" + coordinates + "') "
                + "} ORDER BY DESC(?date) "
                + "LIMIT 10";

        String queryString3 = "PREFIX record: <http://swproject2.org/record/>"
                + "SELECT ?name ?type ?location ?coordinates ?date ?time ?duration "
                + "WHERE {"
                + "?x record:name ?name."
                + "?x record:type ?type."
                + "?x record:location ?location."
                + "?x record:coordinates ?coordinates."
                + "?x record:date ?date."
                + "?x record:time ?time."
                + "?x record:duration ?duration."
                + "} ORDER BY DESC(?date) "
                + "LIMIT 1000";

        String queryString4 = "PREFIX record: <http://swproject2.org/record/>"
                + "SELECT ?location (COUNT(?location) as ?countname) "
                + "WHERE {"
                + "?x record:name ?name."
                + "?x record:type ?type."
                + "?x record:location ?location."
                + "?x record:coordinates ?coordinates."
                + "?x record:date ?date."
                + "?x record:time ?time."
                + "?x record:duration ?duration."
                + "}"
                + "GROUP BY ?location "
                + "ORDER BY DESC(?countname) ?location "
                + "LIMIT 1";

        Query query1 = QueryFactory.create(queryString1);
        QueryExecution qexec1 = QueryExecutionFactory.create(query1, model);
        ResultSet hospitalResults = qexec1.execSelect();

        List<Record> hospitalRec = new LinkedList<>();
        while (hospitalResults.hasNext()) {
            QuerySolution temp = hospitalResults.next();
            hospitalRec.add(new Record(temp.getLiteral("name").toString(), temp.getLiteral("type").toString(), temp.getLiteral("location").toString(), temp.getLiteral("coordinates").toString(), temp.getLiteral("date").toString(), temp.getLiteral("time").toString(), temp.getLiteral("duration").toString()));
        }

        Query query2 = QueryFactory.create(queryString2);
        QueryExecution qexec2 = QueryExecutionFactory.create(query2, model);
        ResultSet coordinateResults = qexec2.execSelect();

        List<Record> coordsRec = new LinkedList<>();
        while (coordinateResults.hasNext()) {
            QuerySolution temp = coordinateResults.next();
            coordsRec.add(new Record(temp.getLiteral("name").toString(), temp.getLiteral("type").toString(), temp.getLiteral("location").toString(), temp.getLiteral("coordinates").toString(), temp.getLiteral("date").toString(), temp.getLiteral("time").toString(), temp.getLiteral("duration").toString()));
        }

        Query query3 = QueryFactory.create(queryString3);
        QueryExecution qexec3 = QueryExecutionFactory.create(query3, model);
        ResultSet timeResults = qexec3.execSelect();

        List<Record> timeRec = new LinkedList<>();
        while (timeResults.hasNext()) {
            QuerySolution temp = timeResults.next();
            if (isLastHour(temp.getLiteral("date").toString(), temp.getLiteral("time").toString())) {
                timeRec.add(new Record(temp.getLiteral("name").toString(), temp.getLiteral("type").toString(), temp.getLiteral("location").toString(), temp.getLiteral("coordinates").toString(), temp.getLiteral("date").toString(), temp.getLiteral("time").toString(), temp.getLiteral("duration").toString()));
            }
        }

        Query query4 = QueryFactory.create(queryString4);
        QueryExecution qexec4 = QueryExecutionFactory.create(query4, model);
        ResultSet countCoordResult = qexec4.execSelect();
        //ResultSetFormatter.out(baos, countCoordResult);
        //String mostCoordRet = baos.toString();
        //System.out.println(mostCoordRet);
        
        
        
        List<Record> countCoord = new LinkedList<>();
        while (countCoordResult.hasNext()) {
            QuerySolution temp = countCoordResult.next();
            countCoord.add(new Record(temp.getLiteral("location").toString(), Integer.toString(temp.getLiteral("countname").getInt())));
            System.out.println("added" + temp.getLiteral("location").toString() + " " + temp.getLiteral("countname").toString());
        }

        request.setAttribute("hospitalInput", hospital);
        request.setAttribute("coordsInput", coordinates);
        request.setAttribute("hospitalRec", hospitalRec);
        request.setAttribute("coordsRec", coordsRec);
        request.setAttribute("timeRec", timeRec);
        request.setAttribute("mostCoord", countCoord);
        request.getRequestDispatcher("dataView.jsp").forward(request, response);
        //tot ce e marcat cu 4 in notepad e facut aici
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(FilterController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(FilterController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
