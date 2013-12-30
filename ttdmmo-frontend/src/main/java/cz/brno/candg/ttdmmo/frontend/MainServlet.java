package cz.brno.candg.ttdmmo.frontend;

import cz.brno.candg.ttdmmo.frontend.firebase.FirebaseServer;
import cz.brno.candg.ttdmmo.serviceapi.UserService;
import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Zdenek Lastuvka
 *
 */
@SuppressWarnings("serial")
@WebServlet("/map")
public class MainServlet extends HttpServlet {

    @Inject
    FirebaseServer fs;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher("/app.html");
        rd.forward(req, resp);
    }

}
