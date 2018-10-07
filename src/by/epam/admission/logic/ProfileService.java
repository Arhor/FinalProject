/*
 * class: ProfileService
 */

package by.epam.admission.logic;

import by.epam.admission.model.User;
import by.epam.admission.util.ConfigurationManager;

/**
 * @author Burishinets Maxim
 * @version 1.0 06 Oct 2018
 */
public class ProfileService {

    private ProfileService() {}

    public static String definePage(User.Role role) {
        String page;
        switch (role) {
            case CLIENT:
                page = ConfigurationManager.getProperty(
                        "path.page.client.profile");
                break;
            case ADMIN:
                page = ConfigurationManager.getProperty(
                        "path.page.admin.profile");
                break;
            case GUEST:
            default:
                page = ConfigurationManager.getProperty("path.page.main");
        }
        return page;
    }
}
