package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.logic.FacultyService;
import by.epam.admission.logic.UserService;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class CheckUsersCommand implements ActionCommand {

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) {

        String[] userIds = request.getParameterValues("userId[]");

        try {
            HashMap<Integer, Boolean> resultSet = UserService.checkUsers(userIds);

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("resultSet", resultSet);
                response.setContentType("application/json");
                response.getWriter().write(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (ProjectException e) {
            e.printStackTrace(); // TODO: STUB
        } catch (IOException e) {
            e.printStackTrace(); // TODO: STUB
        }

        return null; // STUB
    }

}
