/*
 * class: BlockUserCommand
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.logic.UserService;
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
        if (role == User.Role.ADMIN) {
            String userId = request.getParameter(Names.USER_ID);
            userId = userId.replaceAll("[^0-9]","");
            int uid = Integer.parseInt(userId);
            try {
                boolean result = UserService.blockUser(uid);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(Names.USER_ID, uid);
                jsonObject.put(Names.RESULT, result);
                router.setType(Router.Type.AJAX);
                router.setJsonObject(jsonObject);
            } catch (ProjectException | JSONException e) {
                LOG.error("Blocking user error", e);
                router.setType(Router.Type.ERROR);
                router.setErrorCode(500);
            }
        } else {
            LOG.error("Invalid user role");
            router.setType(Router.Type.ERROR);
            router.setErrorCode(403);
        }
        return router;
    }
}
