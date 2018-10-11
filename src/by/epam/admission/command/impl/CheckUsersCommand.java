/*
 * class: CheckUsersCommand
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.service.UserService;
import by.epam.admission.util.Names;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @author Burishinets Maxim
 * @version 1.0 27 Sep 2018
 */
public class CheckUsersCommand implements ActionCommand {

    private static final Logger LOG =
            LogManager.getLogger(CheckUsersCommand.class);

    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        JSONObject jsonObject = new JSONObject();
        String[] userIds = request.getParameterValues(Names.USER_ID_ARRAY);
        try {
            HashMap<Integer, Boolean> resultSet =
                    UserService.checkUsers(userIds);
            jsonObject.put(Names.RESULT_SET, resultSet);
        } catch (ProjectException | JSONException e) {
            LOG.error("Checking users error", e);
            try {
                jsonObject.put("error", true);
            } catch (JSONException e1) {
                LOG.error("JSON error", e1);
            }
        }
        router.setType(Router.Type.AJAX);
        router.setJsonObject(jsonObject);
        return router;
    }

}
