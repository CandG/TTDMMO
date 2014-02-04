package cz.brno.candg.ttdmmo.frontend;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Zdenek Lastuvka
 *
 */
@SuppressWarnings("serial")
@WebServlet("/map")
public class MainServlet extends HttpServlet {

    final static Logger log = LoggerFactory.getLogger(MainServlet.class);
    private static int cislo = 0;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Start Servlet MainServlet: " + cislo);
        cislo++;
        RequestDispatcher rd = req.getRequestDispatcher("/app.html");
        rd.forward(req, resp);

    }

}
