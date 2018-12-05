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
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
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
public class ParkingController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     * @throws java.text.ParseException
     */
    public boolean isWeekend(String date) throws ParseException {
        //System.out.println("STARTED");
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        cal.setTime(sdf.parse(date));
        //System.out.println(cal.get(Calendar.DAY_OF_WEEK));
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            //System.out.println("TRUE");
            return true;
        }
        //System.out.println("FALSE");
        return false;
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        response.setContentType("text/html;charset=UTF-8");

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
                + "?x record:duration ?duration. FILTER (?duration='nothing' && ?type='parking') "
                + "} ORDER BY DESC(?date) ";

        String queryString2 = "PREFIX record: <http://swproject2.org/record/>"
                + "SELECT ?duration "
                + "WHERE {"
                + "?x record:duration ?duration. "
                + "?x record:type ?type. FILTER (?type='parking' && ?duration!='nothing') "
                + "}"
                + "LIMIT 1000";

        String queryString3
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
                + "?x record:duration ?duration. FILTER (?duration!='nothing' && ?type='parking') "
                + "} ORDER BY DESC(?date) ";

        Query query1 = QueryFactory.create(queryString1);
        QueryExecution qexec1 = QueryExecutionFactory.create(query1, model);
        ResultSet unavailableResults = qexec1.execSelect();
        //ResultSetFormatter.out(baos, unavailableResults);
        //String unavailableRet = baos.toString();
        //System.out.println(unavailableRet);

        List<Record> unavailableRec = new LinkedList<>();
        while (unavailableResults.hasNext()) {
            QuerySolution temp = unavailableResults.next();
            unavailableRec.add(new Record(temp.getLiteral("name").toString(), temp.getLiteral("type").toString(), temp.getLiteral("location").toString(), temp.getLiteral("coordinates").toString(), temp.getLiteral("date").toString(), temp.getLiteral("time").toString(), temp.getLiteral("duration").toString()));
        }

        Query query2 = QueryFactory.create(queryString2);
        QueryExecution qexec2 = QueryExecutionFactory.create(query2, model);
        ResultSet averageResults = qexec2.execSelect();

        int total = 0;
        int noEntries = 0;
        while (averageResults.hasNext()) {
            QuerySolution temp = averageResults.next();
            noEntries++;
            total += Integer.parseInt(temp.getLiteral("duration").toString());
        }

        int average = total / noEntries;

        Query query3 = QueryFactory.create(queryString3);
        QueryExecution qexec3 = QueryExecutionFactory.create(query3, model);
        ResultSet weekendResults = qexec3.execSelect();

        List<Record> weekendRec = new LinkedList<>();
        while (weekendResults.hasNext()) {
            QuerySolution temp = weekendResults.next();
            //System.out.println(temp.getLiteral("date").toString());
            if (isWeekend(temp.getLiteral("date").toString())) {
                weekendRec.add(new Record(temp.getLiteral("name").toString(), temp.getLiteral("type").toString(), temp.getLiteral("location").toString(), temp.getLiteral("coordinates").toString(), temp.getLiteral("date").toString(), temp.getLiteral("time").toString(), temp.getLiteral("duration").toString()));
            }
        }

        request.setAttribute("weekendRec", weekendRec);
        request.setAttribute("unavailableRec", unavailableRec);
        request.setAttribute("average", average);
        request.getRequestDispatcher("parkingView.jsp").forward(request, response);

        //tot ce e marcat cu 5 in notepad e aici
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
            Logger.getLogger(ParkingController.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ParkingController.class.getName()).log(Level.SEVERE, null, ex);
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
