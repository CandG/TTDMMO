package cz.brno.candg.ttdmmo.frontend;

import cz.brno.candg.ttdmmo.backend.firebase.FbServer;
import cz.brno.candg.ttdmmo.serviceapi.GameService;
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
 * Servlet for setting FbServer state
 *
 * @author lastuvka
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = {"/server/*", "/set/*"})
public class FbServlet extends HttpServlet {

    final static Logger log = LoggerFactory.getLogger(FbServlet.class);

    @Inject
    FbServer fbServer;

    @Inject
    GameService gameService;

    private int isRunning = 0;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if ("/server".equals(request.getServletPath()) && "/start".equals(request.getPathInfo())) {
            if (isRunning == 0) {
                fbServer.start();
                isRunning = 1;
            }

            request.setAttribute("mylist", Arrays.asList("Server is running"));
            request.getRequestDispatcher("/server.jsp").forward(request, response);
            return;

        } else if ("/server".equals(request.getServletPath()) && "/stop".equals(request.getPathInfo())) {
            fbServer.stop();
            isRunning = 0;
            request.setAttribute("mylist", Arrays.asList("Server is stopped"));
            request.getRequestDispatcher("/server.jsp").forward(request, response);
            return;

        } else if ("/server".equals(request.getServletPath()) && "/generate".equals(request.getPathInfo())) {
            gameService.createGame(Integer.parseInt(request.getParameter("offset")));
            if (isRunning == 0) {
                request.setAttribute("mylist", Arrays.asList("Server is stopped "));

            } else {
                request.setAttribute("mylist", Arrays.asList("Server is running"));
            }
            request.getRequestDispatcher("/server.jsp").forward(request, response);
            return;

        } else if ("/server".equals(request.getServletPath())) {
            if (isRunning == 0) {
                request.setAttribute("mylist", Arrays.asList("Server is stopped "));

            } else {
                request.setAttribute("mylist", Arrays.asList("Server is running"));
            }
            request.getRequestDispatcher("/server.jsp").forward(request, response);
            return;
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Nothing here, you are lost");
            return;
        }

    }

}
