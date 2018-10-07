/*
 * class: UnblockUserCommand
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.logic.UserService;
import by.epam.admission.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author Burishinets maxim
 * @version 1.0 10 Sep 2018
 */
public class UnblockUserCommand implements ActionCommand {

    private static final Logger LOG =
            LogManager.getLogger(UnblockUserCommand.class);

    private static final String PARAM_USER_ID = "userId";
    private static final String PARAM_RESULT = "result";
    private static final String ATTR_ROLE = "role";

    @Override
    public Router execute(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession();
        User.Role role = (User.Role) session.getAttribute(ATTR_ROLE);
        if (role == User.Role.ADMIN) {
            String userId = request.getParameter(PARAM_USER_ID);
            userId = userId.replaceAll("[^0-9]","");
            int uid = Integer.parseInt(userId);
            try {
                boolean result = UserService.unblockUser(uid);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(PARAM_USER_ID, uid);
                jsonObject.put(PARAM_RESULT, result);
                response.setContentType("application/json");
                response.getWriter().write(jsonObject.toString());
            } catch (ProjectException | JSONException e) {
                LOG.error("Unblocking user error", e);
                response.sendError(500);
            }
        } else {
            LOG.error("Invalid user role");
            response.sendError(403);
        }
        return null;
    }
}
