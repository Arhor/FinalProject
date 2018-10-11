/*
 * class: BlockUserCommand
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.service.UserService;
import by.epam.admission.model.User;
import by.epam.admission.util.Names;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Burishinets Maxim
 * @version 1.0 27 Sep 2018
 */
public class BlockUserCommand implements ActionCommand {

    private static final Logger LOG =
            LogManager.getLogger(BlockUserCommand.class);

    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        HttpSession session = request.getSession();
        User.Role role = (User.Role) session.getAttribute(Names.ROLE);
        JSONObject jsonObject = new JSONObject();
        if (role == User.Role.ADMIN) {
            String userId = request.getParameter(Names.USER_ID);
            userId = userId.replaceAll("[^0-9]","");
            int uid = Integer.parseInt(userId);
            try {
                boolean result = UserService.blockUser(uid);
                jsonObject.put(Names.USER_ID, uid);
                jsonObject.put(Names.RESULT, result);
            } catch (ProjectException | JSONException e) {
                LOG.error("Blocking user error", e);
                try {
                    jsonObject.put("error", true);
                } catch (JSONException e1) {
                    LOG.error("JSON error", e1);
                }
            }
        } else {
            LOG.error("Invalid user role");
            try {
                jsonObject.put("error", true);
            } catch (JSONException e) {
                LOG.error("JSON error", e);
            }
        }
        router.setType(Router.Type.AJAX);
        router.setJsonObject(jsonObject);
        return router;
    }
}
