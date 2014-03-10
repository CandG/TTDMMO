/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.brno.candg.ttdmmo.frontend;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import cz.brno.candg.ttdmmo.backend.firebase.FbServerTime;
import cz.brno.candg.ttdmmo.constants.FbRef;
import cz.brno.candg.ttdmmo.frontend.firebase.BusController;
import cz.brno.candg.ttdmmo.frontend.firebase.FbChangeColorServer;
import cz.brno.candg.ttdmmo.frontend.firebase.FbVehiclePathServer;
import cz.brno.candg.ttdmmo.frontend.firebase.FbMapServer;
import cz.brno.candg.ttdmmo.frontend.firebase.FbNewUserServer;
import cz.brno.candg.ttdmmo.frontend.firebase.FbNewVehicleServer;
import cz.brno.candg.ttdmmo.frontend.firebase.FbSellVehicleServer;
import java.io.IOException;
import java.util.Arrays;
import javax.inject.Inject;
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
    FbServerTime serverTime;

    @Inject
    FbSellVehicleServer sellVehicle;

    @Inject
    FbMapServer fbMap;

    @Inject
    FbNewVehicleServer newVehicle;

    @Inject
    FbVehiclePathServer pathVehicle;

    @Inject
    BusController busController;

    @Inject
    FbNewUserServer fbNewUserServer;

    @Inject
    FbChangeColorServer fbChangeColorServer;

    private int mapServer = 0;
    private int newVehicleServer = 0;
    private int pathVehicleServer = 0;

    Firebase dataRef = new Firebase(FbRef.refQ);

    public FbServlet() {
        log.info("Start Servlet FbServlet: " + cislo);
        cislo++;
        dataRef.auth("nkmw6PSBrcg8FgLBevSRlkIVug1vt1wte9684piE", new Firebase.AuthListener() {

            @Override
            public void onAuthError(FirebaseError error) {
                System.err.println("Login Failed! " + error.getMessage());
            }

            @Override
            public void onAuthSuccess(Object authData) {
                System.out.println("Login Succeeded!");

            }

            @Override
            public void onAuthRevoked(FirebaseError fe) {
                System.err.println("Authenticcation status was cancelled! " + fe.getMessage());
            }

        });
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if ("/server".equals(request.getServletPath()) && "/start".equals(request.getPathInfo())) {
            if (mapServer == 0) {
                serverTime.start();
                mapServer = 1;
                newVehicleServer = 1;
                pathVehicleServer = 1;
                fbMap.start();
                newVehicle.start();
                pathVehicle.start();
                busController.start();
                sellVehicle.start();
                fbNewUserServer.start();
                fbChangeColorServer.start();
            }

            request.setAttribute("mylist", Arrays.asList("mapServer: " + mapServer, "newVehicleServer: " + newVehicleServer, "pathVehicleServer: " + pathVehicleServer));
            request.getRequestDispatcher("/server.jsp").forward(request, response);
            return;

        } else if ("/server".equals(request.getServletPath()) && "/stop".equals(request.getPathInfo())) {
            mapServer = 0;
            newVehicleServer = 0;
            pathVehicleServer = 0;
            fbMap.stop();
            newVehicle.stop();
            pathVehicle.stop();
            busController.stop();
            serverTime.stop();
            sellVehicle.stop();
            fbNewUserServer.stop();
            fbChangeColorServer.stop();
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
