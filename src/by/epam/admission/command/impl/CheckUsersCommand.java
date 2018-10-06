/*
 * class: CheckUsersCommand
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.logic.UserService;
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

    private static final String PARAM_USER_ID_ARRAY = "userId[]";
    private static final String PARAM_RESULT_SET = "resultSet";

    @Override
    public Router execute(HttpServletRequest request,
                          HttpServletResponse response) {

        String[] userIds = request.getParameterValues(PARAM_USER_ID_ARRAY);
        try {
            HashMap<Integer, Boolean> resultSet =
                    UserService.checkUsers(userIds);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(PARAM_RESULT_SET, resultSet);
            response.setContentType("application/json");
            response.getWriter().write(jsonObject.toString());
        } catch (ProjectException | IOException | JSONException e) {
            LOG.error(e);
            try {
                response.sendError(500);
            } catch (IOException e1) {
                LOG.error(e1);
            }
        }
        return null;
    }

}
