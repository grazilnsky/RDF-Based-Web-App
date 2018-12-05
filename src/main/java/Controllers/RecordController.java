/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.FileManager;
import java.util.Random;

/**
 *
 * @author Veronica Marinescu
 */
public class RecordController extends HttpServlet {

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
            throws ServletException, IOException {
        String path = "C:\\Anul 4\\Semantic Web\\SmartCity\\SmartCityRDF.rdf";
        String path2 = "C:\\Anul 4\\Semantic Web\\SmartCity\\src\\main\\webapp\\WEB-INF\\infoID.txt";
        Random r = new Random();
        int id = Integer.parseInt(FileUtils.readFileToString(new File(path2), "UTF-8"));
        System.out.println(id);
        Model model = ModelFactory.createDefaultModel();
        String base = "http://swproject2.org/record";

        // use the FileManager to find the input file
        InputStream in = FileManager.get().open(path);
        if (in == null) {
            throw new IllegalArgumentException("File: " + path + " not found");
        }

        // read the RDF/XML file
        if (request.getParameter("type").equals("parking")) {
            model.read(in, null);
            Resource name = model.createResource("http://swproject2.org/record/" + request.getParameter("name").replace(" ", "") + Integer.toString(id));

            Property rName = model.createProperty("record:name");
            Property type = model.createProperty("record:type");
            Property location = model.createProperty("record:location");
            Property coordinates = model.createProperty("record:coordinates");
            Property date = model.createProperty("record:date");
            Property time = model.createProperty("record:time");
            Property duration = model.createProperty("record:duration");

            int chance = r.nextInt(2);
            if (chance == 2) {
                name.addProperty(duration, Integer.toString(r.nextInt(1000)));
            } else {
                name.addProperty(duration, "nothing");
            }

            name.addProperty(time, request.getParameter("time"));
            name.addProperty(date, request.getParameter("date"));
            name.addProperty(coordinates, request.getParameter("coordinates"));
            name.addProperty(location, request.getParameter("location"));
            name.addProperty(type, request.getParameter("type"));
            name.addProperty(rName, request.getParameter("name"));
        } else {
            model.read(in, null);

            Resource name = model.createResource("http://swproject2.org/record/" + request.getParameter("name").replace(" ", "") + Integer.toString(id));

            Property rName = model.createProperty("record:name");
            Property type = model.createProperty("record:type");
            Property location = model.createProperty("record:location");
            Property coordinates = model.createProperty("record:coordinates");
            Property date = model.createProperty("record:date");
            Property time = model.createProperty("record:time");
            Property duration = model.createProperty("record:duration");

            name.addProperty(duration, Integer.toString(r.nextInt(1000)));
            name.addProperty(time, request.getParameter("time"));
            name.addProperty(date, request.getParameter("date"));
            name.addProperty(coordinates, request.getParameter("coordinates"));
            name.addProperty(location, request.getParameter("location"));
            name.addProperty(type, request.getParameter("type"));
            name.addProperty(rName, request.getParameter("name"));
        }

        // write it to standard out
        try {

            FileOutputStream fout = new FileOutputStream(path);
            model.write(fout);

            String content = FileUtils.readFileToString(new File(path), "UTF-8");
            content = content.replace("xmlns:j.0=\"record:\"", "");
            content = content.replace("j.0", "record");
            try (PrintWriter out = new PrintWriter(path)) {
                out.println(content);
            }
            try (PrintWriter out = new PrintWriter(path2)) {
                out.print(id + 1);
            }

        } catch (IOException e) {
            System.out.println("Exception caught" + e.getMessage());
        }

        request.getRequestDispatcher("index.jsp").forward(request, response);
        //adauga record
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
        processRequest(request, response);
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
        processRequest(request, response);
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
