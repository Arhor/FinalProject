/*
 * class: CheckUsersCommand
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.logic.UserService;
import by.epam.admission.util.Names;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author Burishinets Maxim
 * @version 1.0 27 Sep 2018
 */
public class CheckUsersCommand implements ActionCommand {

    private static final Logger LOG =
            LogManager.getLogger(CheckUsersCommand.class);

    @Override
    public Router execute(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {

        String[] userIds = request.getParameterValues(Names.USER_ID_ARRAY);
        try {
            HashMap<Integer, Boolean> resultSet =
                    UserService.checkUsers(userIds);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Names.RESULT_SET, resultSet);
            response.setContentType("application/json");
            response.getWriter().write(jsonObject.toString());
        } catch (ProjectException | IOException | JSONException e) {
            LOG.error("Checking users error", e);
            response.sendError(500);
        }
        return null;
    }

}
