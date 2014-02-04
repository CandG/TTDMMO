/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.frontend;

import cz.brno.candg.ttdmmo.frontend.firebase.BusController;
import cz.brno.candg.ttdmmo.frontend.firebase.FbVehiclePathServer;
import cz.brno.candg.ttdmmo.frontend.firebase.FirebaseMapServer;
import cz.brno.candg.ttdmmo.frontend.firebase.FirebaseNewVehicleServer;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lastuvka
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = {"/server/*", "/set/*"})
public class FbServlet extends HttpServlet {

    final static Logger log = LoggerFactory.getLogger(FbServlet.class);
    private static int cislo = 0;

    @Inject
    FirebaseMapServer fs;

    @Inject
    FirebaseNewVehicleServer fnvs;

    @Inject
    FbVehiclePathServer fvps;

    @Inject
    BusController bc;

    private int mapServer = 0;
    private int newVehicleServer = 0;
    private int pathVehicleServer = 0;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("Start Servlet FbServlet: " + cislo);
        cislo++;

        if ("/server".equals(request.getServletPath()) && "/start".equals(request.getPathInfo())) {
            if (mapServer == 0) {
                mapServer = 1;
                newVehicleServer = 1;
                pathVehicleServer = 1;
                fs.start();
                fnvs.start();
                fvps.start();
                bc.start();
            }
            request.setAttribute("mylist", Arrays.asList("mapServer: " + mapServer, "newVehicleServer: " + newVehicleServer, "pathVehicleServer: " + pathVehicleServer));
            request.getRequestDispatcher("/server.jsp").forward(request, response);
            return;

        } else if ("/server".equals(request.getServletPath()) && "/stop".equals(request.getPathInfo())) {
            mapServer = 0;
            newVehicleServer = 0;
            pathVehicleServer = 0;
            fs.stop();
            fnvs.stop();
            fvps.stop();
            bc.stop();
            request.setAttribute("mylist", Arrays.asList("mapServer: " + mapServer, "newVehicleServer: " + newVehicleServer, "pathVehicleServer: " + pathVehicleServer));
            request.getRequestDispatcher("/server.jsp").forward(request, response);
            return;

        } else if ("/server".equals(request.getServletPath())) {
            request.setAttribute("mylist", Arrays.asList("mapServer: " + mapServer, "newVehicleServer: " + newVehicleServer, "pathVehicleServer: " + pathVehicleServer));
            request.getRequestDispatcher("/server.jsp").forward(request, response);
            return;
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Nothing here, you are lost");
            return;
        }

    }

}
