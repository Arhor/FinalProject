/*
 * class: BlockUserCommand
 */

package by.epam.admission.command.impl;

import by.epam.admission.command.ActionCommand;
import by.epam.admission.command.Router;
import by.epam.admission.exception.ProjectException;
import by.epam.admission.service.UserService;
import by.epam.admission.model.User;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static by.epam.admission.util.Names.*;

/**
 * Class BlockUserCommand implements command that signals to block concrete user
 * by UserID and update corresponding record in the database
 *
 * @author Burishinets Maxim
 * @version 1.0 27 Sep 2018
 * @see ActionCommand
 */
public class BlockUserCommand implements ActionCommand {

    private static final Logger LOG =
            LogManager.getLogger(BlockUserCommand.class);

    /**
     * Method at first current session's role and if it not an ADMIN returns
     * JSON with 'error' flag. Otherwise retrieves 'userId' parameter from HTTP
     * request and tries to to block User with corresponding ID. After all the
     * results are placed in the JSON object, which is placed in a Router object
     *
     * @param request {@link HttpServletRequest} object received from
     *                controller-servlet
     * @return {@link Router} object that contains result of executing concrete
     * command
     */
    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();
        HttpSession session = request.getSession();
        User.Role role = (User.Role) session.getAttribute(ROLE);
        JSONObject jsonObject = new JSONObject();
        if (role == User.Role.ADMIN) {
            String userId = request.getParameter(USER_ID);
            userId = userId.replaceAll("[^0-9]","");
            int uid = Integer.parseInt(userId);
            try {
                boolean result = UserService.blockUser(uid);
                jsonObject.put(USER_ID, uid);
                jsonObject.put(RESULT, result);
            } catch (ProjectException | JSONException e) {
                LOG.error("Blocking user error", e);
                try {
                    jsonObject.put(ERROR, true);
                } catch (JSONException e1) {
                    LOG.error("JSON error", e1);
                }
            }
        } else {
            LOG.error("Invalid user role");
            try {
                jsonObject.put(ERROR, true);
            } catch (JSONException e) {
                LOG.error("JSON error", e);
            }
        }
        router.setType(Router.Type.AJAX);
        router.setJsonObject(jsonObject);
        return router;
    }
}
