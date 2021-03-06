package server.controllers;

import server.Logger;
import server.models.Admin;
import server.models.services.AdminService;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("admin/")
public class AdminController {

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String attemptLogin(@FormParam("username") String username,
                             @FormParam("password") String password ) {

        Logger.log("/user/login - Attempt by " + username);
        AdminService.selectAllInto(Admin.admins);
        for (Admin a: Admin.admins) {
            if (a.getUsername().toLowerCase().equals(username.toLowerCase())) {
                if (!a.getPassword().equals(password)) {
                    return "Error: Incorrect password";
                }
                String token = UUID.randomUUID().toString();
                a.setSessionToken(token);
                String success = AdminService.update(a);
                if (success.equals("OK")) {
                    return token;
                } else {
                    return "Error: Can't create session token.";
                }
            }
        }
        return "Error: Can't find user account.";
    }

    @GET
    @Path("check")
    @Produces(MediaType.TEXT_PLAIN)
    public String checkLogin(@CookieParam("sessionToken") Cookie sessionCookie) {

        Logger.log("/admin/check - Checking user against database");

        String currentUser = validateSessionCookie(sessionCookie);

        if (currentUser == null) {
            Logger.log("Error: Invalid user session token");
            return "";
        } else {
            return currentUser;
        }
    }


    public static String validateSessionCookie(Cookie sessionCookie) {
        if (sessionCookie != null) {
            String token = sessionCookie.getValue();
            String result = AdminService.selectAllInto(Admin.admins);
            if (result.equals("OK")) {
                for (Admin a : Admin.admins) {
                    if (a.getSessionToken().equals(token)) {
                        Logger.log("Valid session token received.");
                        return a.getUsername();
                    }
                }
            }
        }
        return null;
    }

}
