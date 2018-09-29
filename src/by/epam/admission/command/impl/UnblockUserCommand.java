package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.logic.UserService;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UnblockUserCommand implements ActionCommand {

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter("userId").replaceAll("[^0-9]","");

        int uid = Integer.parseInt(userId);

        try {
            boolean result = UserService.unblockUser(uid);

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("userId", uid);
                jsonObject.put("result", result);
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

        return null;
    }
}
