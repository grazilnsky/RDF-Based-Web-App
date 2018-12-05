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
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.util.FileManager;

/**
 *
 * @author Veronica Marinescu
 */
public class SortingController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        response.setContentType("text/html;charset=UTF-8");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //FileManager.get().addLocatorClassLoader(sparqlQuery.class.getClassLoader());
        Model model = FileManager.get().loadModel("C:\\Anul 4\\Semantic Web\\SmartCity\\SmartCityRDF.rdf");
        
        String queryString
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
                + "?x record:duration ?duration."
                + "} ORDER BY DESC (?date) ?time";
        
        String queryString2 = "PREFIX record: <http://swproject2.org/record/>"
                + "SELECT DISTINCT ?location "
                + "WHERE {"
                + "?x record:location ?location. "
                + "?x record:type ?type. FILTER (?type='hospital') "
                + "}"
                + "LIMIT 1000";

        
        String queryString3 = "PREFIX record: <http://swproject2.org/record/>"
                + "SELECT DISTINCT ?coordinates "
                + "WHERE {"
                + "?x record:coordinates ?coordinates. "
                + "}"
                + "LIMIT 1000";
        
        /*String queryString3 = "SELECT DISTINCT ?s\n"
                + "WHERE {\n"
                + "    ?s ?p ?o .\n"
                + "  FILTER (strends(str(?s), '/hospital'))\n"
                + "}\n"
                + "LIMIT 1000";*/

        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        ResultSet results = qexec.execSelect();

        List<Record> records = new LinkedList<>();
        while (results.hasNext()) {
            QuerySolution temp = results.next();
            records.add(new Record(temp.getLiteral("name").toString(), temp.getLiteral("type").toString(), temp.getLiteral("location").toString(), temp.getLiteral("coordinates").toString(), temp.getLiteral("date").toString(), temp.getLiteral("time").toString(), temp.getLiteral("duration").toString()));
        }
        /*for(Record r:records){           
            System.out.print(r.getName() + " " + r.getType() + " " + r.getLocation() + " " + r.getDate() + " " + r.getTime() + " " + r.getDuration());
            r.isThisWeek();
        }*/
        
        Query query2 = QueryFactory.create(queryString2);
        QueryExecution qexec2 = QueryExecutionFactory.create(query2, model);
        ResultSet hospitalResults = qexec2.execSelect();
        //ResultSetFormatter.out(baos, hospitalResults);
        //String hospitalRet = baos.toString();
        //System.out.println(hospitalRet);
        
        List<String> hospitals = new LinkedList<>();
        while(hospitalResults.hasNext()){
            QuerySolution temp = hospitalResults.next();
            hospitals.add(temp.getLiteral("location").toString());
            //System.out.println(temp.getLiteral("location").toString());
        }
        
        Query query3 = QueryFactory.create(queryString3);
        QueryExecution qexec3 = QueryExecutionFactory.create(query3, model);
        ResultSet coordinateResults = qexec3.execSelect();
        //ResultSetFormatter.out(baos, coordinateResults);
        //String coordinateRet = baos.toString();
        //System.out.println(coordinateRet);
        
        List<String> coordinates = new LinkedList<>();
        while(coordinateResults.hasNext()){
            QuerySolution temp = coordinateResults.next();
            coordinates.add(temp.getLiteral("coordinates").toString());
            //System.out.println(temp.getLiteral("coordinates").toString());
        }
        
        request.setAttribute("coordinates", coordinates);
        request.setAttribute("records", records);
        request.setAttribute("hospitals", hospitals);
        
        request.getRequestDispatcher("recordsView.jsp").forward(request, response);
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
            Logger.getLogger(SortingController.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(SortingController.class.getName()).log(Level.SEVERE, null, ex);
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
