/*
 * class: BlockUserCommand
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

/**
 * @author Burishinets Maxim
 * @version 1.0 27 Sep 2018
 */
public class BlockUserCommand implements ActionCommand {

    private static final Logger LOG =
            LogManager.getLogger(BlockUserCommand.class);

    private static final String PARAM_USER_ID = "userId";
    private static final String PARAM_RESULT = "result";

    @Override
    public Router execute(HttpServletRequest request,
                          HttpServletResponse response) {

        String userId = request.getParameter(PARAM_USER_ID);
        userId = userId.replaceAll("[^0-9]","");
        int uid = Integer.parseInt(userId);
        try {
            boolean result = UserService.blockUser(uid);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(PARAM_USER_ID, uid);
                jsonObject.put(PARAM_RESULT, result);
                response.setContentType("application/json");
                response.getWriter().write(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (ProjectException | IOException e) {
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
