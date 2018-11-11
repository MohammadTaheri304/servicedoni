package io.zino.servicedoni.servlet;

import com.google.gson.Gson;
import io.zino.servicedoni.model.request.RegisterRequest;
import io.zino.servicedoni.serviceregistery.ServiceRegisteryEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegistrationServlet extends HttpServlet {
    private static final Gson gson = new Gson();
    private static Logger logger = LoggerFactory.getLogger(RegistrationServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RegisterRequest registerRequest = gson.fromJson(req.getReader(), RegisterRequest.class);
        ServiceRegisteryEngine.getInstance().register(registerRequest);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
