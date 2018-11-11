package io.zino.servicedoni.servlet;

import com.google.gson.Gson;
import io.zino.servicedoni.model.response.ServiceResponse;
import io.zino.servicedoni.serviceregistery.ServiceRegisteryEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServiceRequestServlet extends HttpServlet {
    private static final Gson gson = new Gson();
    private static Logger logger = LoggerFactory.getLogger(ServiceRequestServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String reqServiceName = req.getParameter("service");
        ServiceResponse result = ServiceRegisteryEngine.getInstance().getServiceByName(reqServiceName);

        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(gson.toJson(result));
    }
}
